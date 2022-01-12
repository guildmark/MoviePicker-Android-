package se.umu.maka0437.ou3;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Movie {

    public Movie() {
        title = "";
        description = "";
        country = "";
        releaseYear = 0;
        runTime = 0;
    }

    public Movie(String name, int year, int time, String genre) {
        title = name;
        releaseYear = year;
        this.runTime = time;
        this.genre = genre;
        /*
        description = desc;
        country = land;
        */
    }


    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "releaseYear")
    public int releaseYear;

    @ColumnInfo(name = "runtime")
    public int runTime;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "country")
    public String country;

    @ColumnInfo(name = "genre")
    public String genre;

}
