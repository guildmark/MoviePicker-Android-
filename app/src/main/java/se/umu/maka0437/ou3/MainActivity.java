package se.umu.maka0437.ou3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends ToolbarActivity {

    public static final String MOVIE_KEY = "MOVIE_KEY";
    public static final String GENRE_KEY = "GENRE_KEY";

    ArrayList<String[]> movieList = new ArrayList<String[]>();
    TextView welcomeText, movieText, positionText, genreText;
    Movie currentMovie;
    ImageView startImage;
    AppDatabase db;
    Position currentPos;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);

        //welcomeText = findViewById(R.id.welcomeText);
        movieText = findViewById(R.id.movieText);
        startImage = findViewById(R.id.startImage);
        genreText = findViewById(R.id.genreText);

        //Get back state
        if(savedInstanceState != null) {
            currentMovie = savedInstanceState.getParcelable(MOVIE_KEY);
            //Set correct texts
            movieText.setText(currentMovie.title + " (" + currentMovie.releaseYear + ")");
            genreText.setText(currentMovie.genre);
        }

        Button movieButton = findViewById(R.id.findMovieButton);
        movieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Find a specific movie
                getMovieDB();
                /*
                List<Movie> currentMovies = db.movieDao().getAll();
                int random = ThreadLocalRandom.current().nextInt(0,3);
                movieText.setText(currentMovies.get(random).title);
                */
                //Check to see if database is working

            }
        });

        //Import CSV file and add movies to database
        Button csvButton = findViewById(R.id.CSVbutton);
        csvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    importCSV("WATCHLIST.csv");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //positionText = findViewById(R.id.posText);

        /*
        Button getPositionButton = findViewById(R.id.posButton);
        getPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get position for viewer and display
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //Check for the permission
                    if(getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                        //Get location
                        fusedLocationProviderClient.getLastLocation()
                                .addOnSuccessListener(new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if(location != null) {
                                            double latitude = location.getLatitude();
                                            double longitude = location.getLongitude();

                                            currentPos.setLatitude(latitude);
                                            currentPos.setLongitude(longitude);

                                            positionText.setText("POSITION: " + latitude + " " + longitude);
                                        }
                                    }
                                });
                    }
                }
                else {
                    //If there is no permission, ask the user for it
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            }
        });
        */


        //Create database if not previously done
        if(db == null) {
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "movieDB")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() //temporary until fix?
                    .build();
        }

        //If there is no movie, create a new one
        if(currentMovie == null) {
            currentMovie = new Movie();
        }
        else {
            //Restore movie
            currentMovie = savedInstanceState.getParcelable(MOVIE_KEY);
        }

        //Save condition on recreation / rotate etc


    }

    //Use when coming back from other activites
    @Override
    protected void onResume() {
        super.onResume();

        //Add imported movies to database
        Intent intent = getIntent();
        //movieList = intent.getStringArrayExtra();

    }

    //Insert movie into database
    private void insertMovie(Movie movie) {
        new Thread(() -> db.movieDao().insertMovie(movie)).start();
    }

    //Delete a movie from database
    private void deleteMovie(Movie movie) {
        new Thread(() -> db.movieDao().deleteMovie(movie)).start();
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState)  {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putParcelable(MOVIE_KEY, currentMovie);
    }

    private String getImdbID() {

        int random = ThreadLocalRandom.current().nextInt(1000000, 1999999);
        String ID = "tt" + random;
        //Add more criteria? Find good API
        return ID;
    }

    private void getMovieDB() {
        //Get a random movie from the current database and display it on screen
        /*
        Movie movie = new Movie();
        movie = db.movieDao().getRandomMovie();
        */
        currentMovie = db.movieDao().getRandomMovie();
        String description = getImdbID();

        movieText.setText(currentMovie.title + " (" + currentMovie.releaseYear + ")");
        genreText.setText(currentMovie.genre);

    }


    private void getMovieAPI() {

        //Use The Movie Database API to get a random movie (OMDbapi.com)
        String APIkey = "9297e7";

        //Get movies from the OMDb API
        //TODO only use completely random if user has no preference
        String testURL = "http://www.omdbapi.com/?apikey=" + APIkey + "&i=" + getImdbID();

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

                    movieText.setText(title + " (" + releaseYear + ")");

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
                //goToListActivity();

                //openFile();
                //Let user choose their own file to import
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void goToFilterActivity() {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }

    private void goToProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void goToListActivity() {
        //Go to list activity
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }


    //Function to import list from a CSV file to database
    private void importCSV(String fileName) throws IOException {

        String csvFileString = this.getApplicationInfo().dataDir + File.separatorChar
                + "WATCHLIST.csv";

        try {

            InputStreamReader is = new InputStreamReader(getAssets().open("WATCHLIST.csv"));
            CSVReader csvReader = new CSVReader(is);
            String[] line;

            csvReader.readNext();

            while((line = csvReader.readNext()) != null) {
                //Add the CSV elements to the database
                movieList.add(line);
                //Get relevant movie data -- Below is specifically CSV from imdb
                String description = line[4];
                String name = line[5];
                int year = Integer.parseInt(line[10]);
                int runTime = Integer.parseInt(line[9]);
                String genre = line[11];
                String directors = line[14];

                Movie movie = new Movie(name, year, runTime, genre, description);
                insertMovie(movie);
            }

            //Send parcel back to main activity with data
            //goToMainActivity();



        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }

    }

    private void addToDB(ArrayList<String[]> movieList) {

        //For each movie, add to database with same values
    }

    /*
    // Request code for selecting a PDF document.
    private static final int PICK_CSV_FILE = 2;

    private void openFile(Uri pickerInitialUri) {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");

        startActivityForResult(intent, PICK_CSV_FILE);
    }
    */

}