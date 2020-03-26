package tired.coder.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tired.coder.myapplication.Constants;
import tired.coder.myapplication.MapsActivity;
import tired.coder.myapplication.R;

public class UpdateProfile extends Fragment {
    ArrayList<String> vehicleTypes = new ArrayList<>();
    TextInputEditText usernameEditText, nameEditText, emailEditText, vehicleEditText, aadharEditText, passwordEditText;
    ArrayAdapter<String> dataAdapter;
    MaterialButton updateProfileButton ;
    private String type="4 Wheeler";
    RadioButton twoWheeler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.update_profile, container, false);
        initViews(view);
        return view;

    }

    void initViews(View view) {
        usernameEditText = view.findViewById(R.id.usernameEditText);
        nameEditText = view.findViewById(R.id.nameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        vehicleEditText = view.findViewById(R.id.vehicleEditText);
        aadharEditText = view.findViewById(R.id.aadharEditText);
        twoWheeler = view.findViewById(R.id.twowheeler);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        vehicleTypes.add("2 Wheeler");
        vehicleTypes.add("4 Wheeler");
        updateProfileButton  = view.findViewById(R.id.update);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        // attaching data adapter to spinner

        SharedPreferences preferences = getContext().getSharedPreferences(Constants.userDb, Context.MODE_PRIVATE);
        String username = preferences.getString(Constants.preferencesUsername, "abcd");
        getUserDetails(username);


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
                            String name, email, password, adhar, vno, username, vtype;
                            name = jsonObject.getString("name");
                            email = jsonObject.getString("email");
                            password = jsonObject.getString("password");
                            adhar = jsonObject.getString("adhar");
                            vno = jsonObject.getString("vno");
                            username = jsonObject.getString("username");
                            vtype = jsonObject.getString("vtype");
                            if(vtype.equals("2 Wheeler"))
                                twoWheeler.setChecked(true);
                            usernameEditText.setText(username);
                            nameEditText.setText(name);
                            emailEditText.setText(email);
                            passwordEditText.setText(password);
                            aadharEditText.setText(adhar);
                            vehicleEditText.setText(vno);


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
    void updateProfile(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest updateProfile = new StringRequest(Request.Method.POST, Constants.updateProfileUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                          if(response.contains("success"))
                          {
                              Toast.makeText(getContext(),"Successful",Toast.LENGTH_SHORT).show();
                          }


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
                params.put("username", usernameEditText.getText().toString());
                params.put("password",passwordEditText.getText().toString());
                params.put("adhar",aadharEditText.getText().toString());
                params.put("vno",vehicleEditText.getText().toString());
                params.put("vtype",type);
                params.put("name",nameEditText.getText().toString());
                params.put("email",emailEditText.getText().toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };
        queue.add(updateProfile);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.fourwheeler:
                if (checked)
                // Pirates are the best
                {
                    type = "4 Wheeler";
                }
                break;
            case R.id.twowheeler:
                if (checked)
                    // Ninjas rule
                    type = "2 Wheeler";
                break;
        }
    }
}
