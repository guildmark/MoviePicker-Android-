package se.umu.maka0437.ou3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    Button importListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        importListButton = findViewById(R.id.importListButton);
        importListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    importList("WATCHLIST");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Function to import list from a CSV file to database
    private void importList(String fileName) throws IOException {

        /*
        InputStream inStream = getResources().openRawResource(R.raw.watchlist);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, Charset.forName("UTF-8")));
        String line = "";
        */
        String csvFileString = this.getApplicationInfo().dataDir + File.separatorChar
                + "WATCHLIST.csv";

        //String textfile = convertStreamToString(is);

        try {

            InputStreamReader is = new InputStreamReader(getAssets().open("WATCHLIST.csv"));
            CSVReader csvReader = new CSVReader(is);
            String[] line;
            ArrayList<String[]> movieList = new ArrayList<String[]>();

            csvReader.readNext();

            while((line = csvReader.readNext()) != null) {
                //Add the CSV elements to the database
                movieList.add(line);

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }

    }
}