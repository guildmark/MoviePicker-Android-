package se.umu.maka0437.ou3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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

import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    public static final String MOVIE_KEY = "MOVIE_KEY";
    TextView welcomeText;
    TextView movieText;
    Movie currentMovie;
    ImageView startImage;

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
                getMovie();
            }
        });

        //If there is no movie, create a new one
        if(currentMovie == null) {
            currentMovie = new Movie();
        }
        else {
            //Restore movie
            currentMovie = savedInstanceState.getParcelable(MOVIE_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState)  {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putParcelable(MOVIE_KEY, currentMovie);
    }

    private String getImdbID() {

        int random = ThreadLocalRandom.current().nextInt(1000000, 1999999);
        String ID = "tt" + random;
        return ID;
    }

    private void getMovie() {

        //Use The Movie Database API to get a random movie (OMDbapi.com)
        String APIkey = "9297e7";
        //Call movie APITextView welcomeText = findViewById(R.id.welcomeText);

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
                Log.e("Rest response", response.toString());

                try {

                    String title = response.getString("Title");
                    String description = response.getString("Plot");
                    String country = response.getString("Country");
                    int releaseYear = Integer.parseInt(response.getString("Year"));

                    /*
                    currentMovie.setTitle(title);
                    currentMovie.setDescription(description);
                    currentMovie.setReleaseYear(releaseYear);
                    currentMovie.setCountry(country);
                    */

                    movieText.setText(title + " (" + releaseYear + ")");

                } catch (JSONException e) {
                    //TODO ADD WARNING?
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
                //Import CSV file to SQLite database

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



}