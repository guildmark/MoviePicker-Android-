package se.umu.maka0437.ou3;

import static se.umu.maka0437.ou3.FilterActivity.EXTRA_COUNTRY;
import static se.umu.maka0437.ou3.FilterActivity.EXTRA_GENRE;
import static se.umu.maka0437.ou3.FilterActivity.EXTRA_YEAR;
import static se.umu.maka0437.ou3.RegisterActivity.EXTRA_USER;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.opencsv.CSVReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends ToolbarActivity {

    private static final String MOVIE_KEY = "MOVIE_KEY";
    private static final String GENRE_KEY = "GENRE_KEY";
    private static final String POS_KEY = "POS_KEY";
    private static final String DESC_KEY = "DESC_KEY";

    ArrayList<String[]> movieList = new ArrayList<>();
    TextView welcomeText, movieText, positionText, genreText, descriptionText;
    Movie currentMovie;
    List<Movie> filteredMovies;

    Position currentPos;
    String currentGenre, currentCountry, currentDescription;
    int currentYear;
    String testYear;
    User currentUser;
    String userCountry;
    ImageView mainImage;
    View titleText;

    private FusedLocationProviderClient fusedLocationProviderClient;

    //New way of starting a new activity for result
    ActivityResultLauncher<Intent> startForResult=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(it)-> {
        if (it.getResultCode() == Activity.RESULT_OK) {
            Intent intent=it.getData();
            if (intent != null) {
                //Get the current values from the filter
                //testYear = intent.getStringExtra(EXTRA_YEAR);
                currentYear = intent.getIntExtra(EXTRA_YEAR, 0);
                currentGenre = intent.getStringExtra(EXTRA_GENRE);
                currentCountry = intent.getStringExtra(EXTRA_COUNTRY);
                //currentDescription = intent.getStringExtra(EXTRA_DESC);
                currentUser = intent.getParcelableExtra(EXTRA_USER); //Get current user
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appBar = findViewById(R.id.toolbar);
        setSupportActionBar(appBar);

        //welcomeText = findViewById(R.id.welcomeText);
        movieText = findViewById(R.id.movieText);
        genreText = findViewById(R.id.genreText);
        descriptionText = findViewById(R.id.descriptionText);
        //positionText = findViewById(R.id.posText);
        //countryText = findViewById(R.id.countryText);
        mainImage = findViewById(R.id.mainImage);
        titleText = findViewById(R.id.titleScreen);

        //Get back state
        if(savedInstanceState != null) {
            currentMovie = savedInstanceState.getParcelable(MOVIE_KEY);
            currentPos = savedInstanceState.getParcelable(POS_KEY);
            currentDescription = savedInstanceState.getString(DESC_KEY);
            //Set correct texts
            movieText.setText(currentMovie.title + " (" + currentMovie.releaseYear + ")");
            genreText.setText(currentMovie.genre);
            descriptionText.setText(currentDescription);
        }
        else {
            currentMovie = new Movie();
            showTitle = true;
        }

        Button movieButton = findViewById(R.id.findMovieButton);
        movieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Find a specific movie
                if (currentMovie == null) {
                    //Import default CSV file if no movies are found
                    try {
                        importDefaultCSV("WATCHLIST.csv");
                        Toast.makeText(MainActivity.this,
                                "No list found, importing default WATCHLIST...",
                                Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getMovie();
            }
        });

        //Set image as film roll for default
        mainImage.setImageResource(R.drawable.film_roll);
    }

    //Use when coming back from other activites
    @Override
    protected void onResume() {
        super.onResume();

        //Add imported movies to database
        Intent intent = getIntent();
        //movieList = intent.getStringArrayExtra();

    }

    private void getMovie() {
        //Get a random movie from the current database and display it on screen

        //Hide the title screen
        titleText.setVisibility(View.INVISIBLE);

        if(currentYear != 0) {
            //currentList = db.movieDao().findByYear(currentYear);
            getFilteredMovie(currentYear, currentGenre);
        }
        else {
            getRandomMovie();
        }
        //Get the description for the movie
        //Get from API only once, after that append to the DB entry

        //PROBLEM: ALTERNERAR MELLAN IF-SATSER
        //if(currentMovie.description.equals("")) {
        //Update DB entry with new movie description, less API calls!
        getMovieDescriptionAPI(currentMovie.imdbID);
        //}
        /*
        else {
            //Get the description from the database
            //currentMovie.description = getMovieDescriptionDB(currentMovie.uid);
            getMovieDescDB(currentMovie.uid);
        }
        */


        descriptionText.setText(currentMovie.description);

        movieText.setText(new StringBuilder().append(currentMovie.title).append(" (").append(currentMovie.releaseYear).append(")").toString());

        //movieText.setText(currentMovie.title + " (" + currentMovie.releaseYear + ")");
        genreText.setText(currentMovie.genre);

        //Change the image to correct depending on genre (Avoid movie images for copyright)
        setImage(mainImage);


    }


    private void getRandomMovie() {
        //Need to run on new thread because it otherwise might lock UI (Illegal state exception)

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Getting movie...");
                currentMovie = AppDatabase.getInstance(getApplicationContext())
                        .movieDao()
                        .getRandomMovie();
                System.out.println("Found movie: " + currentMovie.title + " (" + currentMovie.releaseYear + ")" + " IMDb ID: " + currentMovie.imdbID);
            }
        });

        thread.start();
    }

    private void getMovieDescDB(int id) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                currentMovie.description = AppDatabase.getInstance(getApplicationContext())
                        .movieDao()
                        .getDescription(id);
            }
        });
        thread.start();
    }




    private void insertUser(User user) {
        new Thread(() -> AppDatabase.getInstance(getApplicationContext()).userDao().insertUser(user));
    }

    private void deleteUser(User user) {
        new Thread(() -> AppDatabase.getInstance(getApplicationContext()).userDao().deleteUser(user));
    }

    //Insert movie into database
    private void insertMovie(Movie movie) {
        new Thread(() -> AppDatabase.getInstance(getApplicationContext()).movieDao().insertMovie(movie)).start();
    }

    //Function to add description to the movie database so that the API call is no longer required
    private void updateMovie(String desc, int id) {
        new Thread(() -> AppDatabase.getInstance(getApplicationContext()).movieDao().updateDesc(desc, id)).start();
    }

    //Delete a movie from database
    private void deleteMovie(Movie movie) {
        new Thread(() -> AppDatabase.getInstance(getApplicationContext()).movieDao().deleteMovie(movie)).start();
    }

    /*
    //Get the description from the database
    private String getMovieDescriptionDB(int id) {
        return db.movieDao().getDescription(id);
    }
    */



    @Override
    public void onSaveInstanceState(Bundle saveInstanceState)  {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putParcelable(MOVIE_KEY, currentMovie);
        saveInstanceState.putParcelable(POS_KEY, currentPos);
        saveInstanceState.putString(DESC_KEY, currentDescription);

    }

    private String getImdbID() {

        int random = ThreadLocalRandom.current().nextInt(1000000, 1999999);
        String ID = "tt" + random;
        //Add more criteria? Find good API
        return ID;
    }

    private void getFilteredMovie(int year, String genre) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                filteredMovies = AppDatabase.getInstance(getApplicationContext()).movieDao().findByAll(year, genre);
                int randomInt = ThreadLocalRandom.current().nextInt(0, filteredMovies.size());
                currentMovie = filteredMovies.get(randomInt);
            }
        });
        thread.start();
    }

    private void getMovieDescriptionAPI(String imdbTag) {
        //Use The Movie Database API to get a random movie (OMDbapi.com)
        String APIkey = "9297e7";

        //Get movies from the OMDb API
        //TODO only use completely random if user has no preference
        String testURL = "http://www.omdbapi.com/?apikey=" + APIkey + "&i=" + imdbTag;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonReq = new JsonObjectRequest(
                Request.Method.GET,
                testURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: Handle response
                Log.e("Data:", response.toString());

                try {
                    //Get the description for the movie
                    if(currentMovie.title == response.getString("Title")) {
                        currentMovie.description = response.getString("Plot");
                        descriptionText.setText(currentMovie.description);
                        updateMovie(currentMovie.description, currentMovie.uid);
                        //System.out.println("Added description: " + currentMovie.description + "to" + " movie: " + currentMovie.title);

                    }
                    else {
                        currentMovie.description = response.getString("Plot");
                        descriptionText.setText(currentMovie.description);
                        updateMovie(currentMovie.description, currentMovie.uid);
                        //Update the database entry with description
                    }
                } catch (JSONException e) {
                    System.out.println(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR:", error.toString());
            }
        });

        queue.add(jsonReq);
    }

    private void getMovieAPI(String imdbTag) {

        //Use The Movie Database API to get a random movie (OMDbapi.com)
        String APIkey = "9297e7";

        //Get movies from the OMDb API
        //TODO only use completely random if user has no preference
        String testURL = "http://www.omdbapi.com/?apikey=" + APIkey + "&i=" + imdbTag;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonReq = new JsonObjectRequest(
                Request.Method.GET,
                testURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: Handle response
                Log.e("Data:", response.toString());

                try {

                    String title = response.getString("Title");
                    String description = response.getString("Plot");
                    String country = response.getString("Country");
                    int releaseYear = Integer.parseInt(response.getString("Year"));

                    currentMovie.title = title;
                    currentMovie.description = description;
                    currentMovie.releaseYear = releaseYear;
                    currentMovie.country = country;

                    movieText.setText(currentMovie.title + " (" + currentMovie.releaseYear + ")");
                    genreText.setText(currentMovie.genre);
                    descriptionText.setText(currentMovie.description);

                } catch (JSONException e) {
                    System.out.println(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR:", error.toString());
            }
        });

        queue.add(jsonReq);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                // User chose the "Settings" item, show the app settings UI...
                goToFilterActivity();
                return true;

            case R.id.action_profile:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                goToProfileActivity();
                return true;

            case R.id.action_importList:
                //Import CSV file to database
                try {
                    importDefaultCSV("WATCHLIST.csv");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "WATCHLIST imported!", Toast.LENGTH_SHORT).show();

                //openFile();
                //Let user choose their own file to import
                return true;

            case R.id.action_register:
                goToRegisterActivity();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void goToFilterActivity() {
        Intent intent = new Intent(this, FilterActivity.class);
        //Add current filter options so it doesn't reset all the time
        intent.putExtra(EXTRA_YEAR, currentYear);
        startForResult.launch(intent);
    }

    private void goToProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);
    }

    //Function to import list from a CSV file to database
    private void importDefaultCSV(String fileName) throws IOException {

        String csvFileString = this.getApplicationInfo().dataDir + File.separatorChar
                + fileName;

        try {

            InputStreamReader is = new InputStreamReader(getAssets().open(fileName));
            CSVReader csvReader = new CSVReader(is);
            String[] line;

            csvReader.readNext();

            while((line = csvReader.readNext()) != null) {
                //Add the CSV elements to the database
                movieList.add(line);
                //Get relevant movie data -- Below is specifically CSV from imdb
                String imdbID = line[1];
                String description = line[4];
                String name = line[5];
                int year = Integer.parseInt(line[10]);
                int runTime = Integer.parseInt(line[9]);
                String genre = line[11];
                String directors = line[14];

                Movie movie = new Movie(name, year, runTime, genre, description, imdbID);
                insertMovie(movie);
            }

            //Send parcel back to main activity with data
            //goToMainActivity();



        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }

    }

    //Let user choose file to import

    void importCSV() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        //startActivityForResult(chooseFile, );
    }


    private void getUserPosition() {
        //Check for the permission
        if(getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //Get location
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null) {

                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();

                                currentPos = new Position(latitude, longitude);
                                currentPos.setLatitude(latitude);
                                currentPos.setLongitude(longitude);
                                Toast.makeText(MainActivity.this, "Lat: " + latitude + " - Long: " + longitude, Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
        }
        else {
            //If there is no permission, ask the user for it
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }


    }

    private void getUserCountry() {

        getUserPosition();

        Geocoder geocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(63.828869, 20.256670, 1);
            if(addresses.size() > 0) {
                userCountry = addresses.get(0).getCountryName();
                Toast.makeText(this, "You are currently in " + userCountry, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setImage(ImageView image) {

        if(currentMovie.genre.contains("Drama")) {
            mainImage.setImageResource(R.drawable.drama);
        }
        else if(currentMovie.genre.contains("Sci-Fi")) {
            mainImage.setImageResource(R.drawable.sci_fi);
        }
        else if(currentMovie.genre.contains("Action")) {
            mainImage.setImageResource(R.drawable.people);
        }
        else if(currentMovie.genre.contains("Romance")) {
            mainImage.setImageResource(R.drawable.hearts);
        }
    }

}