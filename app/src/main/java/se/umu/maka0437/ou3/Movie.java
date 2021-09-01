package se.umu.maka0437.ou3;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class Movie implements Parcelable {

    private String title;
    private int releaseYear;
    private String description;
    private String country;
    private ImageView cover; //How to use this?

    public Movie() {
        title = "";
        releaseYear = 0;
        description = "";
    }

    protected Movie(Parcel in) {
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
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
