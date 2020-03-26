package tired.coder.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.razorpay.Checkout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tired.coder.myapplication.Constants;
import tired.coder.myapplication.MapsActivity;
import tired.coder.myapplication.ParkingSpotsSheet;
import tired.coder.myapplication.R;
import tired.coder.myapplication.models.ParkingSpot;

public class ParkingSpotAdapter extends RecyclerView.Adapter<ParkingSpotAdapter.MyViewHolder> {
    private final View view;
    private final Activity activity;
    private ArrayList<ParkingSpot> parkingSpots;
    int size;
    private ArrayList<String> keys ;
    private Context context;
    private  ArrayList<String> distanceList ;
    private ArrayList<String> timetakenList;
    private ArrayList<Boolean> startedList ;
    Point origin;
    public ParkingSpotAdapter(Context context, ArrayList<ParkingSpot> parkingSpots, Point origin, View view, Activity activity)
    {
        this.context=context;
        this.parkingSpots =parkingSpots;
        this.origin = origin;
        this.view= view;
        this.activity = activity;
        distanceList = new ArrayList<>();
        timetakenList = new ArrayList<>();
        startedList = new ArrayList<>();
        for(int i=0;i<parkingSpots.size();i++)
        {
            distanceList.add(null);
            timetakenList.add(null);
            startedList.add(false);
        }
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTextView,timeTakenTextView,distanceTextView;
        private ImageButton removeButton;
        ProgressBar progressBar ;
        private MaterialCardView cardView;
        MyViewHolder(View view)
        {
            super(view);



            nameTextView = view.findViewById(R.id.placeName);
            timeTakenTextView = view.findViewById(R.id.time_taken);
            distanceTextView = view.findViewById(R.id.distance);
            progressBar = view.findViewById(R.id.progressbar);
            cardView = view.findViewById(R.id.card_view);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_spot,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final ParkingSpot parkingSpot = parkingSpots.get(holder.getAdapterPosition());
        final int pos = holder.getAdapterPosition();
        holder.nameTextView.setText(parkingSpot.getName());
//       holder.distanceTextView.setText(parkingSpot.getDistance()+" KM");
        if((!startedList.get(pos) && timetakenList.get(holder.getAdapterPosition())== null) && holder.distanceTextView.getVisibility() == View.GONE && holder.progressBar.getVisibility() == View.VISIBLE)
        {
            startedList.set(pos,true);
            Point destination  = Point.fromLngLat(parkingSpot.getLng(),parkingSpot.getLat());
            NavigationRoute.builder(context).accessToken(Mapbox.getAccessToken()).origin(origin).destination(destination).build().getRoute(new Callback<DirectionsResponse>() {
                @Override
                public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                    final DirectionsRoute currentRoute = response.body().routes().get(0);

                    DecimalFormat df = new DecimalFormat("0.00");
                    df.setMaximumFractionDigits(2);
                    double distance = Double.parseDouble(df.format(currentRoute.distance()/1000));

                    final double time = Double.parseDouble(df.format(currentRoute.duration()/60));
                    timetakenList.set(pos,""+time+" minutes");
                    distanceList.set(pos,""+distance+" km");
                    holder.timeTakenTextView.setText(""+time+" minutes");
                  holder.distanceTextView.setText(""+distance+" km");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date date = new Date();
                    Log.i("Date is","abc"+formatter.format(date));
                    holder.distanceTextView.setVisibility(View.VISIBLE);
                  holder.timeTakenTextView.setVisibility(View.VISIBLE);
                  holder.cardView.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          AlertDialog.Builder builder = new AlertDialog.Builder(context);
                          Random random = new Random();
                          SharedPreferences preferences = context.getSharedPreferences(Constants.userDb,Context.MODE_PRIVATE);
                          int bookingsLeft = preferences.getInt(Constants.preferencesBookingsLeft,5);
                          builder.setTitle("Available Spots : "+bookingsLeft );
                          View viewInflated = LayoutInflater.from(context).inflate(R.layout.name_dialog_box, (ViewGroup) view, false);
                          final TextInputEditText timeEditText = viewInflated.findViewById(R.id.timeInput);
                          Date currentTime = Calendar.getInstance().getTime();
                          Log.i("currentTimeIs",""+currentTime.getHours());
                          int currentMinutes = currentTime.getMinutes();
                          int timeTakenInMinutes = (int) time +10+ currentMinutes;
                          boolean shouldIncreaseDate  = false;
                          int hour = currentTime.getHours();
                          Log.i("houris",""+hour);
                          Log.i("hourminutesis",""+timeTakenInMinutes);
                          int estimatedHour = (hour +(int)(timeTakenInMinutes/60))%24;
                          if(hour== 23 &&  estimatedHour == 0)
                              shouldIncreaseDate = true;
                           Log.i("estimatedHour",""+estimatedHour);
                           Log.i("estimatedMinute",""+timeTakenInMinutes%60);
                           int estimatedMinutes = timeTakenInMinutes%60;
                           String estimatedHourInString,estimatedMinutesInString ;
                           if(estimatedHour<10)
                               estimatedHourInString ="0"+estimatedHour;
                           else
                               estimatedHourInString=""+estimatedHour;
                           if(estimatedMinutes<10)
                               estimatedMinutesInString="0"+estimatedMinutes;
                           else
                               estimatedMinutesInString =""+estimatedMinutes;
                           final String estimatedTime =estimatedHourInString+":"+estimatedMinutesInString;
                          timeEditText.setText(estimatedTime);

                          builder.setView(viewInflated);builder.setPositiveButton("Book Now", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                 // dialog.dismiss();

                                  // Toast.makeText(getContext(),placeName,Toast.LENGTH_SHORT).show();
                                  ((MapsActivity)activity).startPayment(parkingSpots.get(holder.getAdapterPosition()),estimatedTime);
                                  //checkout.open(activity,options);

                              }
                          });
                          builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  dialog.cancel();
                              }
                          });

                          builder.show();

                      }
                  });
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return parkingSpots.size();
    }
}
