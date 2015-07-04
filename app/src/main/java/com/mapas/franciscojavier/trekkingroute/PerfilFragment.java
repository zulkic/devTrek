package com.mapas.franciscojavier.trekkingroute;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.mapas.franciscojavier.trekkingroute.Account.MainCalls;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import JSON.Cambiar_Contrasenia;
import greendao.Usuario;

/**
 * Created by nicolas on 02-07-2015.
 */
public class PerfilFragment extends SherlockFragment implements AdapterView.OnClickListener {

    private EditText etName;
    private TextView etEmail;
    private EditText etPassword;
    private EditText etNewPassword;
    private EditText etNewPasswordConfirmation;
    private Button btnPerAceptar;
    private Button btnPerCancelar;

    private SharedPreferences pref;
    private String emailUser;
    private String nameUser;
    private String passUser;
    private MainCalls mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register);
        View v = inflater.inflate(R.layout.perfil, container, false);

        btnPerAceptar = (Button) v.findViewById(R.id.button_guardar_perfil);
        btnPerCancelar = (Button) v.findViewById(R.id.button_cancelar_perfil);
        etName = (EditText) v.findViewById(R.id.namePerfil);
        etEmail = (TextView) v.findViewById(R.id.emailPerfil);
        etPassword = (EditText) v.findViewById(R.id.oldPasswordPerfil);
        etNewPassword = (EditText) v.findViewById(R.id.newPasswordPerfil);
        etNewPasswordConfirmation = (EditText) v.findViewById(R.id.newPasswordConfirmPerfil);


        pref = this.getActivity().getSharedPreferences(Globals.PREF, Context.MODE_PRIVATE);
        emailUser = pref.getString(Globals.EMAIL, "Email");
        nameUser = pref.getString(Globals.NOMBRE, "Nombre");


        etEmail.setText(emailUser);
        etName.setText(nameUser);

        btnPerAceptar.setOnClickListener(this);
        btnPerCancelar.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_guardar_perfil:
                if (!formHaveErrors()) {
//                    Buscar_Usuario usuario = new Buscar_Usuario(etEmail.getText().toString(),getActivity(), mListener);
//                    usuario.execute();
                    Usuario client = new Usuario();
                    client.setContrasenia(etNewPassword.getText().toString());
                    client.setEmail(etEmail.getText().toString());
                    SharedPreferences pref = Globals.context.getSharedPreferences(Globals.PREF, Context.MODE_PRIVATE);
                    String pass = pref.getString(Globals.PASS, " ");
                    if(etPassword.getText().toString().equals(pass) )
                    {
                        Cambiar_Contrasenia cont = new Cambiar_Contrasenia( client, getActivity());
                        cont.execute();
                    }
                    else
                    {
                        Toast.makeText(getActivity().getBaseContext(), "Error ", Toast.LENGTH_SHORT).show();
                    }
                    mListener.goToHome();
                }
                break;
            case R.id.button_cancelar_perfil:
                mListener.goToHome();
                break;
            default:
                break;
        }
    }

    private boolean formHaveErrors() {
        boolean haveErrors = false;
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        if (etName.getText().toString().matches("")){
            etName.setError(getString(R.string.errorEmpty));
            etName.startAnimation(shake);
            haveErrors = true;
        }

        if (!etNewPassword.getText().toString().matches("") && !etNewPassword.getText().toString().matches(Globals.PASSWORD_REGEX)){
            etNewPassword.setError(getString(R.string.passwordError));
            etNewPassword.startAnimation(shake);
            haveErrors = true;
        }

        else if (etNewPassword.getText().toString().contains(" ") ) {
            etNewPassword.setError(getString(R.string.errorSpaces));
            etNewPassword.startAnimation(shake);
            haveErrors = true;
        }
        if (!etNewPassword.getText().toString().equals(etNewPasswordConfirmation.getText().toString())){
            etNewPasswordConfirmation.setError(getString(R.string.passwordConfirmationError));
            etNewPasswordConfirmation.startAnimation(shake);
            haveErrors = true;
        }
        if (!etNewPassword.getText().toString().matches("")&& etPassword.getText().toString().matches("")){
            etPassword.setError(getString(R.string.passwordOld));
            etPassword.startAnimation(shake);
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
    public void nada(){

    }
}
