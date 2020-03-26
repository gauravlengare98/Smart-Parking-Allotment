package tired.coder.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tired.coder.myapplication.adapters.AdminBookingAdapter;
import tired.coder.myapplication.adapters.BookingsAdapter;
import tired.coder.myapplication.models.Booking;

public class ManagerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_parking_spots);
        final RecyclerView recyclerView =findViewById(R.id.parking_spots_recycler_view);
        // ParkingSpotAdapter parkingSpotAdapter = new ParkingSpotAdapter(getContext(),parkingSpots,origin,getView(),getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        //  recyclerView.setAdapter(parkingSpotAdapter);
        RequestQueue queue = Volley.newRequestQueue(this);
        final Activity managerActivity = ManagerActivity.this;
        StringRequest getBookingDetails = new StringRequest(Request.Method.POST, Constants.showAllBookingsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<Booking> bookings = new ArrayList<>();
                            for(int i =0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject =  jsonArray.getJSONObject(i);
                                boolean verified =  jsonObject.getBoolean("verified");
                                String longitude = jsonObject.getString("longitude");
                                String latitude = jsonObject.getString("latitude");
                                String placeName = jsonObject.getString("placeName");
                                String time = jsonObject.getString("time");
                                String cost = jsonObject.getString("cost");
                                String otp = jsonObject.getString("otp");
                                String id = jsonObject.getString("_id");
                                String date = jsonObject.getString("date");
                                String vno = jsonObject.getString("vno");
                                String username = jsonObject.getString("bookingUsername");
                                Booking booking = new Booking(placeName,username,Double.parseDouble(latitude),Double.parseDouble(longitude),verified, Integer.parseInt(cost),time,date,otp,vno);
                                booking.setBookingId(id);
                                bookings.add(booking);

                            }
                            AdminBookingAdapter bookingsAdapter = new AdminBookingAdapter(getApplicationContext(),bookings,managerActivity);
                            recyclerView.setAdapter(bookingsAdapter);



                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(getApplicationContext(), "Backend Not Working", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                //params.put("username", username);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };
        queue.add(getBookingDetails);
    }
}
