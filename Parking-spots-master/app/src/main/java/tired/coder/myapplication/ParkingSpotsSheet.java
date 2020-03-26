package tired.coder.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mapbox.geojson.Point;

import java.util.ArrayList;

import tired.coder.myapplication.adapters.ParkingSpotAdapter;
import tired.coder.myapplication.models.ParkingSpot;

public class ParkingSpotsSheet extends BottomSheetDialogFragment {
    ArrayList<ParkingSpot> parkingSpots;
    Point origin;
    public ParkingSpotsSheet(ArrayList<ParkingSpot> parkingSpots,Point origin) {
        this.parkingSpots = parkingSpots;
        this.origin = origin;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.parkingspots,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.parking_spots_recycler_view);
        recyclerView.setItemViewCacheSize(parkingSpots.size());
        ParkingSpotAdapter parkingSpotAdapter = new ParkingSpotAdapter(getContext(),parkingSpots,origin,getView(),getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(parkingSpotAdapter);
        TextView nobooking = view.findViewById(R.id.noparking);
        if(parkingSpots.size()==0)
            nobooking.setVisibility(View.VISIBLE);
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
