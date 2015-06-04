package com.mapas.franciscojavier.trekkingroute;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class LoginActivity extends Fragment {

    //private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText editUsername;
    private EditText editPassword;
    private Button btnLogin;
    private Button btnRegister;
    private TextView loginLocked;
    private TextView attemptsLeft;
    private TextView numberOfRemainingLogin;
    int numberOfRemainingLoginAttempts = 3;
    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        View view = setupVariables(inflater,container);
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Toast.makeText(getActivity(),
                    "Estoy log", Toast.LENGTH_LONG)
                    .show();
            //Intent intent = new Intent(LoginActivity.this, dbActivity.class);
            //startActivity(intent);
            //finish();

        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = editUsername.getText().toString();
                String password = editPassword.getText().toString();

                // Check for empty data in the form
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    authenticateLogin(email,password);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getActivity(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Toast.makeText(getActivity(), "go to register",
                        Toast.LENGTH_SHORT).show();
                //Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                //startActivity(i);
                //finish();
                Fragment newFragment = new RegisterActivity();
                //newFragment.setTiempoTotal(tiempoTotalRecorrido);
                FragmentManager fm1 = getFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                ft1.replace(R.id.container, newFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    public void authenticateLogin(String email, String password) {

        if (editUsername.getText().toString().equals("admin") &&
                editPassword.getText().toString().equals("admin")) {
            Toast.makeText(getActivity(), "Hello admin!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "Seems like you are not admin!",
                    Toast.LENGTH_SHORT).show();

            boolean existe = checkLogin(email, password);

            if (!existe){
                numberOfRemainingLoginAttempts--;
                attemptsLeft.setVisibility(View.VISIBLE);
                numberOfRemainingLogin.setVisibility(View.VISIBLE);
                numberOfRemainingLogin.setText(Integer.toString(numberOfRemainingLoginAttempts));

                if (numberOfRemainingLoginAttempts == 0) {
                    btnLogin.setEnabled(false);
                    loginLocked.setVisibility(View.VISIBLE);
                    loginLocked.setBackgroundColor(Color.RED);
                    loginLocked.setText("LOGIN BLOCKED!!!");
                }
            }

        }

    }

    private View setupVariables(LayoutInflater inflater,@Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        editUsername = (EditText) view.findViewById(R.id.username);
        editPassword = (EditText) view.findViewById(R.id.password);
        btnLogin = (Button) view.findViewById(R.id.loginBtn);
        btnRegister = (Button) view.findViewById(R.id.btnLinkToRegisterScreen);
        loginLocked = (TextView) view.findViewById(R.id.loginLocked);
        attemptsLeft = (TextView) view.findViewById(R.id.attemptsLeft);
        numberOfRemainingLogin = (TextView) view.findViewById(R.id.numberOfRemainingLogin);
        numberOfRemainingLogin.setText(Integer.toString(numberOfRemainingLoginAttempts));
        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getActivity());
        return view;
    }
    private boolean  checkLogin(final String email, final String password) {

        Toast.makeText(getActivity(),
                "busco en la db", Toast.LENGTH_LONG)
                .show();

        return true;
        /**
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                dbActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "login");
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        */
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}


