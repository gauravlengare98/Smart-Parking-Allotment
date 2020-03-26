package tired.coder.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.mapbox.mapboxsdk.Mapbox;


public class LocationsFragment extends Fragment {
    AutocompleteSupportFragment originFragment,destinationFragment;
    private static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoiZGNwdHJhZmZpY25tIiwiYSI6ImNrNW56NXY1eDA2czkzZW56bTQ3cmc5enUifQ.O31SW3eOw3zkYMlCS3M9dg";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity().getApplicationContext(), MAPBOX_ACCESS_TOKEN);

        View view = inflater.inflate(R.layout.locations_fragment, container, false);
        Button button = view.findViewById(R.id.direction_button);

          button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MapsActivity)getActivity()).callGetRoute();
            }
        });
        return view;
    }







    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
       // super.onSaveInstanceState(outState);
    }




}