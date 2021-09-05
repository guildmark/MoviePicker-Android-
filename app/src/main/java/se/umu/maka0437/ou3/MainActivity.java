package se.umu.maka0437.ou3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonIOException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    public static final String MOVIE_KEY = "MOVIE_KEY";
    TextView welcomeText;
    TextView movieText;
    Movie currentMovie;
    ImageView startImage;
    AppDatabase db;
    public LiveData<List<Movie>> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);

        welcomeText = findViewById(R.id.welcomeText);
        movieText = findViewById(R.id.movieText);
        startImage = findViewById(R.id.startImage);

        Button movieButton = findViewById(R.id.findMovieBytton);
        movieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Find a specific movie
                //getMovie();
                List<Movie> currentMovies = db.movieDao().getAll();
                int random = ThreadLocalRandom.current().nextInt(0,3);
                movieText.setText(currentMovies.get(random).title);
                //Check to see if database is working
            }
        });

        //Create database if not previously done
        if(db == null) {
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "movieDB")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() //temporary until fix?
                    .build();
        }

        /*
        Movie testMovie1 = new Movie("Blade Runner", 1982);
        Movie testMovie2 = new Movie("Ben Hur", 1959);
        Movie testMovie3 = new Movie("The Snake Pit", 1949);

        insertMovie(testMovie1);
        insertMovie(testMovie2);
        insertMovie(testMovie3);
        */
        //Movie testMovie1 = new Movie("Blade Runner", 1982);
        db.movieDao().deleteAll("Blade Runner");
        //Movie[] testGet = getMovie("Blade Runner");


       // db.movieDao().insertMovie(testMovie);

        //Movie temp = db.movieDao().findByTitle("Blade Runner");
        //movieText.setText(temp.title);



        //If there is no movie, create a new one
        if(currentMovie == null) {
            currentMovie = new Movie();
        }
        else {
            //Restore movie
            //currentMovie = savedInstanceState.getParcelable(MOVIE_KEY);
        }

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
        //saveInstanceState.putParcelable(MOVIE_KEY, currentMovie);
    }

    private String getImdbID() {

        int random = ThreadLocalRandom.current().nextInt(1000000, 1999999);
        String ID = "tt" + random;
        return ID;
    }

    private void getMovie() {

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
    }



}