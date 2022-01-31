package se.umu.maka0437.ou3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FilterActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    public static String FILTER_COUNTRY = "FILTER_COUNTRY";
    public static String FILTER_GENRE = "FILTER_GENRE";
    public static String FILTER_YEAR = "FILTER_YEAR";

    SeekBar yearSlider;
    Spinner genreSpinner, countrySpinner;
    Button acceptButton;
    String genre, country;
    TextView showYear;
    int year;
    private static final String[] genreOptions = {"Drama", "SciFi", "Comedy", "Action"};

    //Functions for getting data from spinners
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Object item = parent.getItemAtPosition(pos);
        if(item != null) {
            //Toast.makeText(FilterActivity.this, item.toString(), Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(FilterActivity.this, "Selected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //Use a slider for choosing year

        yearSlider = findViewById(R.id.seekBar);
        acceptButton = findViewById(R.id.filterAcceptButton);
        showYear = findViewById(R.id.yearInNumber);

        showYear.setText(String.valueOf(yearSlider.getProgress()));

        //Set slider
        yearSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Change value of text as the slider is moved
                showYear.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                year = seekBar.getProgress();
            }
        });

        setSpinners();

        //Go back to main activity after user is done
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain();
            }
        });

        //Use SharedPreferences for settings
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);

        //Get all the saved values and go back to main function
        intent.putExtra(FILTER_COUNTRY, country);
        intent.putExtra(FILTER_YEAR, year);
        intent.putExtra(FILTER_GENRE, genre);

        startActivity(intent);
    }

    private void setSpinners() {

        genreSpinner = findViewById(R.id.genreSpinner);
        //Define function as an onclick for button?
        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);
                if(item != null) {
                    //Toast.makeText(FilterActivity.this, item.toString(), Toast.LENGTH_SHORT).show();
                    genre = item.toString();
                }
                else {
                    Toast.makeText(FilterActivity.this, "Selected",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        countrySpinner = findViewById(R.id.countrySpinner);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                country = item.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Set adapters for genre and country spinners
        ArrayAdapter<CharSequence> genreArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.genres, android.R.layout.simple_spinner_dropdown_item);

        genreArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> countryArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.countries, android.R.layout.simple_spinner_dropdown_item);

        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genreSpinner.setAdapter(genreArrayAdapter);
        countrySpinner.setAdapter(countryArrayAdapter);




        //
    }

}