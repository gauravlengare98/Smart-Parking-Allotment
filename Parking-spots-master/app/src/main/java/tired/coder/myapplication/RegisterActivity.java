package tired.coder.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText usernameEditText,nameEditText,emailEditText,vehicleEditText,aadharEditText,passwordEditText;
    MaterialButton register;
    private RequestQueue queue;
    private String registerUrl="http://86721a9b.ngrok.io/auth/register";
    private TextView gotoLoginPage;
    private Spinner spinner;
    String type = "4 Wheleer";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initViews();
        queue = Volley.newRequestQueue(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final String name = nameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                final String aadhar = aadharEditText.getText().toString();
                final String vechicle= vehicleEditText.getText().toString();
                final String vehicleType = type;
                StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.registrationUrl,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", "abc"+response);
                                if(response.contains("success"))
                                {
                                    Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                                else if(response.contains("err"))
                                {
                                    Toast.makeText(getApplicationContext(),"User Already Exists",Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Some other error Exists",Toast.LENGTH_SHORT).show();

                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", error.toString());
                                Toast.makeText(getApplicationContext(),"Backend Not Working",Toast.LENGTH_SHORT).show();

                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("username", username);
                        params.put("password",password);
                        params.put("adhar",aadhar);
                        params.put("vno",vechicle);
                        params.put("vtype",vehicleType);
                        params.put("name",name);
                        params.put("email",email);
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
                queue.start();

                queue.add(postRequest);
            }

        });
        gotoLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        usernameEditText = findViewById(R.id.usernameEditText);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        vehicleEditText = findViewById(R.id.vehicleEditText);
        aadharEditText = findViewById(R.id.aadharEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        register =findViewById(R.id.register);
        gotoLoginPage = findViewById(R.id.gotologin);


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
