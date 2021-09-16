package se.umu.maka0437.ou3;

import androidx.appcompat.app.AppCompatActivity;

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
import java.nio.charset.Charset;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    Button importList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        importList = findViewById(R.id.importListButton);
        importList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importList("WATCHLIST");
            }
        });
    }

    //Function to import list from a CSV file to database
    private void importList(String fileName) {

        /*
        InputStream inStream = getResources().openRawResource(R.raw.watchlist);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, Charset.forName("UTF-8")));
        String line = "";
        */
        String csvFileString = this.getApplicationInfo().dataDir + File.separatorChar
                + "WATCHLIST.csv";

                //Environment.getExternalStorageDirectory() + "/csvfile.csv"
        try{
            //Get the correct CSV file to import


            String filePath = "android.resource://" + getPackageName() + "/" + R.raw.watchlist;
            File listFile = new File("watchlist.csv");
            String fileAbsPath = listFile.getAbsolutePath();
            String fileCanonicalPath = listFile.getCanonicalPath();

            CSVReader csvReader = new CSVReader(new FileReader(listFile));

            String[] nextLine;
            //List<String[]> allData = csvReader.readAll();

            //While we keep reading from the file
            while((nextLine = csvReader.readNext()) != null) {
                System.out.println(nextLine[0]);
                System.out.println(nextLine[1]);
                System.out.println(nextLine[2]);

                //Add the data to local database
            }


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }

    }
}