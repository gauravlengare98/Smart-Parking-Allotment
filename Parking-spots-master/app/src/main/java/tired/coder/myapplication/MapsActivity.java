package tired.coder.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tired.coder.myapplication.fragments.ShowBookings;
import tired.coder.myapplication.fragments.UpdateProfile;
import tired.coder.myapplication.fragments.VehicleDetailsFragment;
import tired.coder.myapplication.models.Booking;
import tired.coder.myapplication.models.ParkingSpot;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, LocationEngineCallback<LocationEngineResult>, GoogleApiClient.ConnectionCallbacks, PaymentResultListener,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoiYmFsdmluZGVyeiIsImEiOiJjazRtOWduYzIyN2k5M2txd2JqdW1xdmdvIn0.eU3RhQaBleLHSZaVK3lVBw";
    private static final String TAG = "TAG";
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    ShowBookings showBookings;

    LocationManager locationManager;
    private LocationComponent locationComponent;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Point destination;
    boolean shouldCallSettingsRequest = false;
    boolean fromBack = false;
    ParkingSpot receivedParkingSpot= null ;
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 555;
    int ACCESS_FINE_LOCATION_CODE = 3310;
    Place selectedPlace;
    private GoogleApiClient mGoogleApiClient;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;
    private Point origin;
    Style mapStyle;
    boolean shouldCallGetRoute = false;
    String destinationName;
    ActionBar toolbar;
    private FragmentEnum currentFragment;
    String receivedTime  ;
    private RequestQueue queue;
    int currentPosition  = 0;
    private ParkingSpotsSheet parkingSpotsSheet;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    Toolbar myToolBar;
    private String vno;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, MAPBOX_ACCESS_TOKEN);

        setContentView(R.layout.maps_layout);
        myToolBar = findViewById(R.id.toolbar);

        setSupportActionBar(myToolBar);
        myToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        drawer = (DrawerLayout) findViewById(R.id.root);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, myToolBar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        buildGoogleApiClient();
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        toolbar = getSupportActionBar() ;
        currentFragment = FragmentEnum.MapsFragment;
        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }
        MapsFragment mapsFragment = new MapsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.myfragment, mapsFragment);
        fragmentTransaction.commit();

    }






    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // Google Api Client is connectedg
    @Override
    public void onConnected(Bundle bundle) {
        if (mGoogleApiClient.isConnected()) {

            settingsrequest();
        }
    }
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        /**
         * Add your logic here for a successful payment response
         */
       // String otp = "";
        ///Booking booking =new Boo
        try {

               // int bookingsLeft = s
//            //TODO USe sharedpreferences
            SharedPreferences preferences = getSharedPreferences(Constants.userDb,Context.MODE_PRIVATE);
            final String username = preferences.getString(Constants.preferencesUsername,"balvinderz");
            int bookingsLeft = preferences.getInt(Constants.preferencesBookingsLeft,5);
            bookingsLeft--;
            if(bookingsLeft==0)
                bookingsLeft=5;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(Constants.preferencesBookingsLeft,bookingsLeft);
            editor.commit();
            Log.i("ResponseUsername",preferences.getString(Constants.preferencesUsername,"abcd"));
         //   String password = preferences.getString(Constants.preferencesPassword,"123");
            StringRequest getUserDetails = new StringRequest(Request.Method.POST, Constants.profileDetailsUrl,
                    new com.android.volley.Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.i("Response aaya",response);

                            try {
                                JSONObject jsonObject  = new JSONObject(response);
                                vno = jsonObject.getString("vno");
                                final  String otp = ""+((int)(Math.random()*9000)+1000);
                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
                                final String formattedDate = df.format(c);
                                final Booking booking = new Booking(receivedParkingSpot.getName(), username, receivedParkingSpot.getLat(), receivedParkingSpot.getLng(), false, 10, receivedTime, formattedDate, otp, vno);
                                StringRequest addBooking = new StringRequest(Request.Method.POST, Constants.addBookingUrl,
                                        new com.android.volley.Response.Listener<String>()
                                        {
                                            @Override
                                            public void onResponse(String response) {
                                                // response
                                                try {
                                                    Log.i("Response Aaya",response);

                                                 //   Date c = Calendar.getInstance().getTime();
                                                  //  SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
                                                 ////   String formattedDate = df.format(c);
                                                //    Booking booking = new Booking(receivedParkingSpot.getName(), username, receivedParkingSpot.getLat(), receivedParkingSpot.getLng(), false, 10, receivedTime, formattedDate, otp,vno);

                                                } catch(Exception e)
                                                {
                                                    Toast.makeText(getApplicationContext(),"Backend not working",Toast.LENGTH_SHORT).show();

                                                }


                                            }
                                        },
                                        new com.android.volley.Response.ErrorListener()
                                        {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // error
                                                Log.d("Error.Response", error.toString());
                                                Toast.makeText(getApplicationContext(),"Backend Not Working",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                ) {
                                    @Override
                                    protected Map<String, String> getParams()
                                    {
                                        Map<String, String>  params = new HashMap<String, String>();
                                        params.put("username",username);
                                        params.put("latitude",""+receivedParkingSpot.getLat());
                                        params.put("longitude",""+receivedParkingSpot.getLng());
                                        params.put("placename",receivedParkingSpot.getName());
                                        params.put("time",receivedTime);

                                        params.put("cost","10");
                                        params.put("date",formattedDate); // TODO  koli ko bolna hai
                                        params.put("otp",otp);
                                        params.put("vno",booking.getVno()); // TODO koli ko bolna hai
                                        //params.put("password",passwordEditText.getText().toString());

                                        return params;
                                    }
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String,String> params = new HashMap<String, String>();
                                        params.put("Content-Type","application/x-www-form-urlencoded");
                                        return params;
                                    }

                                };
                                queue.add(addBooking);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }catch(Exception e)
                            {
                                Toast.makeText(getApplicationContext(),"Invalid Username or password",Toast.LENGTH_SHORT).show();

                            }


                        }
                    },
                    new com.android.volley.Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                            Toast.makeText(getApplicationContext(),"Backend Not Working",Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("username",username);
                    //params.put("password",passwordEditText.getText().toString());

                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }

            };
            queue.add(getUserDetails);
//            Log.i("Erroraaya", razorpayPaymentID);
//            //TODO Send Volley Request

            fragmentManager.executePendingTransactions();

            LocationsFragment locationsFragment = new LocationsFragment();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.myfragment, locationsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
            currentFragment = FragmentEnum.LocationsFragment;
            parkingSpotsSheet.dismiss();
            fragmentTransaction.remove(parkingSpotsSheet);
        }catch (Exception e)
        {
            Log.e("erroraaya",e.getMessage());
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        Log.i("Erroraaya",response);
        /**
         * Add your logic here for a failed payment response
         */
    }
    public void settingsrequest() {
        if (!PermissionsManager.areLocationPermissionsGranted(this)) {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if (mGoogleApiClient.isConnected()) {

                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_CODE);
                                } else {
                                    // get Location
                                    getLocation();
                                }
                            } else {
                                // get Location
                                getLocation();
                            }

                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {

                            status.startResolutionForResult(MapsActivity.this, REQUEST_RESOLVE_ERROR);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                            Toast.makeText(getApplicationContext(), "Enable Location to Continue", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;
                }
            }
        });
    }


    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.TRAFFIC_DAY, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

// Map is set up and the style has loaded. Now you can add data or make other map adjustments.
                mapStyle = style;
               // addDestinationIconSymbolLayer(style);

                if (isLocationEnabled(getApplicationContext()))
                    enableLocationComponent(style);
               // addDangerMarkers();
            }
        });
    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Get an instance of the component
            locationComponent = mapboxMap.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);
// Set the component's camera mode
            if (shouldCallGetRoute) {
                shouldCallGetRoute = false;
                callGetRoute();
                return;
            }

            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//      //  super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }

    @Override
    public void onSuccess(LocationEngineResult result) {

        if (result.getLastLocation() == null)
            return;
        Log.i("locationresult", "soja" + result.getLastLocation().getLatitude());
        mapboxMap.setCameraPosition(new CameraPosition.Builder().zoom(9).target(new com.mapbox.mapboxsdk.geometry.LatLng(result.getLastLocation().getLatitude(), result.getLastLocation().getLongitude())).build());
    }

    @Override
    public void onFailure(@NonNull Exception exception) {
        Log.i("locationresult", "failed");

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        // Toast.makeText(this, "Explanation", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);

                }
            });
        } else {
            // Toast.makeText(this, "Not Granted", Toast.LENGTH_LONG).show();
            Log.i("something's_wrong", "icanfeelit");
            //finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 3310) {
            if (grantResults.length > 0) {

                for (int i = 0, len = permissions.length; i < len; i++) {

                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        // Show the user a dialog why you need location
                    } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        // get Location
                        settingsrequest();
                        //settingsrequest();
                        if (shouldCallGetRoute) {
                            callGetRoute();
                            shouldCallGetRoute = false;
                        }
                    } else {
                        this.finish();
                    }
                }
            }
            if (grantResults.length > 0)
                settingsrequest();
            return;
        }
        if (grantResults.length > 0)
            settingsrequest();
    }
    public void showPlace( Point selectedDestination,ParkingSpot parkingSpot)
    {
       // selectedPlace = place;
        destination = selectedDestination;
        receivedParkingSpot = parkingSpot;
        for(Marker marker : mapboxMap.getMarkers())
        mapboxMap.removeMarker(marker);
        showBookings.dismiss();
        callGetRoute();
    }
    public void placeSelected(Place place) {
        selectedPlace = place;
        LatLng latLng = place.getLatLng();
        destinationName = place.getName();
        destination = Point.fromLngLat(place.getLatLng().longitude, place.getLatLng().latitude);

        if (!fromBack) {

            currentFragment = FragmentEnum.LocationsFragment;

           // LocationsFragment locationsFragment = new LocationsFragment();
//            fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.myfragment, locationsFragment);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
        } else
            fromBack = false;
        //TODO Show Parking Spots
        queue = Volley.newRequestQueue(this);
         String parkingSpotsUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+place.getLatLng().latitude+","+place.getLatLng().longitude+"&radius=1000&types=parking&sensor=false&key=AIzaSyBYzddcxlLVx9btx-FYOVzjgcirRdolKJI";
        StringRequest postRequest = new StringRequest(Request.Method.GET, parkingSpotsUrl,
                new com.android.volley.Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        final ArrayList<ParkingSpot> parkingSpots =new ArrayList<>();
                        Location location = locationComponent.getLastKnownLocation();

                        origin = Point.fromLngLat(location.getLongitude(), location.getLatitude());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            final JSONArray results =jsonObject.getJSONArray("results");
                            for(int i=0;i<results.length();i++)
                            {
                                JSONObject result = results.optJSONObject(i);
                                 double lat = result.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                                 double longitude = result.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                                 String name = result.getString("name");
                                 if(name.toLowerCase().contains("parking")) {
                                     ParkingSpot parkingSpot = new ParkingSpot(name, lat, longitude);
                                     parkingSpots.add(parkingSpot);
                                 }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        parkingSpotsSheet = new ParkingSpotsSheet(parkingSpots,origin);
                        parkingSpotsSheet.show(getSupportFragmentManager(),parkingSpotsSheet.getTag());

                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(getApplicationContext(),"Backend Not Working",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(postRequest);

    }

    public void callGetRoute() {
        if (!isOnline()) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            if (!isLocationEnabled(getApplicationContext())) {
                shouldCallGetRoute = true;
                settingsrequest();
            } else {

                Location location = locationComponent.getLastKnownLocation();
                if (location == null) {
                    Toast.makeText(this, "Error getting gps location.Please try again", Toast.LENGTH_SHORT).show();

                    return;

                }
                // tried=false;
                origin = Point.fromLngLat(location.getLongitude(), location.getLatitude());

                getRoute(origin, destination);
               // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                currentFragment = FragmentEnum.NavigationStartFragment;
               NavigationStartFragment startFragment = new NavigationStartFragment();

                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.myfragment, startFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } else {
            shouldCallGetRoute = true;
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    private void getRoute(final Point originPoint, final Point destinationPoint) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(originPoint)
                .destination(destinationPoint)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

// Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }


                        navigationMapRoute.addRoute(currentRoute);
                        mapboxMap.addMarker(new MarkerOptions().position(new com.mapbox.mapboxsdk.geometry.LatLng(destinationPoint.latitude(), destinationPoint.longitude())).title("Destination"));
                        com.mapbox.mapboxsdk.geometry.LatLng originLatLng = new com.mapbox.mapboxsdk.geometry.LatLng(originPoint.latitude(), originPoint.longitude());
                        com.mapbox.mapboxsdk.geometry.LatLng destinationLatLng = new com.mapbox.mapboxsdk.geometry.LatLng(destinationPoint.latitude(), destinationPoint.longitude());

                        LatLngBounds latLngBounds = new LatLngBounds.Builder().include(originLatLng).include(destinationLatLng).build();
                        // mapboxMap.moveCamera(new CameraUpdateFactory.newLatLngBounds(latLngBounds,50));
                        double maxZoomLevel = mapboxMap.getMaxZoomLevel();
                        mapboxMap.setMaxZoomPreference(10);
                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 5), 3000);
                        mapboxMap.setMaxZoomPreference(23);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }








    @Override
    public void onConnectionSuspended(int i) {
    }


    // When there is an error connecting Google Services
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void startNavigation() {
        NavigationLauncherOptions.Builder optionsBuilder = NavigationLauncherOptions.builder()
                .shouldSimulateRoute(true);
        CameraPosition initialPosition = new CameraPosition.Builder()
                .target(new com.mapbox.mapboxsdk.geometry.LatLng(receivedParkingSpot.getLat(), receivedParkingSpot.getLng()))
                .zoom(16)
                .build();
        optionsBuilder.initialMapCameraPosition(initialPosition);
        currentFragment = FragmentEnum.NavigationFragment;
        //TODO Route

        optionsBuilder.directionsRoute(currentRoute);
        NavigationLauncher.startNavigation(this, optionsBuilder.build());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.editProfile : showUpdateProfile();
            break;
            case R.id.logout : logOut();
            break;
            case R.id.displayVehicle : displayVehicleSheet();
            break;
            case R.id.showBookings : showBookings();
        }
        drawer.closeDrawers();
        return true;
    }

    private void displayVehicleSheet() {
        VehicleDetailsFragment vehicleDetailsFragment = new VehicleDetailsFragment();
        vehicleDetailsFragment.show(getSupportFragmentManager(),vehicleDetailsFragment.getTag());
    }

    private void showBookings() {
        showBookings = new ShowBookings();
        showBookings.show(getSupportFragmentManager(),showBookings.getTag());

    }

    private void logOut() {
        SharedPreferences preferences = getSharedPreferences(Constants.userDb,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(Constants.preferencesUsername);
        editor.remove(Constants.preferencesPassword);
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showUpdateProfile() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        UpdateProfile updateProfile = new UpdateProfile();
        transaction.replace(R.id.myfragment,updateProfile,null);
        transaction.addToBackStack(null);
        myToolBar.setVisibility(View.GONE);
        currentFragment = FragmentEnum.UpdateProfileFragment;
        transaction.commit();

    }

    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MapsActivity) getActivity()).onDialogDismissed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // get location method
                    getLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getApplicationContext(), "Enable Location to continue", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public void getLocation() {
        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(this);
        locationEngine.getLastLocation(this);
        if (mapStyle != null)
            enableLocationComponent(mapStyle);

    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      switch (currentFragment) {
            case NavigationFragment:
               currentFragment = FragmentEnum.NavigationStartFragment;
                parkingSpotsSheet.dismiss();
                break;
          case  UpdateProfileFragment: myToolBar.setVisibility(View.VISIBLE);
          break;
//                break;
//            case NavigationStartFragment:
//                currentFragment = FragmentEnum.LocationsFragment;
//                navigationMapRoute.removeRoute();
//                currentFragment = FragmentEnum.LocationsFragment;
//                fromBack = true;
//                placeSelected(selectedPlace);
//                break;
//            case LocationsFragment:
//                currentFragment = FragmentEnum.MapsFragment;
//                for (Marker marker : mapboxMap.getMarkers())
//                    mapboxMap.removeMarker(marker);
//                addDangerMarkers();
//                enableLocationComponent(mapStyle);
//
      }

    }
    public void startPayment(ParkingSpot parkingSpot,String time)
    {
        parkingSpotsSheet.dismiss();
        receivedParkingSpot = parkingSpot;
        receivedTime  =time;
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", "100");

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");
            options.put("prefill", preFill);


            Checkout.preload(getApplicationContext());
            Checkout checkout = new Checkout();
            //checkout.setKeyID("rzp_test_NWARnUURelnfMR");
          // checkout.open(MapsActivity.this,options);
            onPaymentSuccess("xyz");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
