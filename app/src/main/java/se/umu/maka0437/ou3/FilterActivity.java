package se.umu.maka0437.ou3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class FilterActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //Use a slider for choosing year

        yearSlider = findViewById(R.id.seekBar);
        acceptButton = findViewById(R.id.filterAcceptButton);
        showYear = findViewById(R.id.yearInNumber);
        genreSpinner = findViewById(R.id.genreSpinner);
        countrySpinner = findViewById(R.id.countrySpinner);

        //Set adapters for genre and country spinners
        ArrayAdapter<CharSequence> genreArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.genres, android.R.layout.simple_spinner_dropdown_item);

        genreArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> countryArrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.countries, android.R.layout.simple_spinner_dropdown_item);

        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genreSpinner.setAdapter(genreArrayAdapter);
        countrySpinner.setAdapter(countryArrayAdapter);

        //Add saveInstance State for rotations etc

        //Add current progress on the year slider
        showYear.setText(String.valueOf(yearSlider.getProgress()));

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

}