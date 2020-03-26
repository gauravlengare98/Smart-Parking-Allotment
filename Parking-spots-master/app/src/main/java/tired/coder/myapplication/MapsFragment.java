package tired.coder.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;

import java.util.Arrays;
//TODO Change Danger Zone icons
//TO    DO only 1 destination marker at a time

public class MapsFragment extends Fragment {

    private static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoiZGNwdHJhZmZpY25tIiwiYSI6ImNrNW56NXY1eDA2czkzZW56bTQ3cmc5enUifQ.O31SW3eOw3zkYMlCS3M9dg";
    String apiKey="AIzaSyBYzddcxlLVx9btx-FYOVzjgcirRdolKJI";
    private DirectionsRoute currentRoute;
    LocationComponent locationComponent;
    LatLng latLng;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity().getApplicationContext(),MAPBOX_ACCESS_TOKEN);

       View view = inflater.inflate(R.layout.search_fragment,container,false);

        Places.initialize(getActivity().getApplicationContext(), apiKey);


        Log.i("ldasd","adad");
        final AutocompleteSupportFragment autocompleteFragment= (AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("IN");
        autocompleteFragment.setHint("Search The Map");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                ((MapsActivity)getActivity()).placeSelected(place);

            }

            @Override
            public void onError(@NonNull Status status) {

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



    }
