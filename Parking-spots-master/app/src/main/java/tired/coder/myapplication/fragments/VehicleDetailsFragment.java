package tired.coder.myapplication.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tired.coder.myapplication.Constants;
import tired.coder.myapplication.R;

public class VehicleDetailsFragment extends BottomSheetDialogFragment {
    TextView vehicleNumberTextView,vehicleTypeTextView;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         View view = inflater.inflate(R.layout.vehicle_sheet,container,false);
         vehicleNumberTextView = view.findViewById(R.id.vehicleNumberDetail);
         vehicleTypeTextView= view.findViewById(R.id.vehicleTypeDetail);
        SharedPreferences preferences =getContext().getSharedPreferences(Constants.userDb, Context.MODE_PRIVATE);
         username = preferences.getString(Constants.preferencesUsername,"abcd");
         getUserDetails(username);
         return view;

    }
    private void getUserDetails(final String username) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest getDetails = new StringRequest(Request.Method.POST, Constants.profileDetailsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String  vno, vtype;

                            vno = jsonObject.getString("vno");
                            vtype = jsonObject.getString("vtype");
                            vehicleNumberTextView.setText("Vehicle Number : "+ vno);
                            vehicleTypeTextView.setText("Vehicle Type :"+ vtype);



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
        queue.add(getDetails);

    }
}
