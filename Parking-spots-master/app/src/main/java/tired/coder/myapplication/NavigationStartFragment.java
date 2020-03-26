package tired.coder.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NavigationStartFragment extends Fragment {
    private FloatingActionButton startNavigation;
    private AutocompleteSupportFragment destinationFragment;
    private AutocompleteSupportFragment originFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_locations,container,false);

        startNavigation  = view.findViewById(R.id.startnavigation);
        startNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MapsActivity)getActivity()).startNavigation();
            }
        });
        return view;
    }
}
