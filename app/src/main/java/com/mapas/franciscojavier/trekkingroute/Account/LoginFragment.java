package com.mapas.franciscojavier.trekkingroute.Account;


import android.app.Activity;
import android.app.ProgressDialog;
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

import com.actionbarsherlock.app.SherlockFragment;
import com.mapas.franciscojavier.trekkingroute.R;
import com.mapas.franciscojavier.trekkingroute.SessionManager;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import JSON.Autentificar_Usuario;
import JSON.Buscar_Usuario;


/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class LoginFragment extends SherlockFragment implements AdapterView.OnClickListener {

    //private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText editUserEmail;
    private EditText editPassword;
    private Button btnLogin;
    private Button btnRegister;
    private Button btnInvite;
    private TextView loginLocked;
    private TextView attemptsLeft;
    private TextView numberOfRemainingLogin;
    int numberOfRemainingLoginAttempts = 3;
    private ProgressDialog pDialog;
    private SessionManager session;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String logEmail;
    private String logPassword;
    public String email;
    private MainCalls mListener;

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            logEmail = getArguments().getString(ARG_PARAM1);
            logPassword = getArguments().getString(ARG_PARAM2);
        }
    }
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
        return view;
    }

    public Boolean authenticateLogin() {
        email = editUserEmail.getText().toString();
        String pass = editPassword.getText().toString();
        Boolean existe = false;
        if (email.equals("admin") &&
                pass.equals("admin")) {
            //Toast.makeText(getActivity(), "Hello admin!",Toast.LENGTH_SHORT).show();
        }
        else {
            //Toast.makeText(getActivity(), "Seems like you are not admin!",Toast.LENGTH_SHORT).show();

            existe = checkLogin(email, pass);
//Toast.makeText(getActivity(), (existe),Toast.LENGTH_SHORT).show();
            if (!existe){
                numberOfRemainingLoginAttempts--;
                attemptsLeft.setVisibility(View.VISIBLE);
                numberOfRemainingLogin.setVisibility(View.VISIBLE);
                numberOfRemainingLogin.setText(Integer.toString(numberOfRemainingLoginAttempts));

                if (numberOfRemainingLoginAttempts == 0) {
                    btnLogin.setEnabled(false);
                    loginLocked.setVisibility(View.VISIBLE);
                    //loginLocked.setBackgroundColor(Color.RED);
                    //loginLocked.setText("LOGIN BLOCKED!!!");
                }
                Toast.makeText(getActivity(), "Ese usuario no existe",Toast.LENGTH_SHORT).show();
            }

        }
        return existe;

    }

    private View setupVariables(LayoutInflater inflater,@Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        editUserEmail = (EditText) view.findViewById(R.id.userEmail);
        editPassword = (EditText) view.findViewById(R.id.password);
        btnLogin = (Button) view.findViewById(R.id.loginBtn);
        btnRegister = (Button) view.findViewById(R.id.btnLinkToRegisterScreen);
        btnInvite = (Button) view.findViewById(R.id.btnInvitado);
        loginLocked = (TextView) view.findViewById(R.id.loginLocked);
        attemptsLeft = (TextView) view.findViewById(R.id.attemptsLeft);
        numberOfRemainingLogin = (TextView) view.findViewById(R.id.numberOfRemainingLogin);
        numberOfRemainingLogin.setText(Integer.toString(numberOfRemainingLoginAttempts));
        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        editUserEmail.setText(logEmail, TextView.BufferType.EDITABLE);
        editPassword.setText(logPassword, TextView.BufferType.EDITABLE);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnInvite.setOnClickListener(this);

        // Session manager
        session = new SessionManager(getActivity());
        return view;
    }
    private boolean  checkLogin(final String email, final String password) {
        Autentificar_Usuario autentificar_usuario = new Autentificar_Usuario(email, password, getActivity());
        Boolean autentificado = false;
        try {
            autentificado = autentificar_usuario.execute().get();
        }
        catch (Exception e)
        {
        }
        return autentificado;
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginBtn:

                if (!formHaveErrors()) {

                    //Toast.makeText(getActivity(), "no error",Toast.LENGTH_SHORT).show();
                    Boolean existe = authenticateLogin();
                    if (existe){
                        try {
                            Buscar_Usuario buscar = new Buscar_Usuario(this.email,getActivity(), mListener);
                            buscar.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.btnLinkToRegisterScreen:
                mListener.goToRegister(editUserEmail.getText().toString(), editPassword.getText().toString());
                break;
            case R.id.btnInvitado:
                mListener.goToHome();
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
        else if (editUserEmail.getText().toString().contains(" ") ) {
            editUserEmail.setError(getString(R.string.errorSpaces));
            editUserEmail.startAnimation(shake);
            haveErrors = true;
        }

        else if (!editUserEmail.getText().toString().matches(Globals.EMAIL_REGEX)) {
            editUserEmail.setError(getString(R.string.errorFormat));
            editUserEmail.startAnimation(shake);
            haveErrors = true;
        }
        if (editPassword.getText().toString().matches("")) {
            editPassword.setError(getString(R.string.errorEmpty));
            editPassword.startAnimation(shake);
            haveErrors = true;
        }
        else if ( editPassword.getText().toString().contains(" ") ) {
            editPassword.setError(getString(R.string.errorSpaces));
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


