package tired.coder.myapplication.models;

public class Booking {
    String placeName;
    String userId;
    String bookingId;
    double latitude;
    double longitude;
    boolean verified;
    int cost;
    String time ;
    String date;
    String otp;
    String vno;
    String id;


    public String getVno() {
        return vno;
    }

    public void setVno(String vno) {
        this.vno = vno;
    }

    public Booking(String placeName, String userId, double latitude, double longitude, boolean verified, int cost, String time, String date, String otp, String vno) {
        this.placeName = placeName;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.verified = verified;
        this.cost = cost;
        this.time = time;
        this.date = date;

        this.vno = vno;
        this.otp = otp;
    }


    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
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

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
