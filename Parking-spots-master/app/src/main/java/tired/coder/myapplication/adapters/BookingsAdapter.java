package tired.coder.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.card.MaterialCardView;
import com.mapbox.geojson.Point;

import java.text.ParseException;
import java.util.ArrayList;

import tired.coder.myapplication.MainActivity;
import tired.coder.myapplication.MapsActivity;
import tired.coder.myapplication.R;
import tired.coder.myapplication.models.Booking;
import tired.coder.myapplication.models.ParkingSpot;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.MyViewHolder> {
    private ArrayList<Booking> bookings;
    private Activity activity;
    private Context context;
    public BookingsAdapter(Context context, ArrayList<Booking> bookings, Activity activity)
    {
        this.context=context;
        this.bookings =bookings;
        this.activity = activity;

    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView placeNameTextView,timeTextView,dateTextView,otpTextView,verifiedTextView;
        private MaterialCardView cardView;
        MyViewHolder(View view)
        {
            super(view);



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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking,parent,false);

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
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParkingSpot parkingSpot = new ParkingSpot(booking.getPlaceName(),booking.getLatitude(),booking.getLongitude());
                ((MapsActivity)activity).showPlace(Point.fromLngLat(booking.getLongitude(),booking.getLatitude()),parkingSpot);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }
}
