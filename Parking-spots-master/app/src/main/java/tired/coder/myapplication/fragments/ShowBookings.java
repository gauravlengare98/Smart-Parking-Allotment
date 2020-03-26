package tired.coder.myapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mapbox.geojson.Point;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tired.coder.myapplication.Constants;
import tired.coder.myapplication.R;
import tired.coder.myapplication.adapters.BookingsAdapter;
import tired.coder.myapplication.adapters.ParkingSpotAdapter;
import tired.coder.myapplication.models.Booking;
import tired.coder.myapplication.models.ParkingSpot;
public class ShowBookings extends BottomSheetDialogFragment {


    String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.show_user_booking,container,false);
        final RecyclerView recyclerView = view.findViewById(R.id.booking_recyclerview);
        SharedPreferences preferences =getContext().getSharedPreferences(Constants.userDb, Context.MODE_PRIVATE);
        username = preferences.getString(Constants.preferencesUsername,"abcd");
        // ParkingSpotAdapter parkingSpotAdapter = new ParkingSpotAdapter(getContext(),parkingSpots,origin,getView(),getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
      //  recyclerView.setAdapter(parkingSpotAdapter);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest getBookingDetails = new StringRequest(Request.Method.POST, Constants.getBookingsUrl,
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
                                String date = jsonObject.getString("date");
                                String vno = jsonObject.getString("vno");
                                Booking booking = new Booking(placeName,username,Double.parseDouble(latitude),Double.parseDouble(longitude),verified, Integer.parseInt(cost),time,date,otp,vno);
                                bookings.add(booking);

                            }
                            BookingsAdapter bookingsAdapter = new BookingsAdapter(getContext(),bookings,getActivity());
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
                        Toast.makeText(getContext(), "Backend Not Working", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                Log.i("usernameis",username);
                params.put("username", username);

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
        return  view;
    }

    @Override
    public void onStop() {
//        dismiss();

        super.onStop();

    }

    @Override
    public void onDestroy() {
        //       dismiss();

        super.onDestroy();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.

    }
}
