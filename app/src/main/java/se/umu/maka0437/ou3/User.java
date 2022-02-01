package se.umu.maka0437.ou3;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public User(String username, String password){}

    private String username;
    private String password;
    private String hashedPass;

    protected User(Parcel in) {
        username = in.readString();
        password = in.readString();
        hashedPass = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public String getHashedPass() {
        return hashedPass;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(hashedPass);
    }
}
