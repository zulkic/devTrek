package com.mapas.franciscojavier.trekkingroute.Account;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
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
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import java.util.concurrent.ExecutionException;

import JSON.Registrar_Usuario;
import JSON.Verificar_Usuario;
import greendao.Usuario;

/**
 * Created by nicolas on 03-06-2015.
 */
public class RegisterFragment extends Fragment implements AdapterView.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String logEmail;
    private String logPassword;

    private Button btnRegister;
    private Button btnLinkToLoginScreen;
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirmation;

    private MainCalls mListener;

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        //setContentView(R.layout.activity_register);
        View v = inflater.inflate(R.layout.activity_register, container, false);

        btnRegister = (Button) v.findViewById(R.id.btnRegister);
        btnLinkToLoginScreen = (Button) v.findViewById(R.id.btnLinkToLoginScreen);
        etName = (EditText) v.findViewById(R.id.name);
        etEmail = (EditText) v.findViewById(R.id.email);
        etPassword = (EditText) v.findViewById(R.id.password);
        etPasswordConfirmation = (EditText) v.findViewById(R.id.passwordConfirm);

        etEmail.setText(logEmail, TextView.BufferType.EDITABLE);
        etPassword.setText(logPassword, TextView.BufferType.EDITABLE);

        btnRegister.setOnClickListener(this);
        btnLinkToLoginScreen.setOnClickListener(this);

        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                if (!formHaveErrors()) {
                    Usuario client = new Usuario();
                    client.setEmail(etEmail.getText().toString());
                    client.setNombre(etName.getText().toString());
                    client.setContrasenia(etPassword.getText().toString());
                    try {
                        if (verificarEmailDB(etEmail.getText().toString())){
                            Toast.makeText(getActivity().getBaseContext(), "El email ya existe, prueba otra vez", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //Nuevo_Usuario nuevoUsuario = new Nuevo_Usuario(client, getActivity())
                            Registrar_Usuario registar_usuario = new Registrar_Usuario(client, getActivity());
                            registar_usuario.execute();
                            Toast.makeText(getActivity().getBaseContext(), "En hora buena, Ya estas Resgistrado!!!", Toast.LENGTH_SHORT).show();
                            mListener.login(etEmail.getText().toString(), etPassword.getText().toString());
                        }
                    }catch (Exception e){
                        Log.i("Error post", e.toString());
                    }
                }
                break;
            case R.id.btnLinkToLoginScreen:
                mListener.login(etEmail.getText().toString(), etPassword.getText().toString());
                break;
            default:
                break;
        }
    }

    private Boolean verificarEmailDB(String newEmail) throws ExecutionException, InterruptedException {
        Verificar_Usuario verificar_usuario = new Verificar_Usuario(newEmail, getActivity());
        Boolean verificar = verificar_usuario.execute().get();
        Log.i("verificar: ", verificar.toString());
        return verificar;

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

    private boolean formHaveErrors() {
        boolean haveErrors = false;
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        if (etName.getText().toString().matches("")){
            etName.setError(getString(R.string.errorEmpty));
            etName.startAnimation(shake);
            haveErrors = true;
        }

        if (etEmail.getText().toString().matches("")) {
            etEmail.setError(getString(R.string.errorEmpty));
            etEmail.startAnimation(shake);
            haveErrors = true;
        }
        if (etEmail.getText().toString().contains(" ") ) {
            etEmail.setError(getString(R.string.errorSpaces));
            etEmail.startAnimation(shake);
            haveErrors = true;
        }

        if (!etEmail.getText().toString().matches(Globals.EMAIL_REGEX)) {
            etEmail.setError(getString(R.string.errorFormat));
            etEmail.startAnimation(shake);
            haveErrors = true;
        }
        if (!etPassword.getText().toString().matches(Globals.PASSWORD_REGEX)){
            etPassword.setError(getString(R.string.passwordError));
            etPassword.startAnimation(shake);
            haveErrors = true;
        }

        if (etPassword.getText().toString().contains(" ") ) {
            etPassword.setError(getString(R.string.errorSpaces));
            etPassword.startAnimation(shake);
            haveErrors = true;
        }
        if (!etPassword.getText().toString().equals(etPasswordConfirmation.getText().toString())){
            etPasswordConfirmation.setError(getString(R.string.passwordConfirmationError));
            etPasswordConfirmation.startAnimation(shake);
            haveErrors = true;
        }

        return haveErrors;
    }

}
