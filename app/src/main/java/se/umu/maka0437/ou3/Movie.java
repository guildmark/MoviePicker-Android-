package se.umu.maka0437.ou3;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Movie implements Parcelable {

    public Movie() {
        title = "The Seventh Seal";
        description = "A knight returning to Sweden after the Crusades seeks answers about life, " +
                "death, and the existence of God as he plays chess against the Grim Reaper during the Black Plague.";
        country = "Sweden";
        genre = "Drama";
        releaseYear = 1957;
        runTime = 96;
    }

    public Movie(String name, int year, int time, String genre, String description, String imdbID) {
        this.title = name;
        this.releaseYear = year;
        this.runTime = time;
        this.genre = genre;
        this.description = description;
        this.imdbID = imdbID;
    }


    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "imdbID")
    public String imdbID;

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


    protected Movie(Parcel in) {
        uid = in.readInt();
        title = in.readString();
        releaseYear = in.readInt();
        runTime = in.readInt();
        description = in.readString();
        country = in.readString();
        genre = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(title);
        dest.writeInt(releaseYear);
        dest.writeInt(runTime);
        dest.writeString(description);
        dest.writeString(country);
        dest.writeString(genre);
    }
}
