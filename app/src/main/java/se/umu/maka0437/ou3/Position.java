package se.umu.maka0437.ou3;

//The usage of position in this program should be with low accuracy and low power
public class Position {

    private double latitude, longitude;

    public Position(double lat, double longi) {
        this.latitude = lat;
        this.longitude = longi;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
