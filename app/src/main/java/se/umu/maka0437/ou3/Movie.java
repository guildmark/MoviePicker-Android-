package se.umu.maka0437.ou3;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Movie {

    @PrimaryKey
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


}
