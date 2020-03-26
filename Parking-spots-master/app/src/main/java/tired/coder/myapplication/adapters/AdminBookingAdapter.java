package tired.coder.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.geojson.Point;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import tired.coder.myapplication.Constants;
import tired.coder.myapplication.MainActivity;
import tired.coder.myapplication.MapsActivity;
import tired.coder.myapplication.R;
import tired.coder.myapplication.models.Booking;
import tired.coder.myapplication.models.ParkingSpot;

public class AdminBookingAdapter extends RecyclerView.Adapter<AdminBookingAdapter.MyViewHolder> {
    private ArrayList<Booking> bookings;
    private Activity activity;
    private Context context;
    private View myview;
    public AdminBookingAdapter(Context context, ArrayList<Booking> bookings,Activity activity)
    {
        this.context=context;
        this.bookings =bookings;
        this.activity=activity;

    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView placeNameTextView,timeTextView,dateTextView,otpTextView,verifiedTextView;
        private MaterialCardView cardView;
        MyViewHolder(View view)
        {
            super(view);
            myview = view;


            placeNameTextView = view.findViewById(R.id.placeName);
            timeTextView = view.findViewById(R.id.time);
            dateTextView = view.findViewById(R.id.date);
            otpTextView = view.findViewById(R.id.otp);
            cardView = view.findViewById(R.id.booking_card);
            verifiedTextView = view.findViewById(R.id.verified);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_booking,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Booking booking = bookings.get(holder.getAdapterPosition());
        final int pos = holder.getAdapterPosition();
        holder.timeTextView.setText(booking.getTime());
        holder.dateTextView.setText(booking.getDate());
        holder.placeNameTextView.setText(booking.getPlaceName());
        Log.i("bookingotp","otp is"+booking.getOtp());
        //holder.otpTextView.setText(booking.getOtp());
        holder.verifiedTextView.setText(booking.isVerified()  ? "Verified" : "Not Verfied");
        holder.otpTextView.setText("Otp : "+booking.getOtp());
        if(bookings.get(holder.getAdapterPosition()).isVerified())
        {
            holder.cardView.setClickable(false);

            changeColorToGreen(holder);
        }

        else
        {

            holder.cardView.setClickable(true);
            changeColorToRed(holder);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Context context = new ContextThemeWrapper(activity, R.style.AppTheme);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    Random random = new Random();

                    builder.setTitle("Enter Otp" );
                    View viewInflated = LayoutInflater.from(activity).inflate(R.layout.verify_dialog_box, (ViewGroup) v, false);
                    final TextInputEditText otpField = viewInflated.findViewById(R.id.otpInput);
                    builder.setView(viewInflated);builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RequestQueue requestQueue  = Volley.newRequestQueue(activity);
                            if(!otpField.getText().toString().equals(booking.getOtp()))
                            {
                                Toast.makeText(activity.getApplicationContext(),"Wrong Otp",Toast.LENGTH_SHORT).show();
                                return ;
                            }
                                StringRequest confirmParking = new StringRequest(Request.Method.POST, Constants.updateBookingUrl,
                                    new Response.Listener<String>()
                                    {
                                        @Override
                                        public void onResponse(String response) {
                                            // response

                                            Log.d("Response", "abc"+response);
                                            if(response.contains("success"))
                                            {
                                                Toast.makeText(activity,"Registered Successfully",Toast.LENGTH_SHORT).show();
                                                booking.setVerified(true);
                                                holder.verifiedTextView.setText("Verified");
                                                changeColorToGreen(holder);
                                                holder.cardView.setClickable(false);
                                            }
                                            else if(response.contains("err"))
                                            {
                                                Toast.makeText(activity,"Failed",Toast.LENGTH_SHORT).show();

                                            }
                                            else
                                            {
                                                Toast.makeText(activity,"Some other error Exists",Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    },
                                    new Response.ErrorListener()
                                    {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // error
                                            Log.d("Error.Response", error.toString());
                                            Toast.makeText(activity,"Backend Not Working",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                            ) {
                                @Override
                                protected Map<String, String> getParams()
                                {
                                    Map<String, String>  params = new HashMap<String, String>();
                                    params.put("otp",booking.getOtp());
                                    params.put("booking_id",booking.getBookingId());
                                    Log.i("Response","yechala");
                                    return params;
                                }
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String,String> params = new HashMap<String, String>();
                                    params.put("Content-Type","application/x-www-form-urlencoded");
                                    return params;
                                }

                            };
                            requestQueue.add(confirmParking);
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();                }
            });
        }
    }

    private void changeColorToGreen(MyViewHolder holder) {
        holder.placeNameTextView.setTextColor(context.getResources().getColor(R.color.black));
        holder.otpTextView.setTextColor(context.getResources().getColor(R.color.black));
        holder.timeTextView.setTextColor(context.getResources().getColor(R.color.black));
        holder.verifiedTextView.setTextColor(context.getResources().getColor(R.color.black));
        holder.dateTextView.setTextColor(context.getResources().getColor(R.color.black));
        holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.lightGreen));
    }

    void changeColorToRed(MyViewHolder holder){
        holder.placeNameTextView.setTextColor(context.getResources().getColor(R.color.white));
        holder.otpTextView.setTextColor(context.getResources().getColor(R.color.white));
        holder.timeTextView.setTextColor(context.getResources().getColor(R.color.white));
        holder.verifiedTextView.setTextColor(context.getResources().getColor(R.color.white));
        holder.dateTextView.setTextColor(context.getResources().getColor(R.color.white));
        holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.red));
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }
}
