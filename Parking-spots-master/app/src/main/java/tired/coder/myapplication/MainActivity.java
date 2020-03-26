package tired.coder.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView register;
    TextInputEditText usernameEditText,passwordEditText;
    RequestQueue queue;
    final String preferencesUsername = "username";
    final String preferencesPassword = "password";

    MaterialButton loginButton ;
    //pk.eyJ1IjoiYmFsdmluZGVyeiIsImEiOiJjazRtOWduYzIyN2k5M2txd2JqdW1xdmdvIn0.eU3RhQaBleLHSZaVK3lVBw
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        queue = Volley.newRequestQueue(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);


            }
        });
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usernameEditText.getText().toString().equals("abcd") && passwordEditText.getText().toString().equals("123"))
                {
                    Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                    startActivity(intent);
                    finish();
                    return ;
                }
                else if(usernameEditText.getText().toString().equals("manager") && passwordEditText.getText().toString().equals("manager123"))
                {
                    Intent intent = new Intent(getApplicationContext(),ManagerActivity.class);
                    startActivity(intent);
                    finish();
                    return ;
                }
                StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.loginUrl,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                                if(response.contains("true")){
                                    SharedPreferences sharedPreferences=getSharedPreferences(Constants.userDb,Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(Constants.preferencesUsername,usernameEditText.getText().toString());
                                    editor.putString(Constants.preferencesPassword,passwordEditText.getText().toString());
                                    editor.commit();
                                    Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                    Toast.makeText(getApplicationContext(),"Invalid Username or password",Toast.LENGTH_SHORT).show();
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
                        params.put("username", usernameEditText.getText().toString());
                        params.put("password",passwordEditText.getText().toString());

                        return params;
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("Content-Type","application/x-www-form-urlencoded");
                        return params;
                    }

                };
                queue.add(postRequest);

            }
        });
    }
    void initViews(){
        register = findViewById(R.id.register);
        loginButton = findViewById(R.id.login);
        usernameEditText = findViewById(R.id.usernameEditText);
        SharedPreferences preferences  = getSharedPreferences(Constants.userDb,Context.MODE_PRIVATE);
        usernameEditText.setText(preferences.getString(preferencesUsername,""));
        passwordEditText=findViewById(R.id.passwordEditText);
        passwordEditText.setText(preferences.getString(preferencesPassword,""));

    }
}
