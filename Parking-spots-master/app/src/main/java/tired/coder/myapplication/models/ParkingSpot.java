package tired.coder.myapplication.models;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.Serializable;

public class ParkingSpot implements Serializable {
    String name;
    double lat,lng;


    public ParkingSpot(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
    public LatLng getLatLng(){
        return new LatLng(lat,lng);
    }
}
