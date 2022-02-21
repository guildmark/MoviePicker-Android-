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

public class FilterActivity extends BaseActivity {

    public static final String EXTRA_YEAR = "se.umu.maka0437.ou3.year";
    public static final String EXTRA_GENRE = "se.umu.maka0437.ou3.genre";
    public static final String EXTRA_COUNTRY = "se.umu.maka0437.ou3.country";

    public static String FILTER_COUNTRY = "FILTER_COUNTRY";
    public static String FILTER_GENRE = "FILTER_GENRE";
    public static String FILTER_YEAR = "FILTER_YEAR";

    SeekBar yearSlider;
    Spinner genreSpinner, countrySpinner;
    Button acceptButton;
    String genre, country;
    TextView showYear;
    Boolean filterEnabled;
    int year;
    private static final String[] genreOptions = {"Drama", "SciFi", "Comedy", "Action"};



    /*
    remember implements AdapterView.OnItemSelectedListener

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
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //Use a slider for choosing year

        yearSlider = findViewById(R.id.seekBar);
        acceptButton = findViewById(R.id.filterAcceptButton);
        showYear = findViewById(R.id.yearInNumber);

        showYear.setText(String.valueOf(yearSlider.getProgress()));
        year = yearSlider.getProgress();

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
                returnResult(year, genre, country);
            }
        });

        //Use SharedPreferences for settings
    }

    //Function to return result to previous activity, getting total and what score its assigned to
    private void returnResult(int year, String genre, String country) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_YEAR, year);
        intent.putExtra(EXTRA_GENRE, genre);
        intent.putExtra(EXTRA_COUNTRY, country);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setSpinners() {

        genreSpinner = findViewById(R.id.genreSpinner);
        //Define function as an onclick for button?
        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);
                //Toast.makeText(FilterActivity.this, item.toString(), Toast.LENGTH_SHORT).show();
                genre = item.toString();
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
    }

}