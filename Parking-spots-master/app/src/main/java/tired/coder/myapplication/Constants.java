package tired.coder.myapplication;

public class Constants {
    public static String baseUrl = "http://192.168.0.105:9000/";
   public static  String loginUrl = baseUrl+"auth/login";
   public static  String registrationUrl = baseUrl+"auth/register";
   public static String profileDetailsUrl = baseUrl+"user/getProfileDetails";
   public static String getBookingsUrl = baseUrl+"user/getBooking";
   public static String addBookingUrl = baseUrl+"book/addBooking";
   public static String updateBookingUrl = baseUrl+"book/updateBooking";
   public static String updateProfileUrl = baseUrl+"/user/updateProfile"; // TODO Koli se maangni hai link

   public static final String userDb = "userDb";
   public static String showAllBookingsUrl = baseUrl+"book/showAllBooking";
    public static final String preferencesUsername = "username";
    public static final String preferencesPassword = "password";
    public static  final String preferencesBookingsLeft = "bookingsLeft";
}
