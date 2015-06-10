package com.mapas.franciscojavier.trekkingroute.Account;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mapas.franciscojavier.trekkingroute.R;
import com.mapas.franciscojavier.trekkingroute.SessionManager;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;


/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class LoginFragment extends Fragment implements AdapterView.OnClickListener {

    //private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText editUserEmail;
    private EditText editPassword;
    private Button btnLogin;
    private Button btnRegister;
    private TextView loginLocked;
    private TextView attemptsLeft;
    private TextView numberOfRemainingLogin;
    int numberOfRemainingLoginAttempts = 3;
    private ProgressDialog pDialog;
    private SessionManager session;

    private MainCalls mListener;


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
/*
        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = editUserEmail.getText().toString();
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
        });*/
        return view;
    }

    public void authenticateLogin() {
        String email = editUserEmail.getText().toString();
        String pass = editPassword.getText().toString();
        if (email.equals("admin") &&
                pass.equals("admin")) {
            Toast.makeText(getActivity(), "Hello admin!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "Seems like you are not admin!",
                    Toast.LENGTH_SHORT).show();

            boolean existe = checkLogin(email, pass);

            if (existe){
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
        editUserEmail = (EditText) view.findViewById(R.id.userEmail);
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

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        // Session manager
        session = new SessionManager(getActivity());
        return view;
    }
    private boolean  checkLogin(final String email, final String password) {

        Toast.makeText(getActivity(),
                "busco en la db", Toast.LENGTH_LONG)
                .show();

        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:

                if (!formHaveErrors()) {

                    Toast.makeText(getActivity(), "no error",
                            Toast.LENGTH_SHORT).show();
                    authenticateLogin();
                    //mListener.loginGoBack(editUserEmail.getText().toString(), editPassword.getText().toString());
                }
                break;
            case R.id.btnLinkToRegisterScreen:
                mListener.goToRegister(editUserEmail.getText().toString(), editPassword.getText().toString());
                break;
            default:
                break;
        }

    }
    private boolean formHaveErrors() {
        boolean haveErrors = false;
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        if (editUserEmail.getText().toString().matches("") ) {
            editUserEmail.setError(getString(R.string.errorEmpty));
            editUserEmail.startAnimation(shake);
            haveErrors = true;
        }
        if (editUserEmail.getText().toString().contains(" ") ) {
            editUserEmail.setError(getString(R.string.errorSpaces));
            editUserEmail.startAnimation(shake);
            haveErrors = true;
        }

        if (!editUserEmail.getText().toString().matches(Globals.EMAIL_REGEX)) {
            editUserEmail.setError(getString(R.string.errorFormat));
            editUserEmail.startAnimation(shake);
            haveErrors = true;
        }
        if (editPassword.getText().toString().matches("")) {
            editPassword.setError(getString(R.string.errorEmpty));
            editPassword.startAnimation(shake);
            haveErrors = true;
        }
        if ( editPassword.getText().toString().contains(" ") ) {
            editPassword.setError(getString(R.string.errorSpaces));
            editPassword.startAnimation(shake);
            haveErrors = true;
        }
        if (!editPassword.getText().toString().matches(Globals.PASSWORD_REGEX)){
            editPassword.setError(getString(R.string.passwordError));
            editPassword.startAnimation(shake);
            haveErrors = true;
        }


        return haveErrors;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MainCalls) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MainCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}

