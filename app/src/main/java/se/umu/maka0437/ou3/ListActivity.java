package se.umu.maka0437.ou3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    //Function to import list from a CSV file to database
    private void importList(String fileName) {

        try{
            //Get the correct CSV file to import
            File listFile = new File(Environment.getExternalStorageDirectory() + "/csvfile.csv");
            CSVReader csvReader = new CSVReader(new FileReader(listFile.getAbsolutePath()));

            String[] nextLine;
            int count = 0;
            StringBuilder columns = new StringBuilder();
            StringBuilder value = new StringBuilder();

            //While we keep reading from the file
            while((nextLine = csvReader.readNext()) != null) {
                System.out.println(nextLine[0]);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}