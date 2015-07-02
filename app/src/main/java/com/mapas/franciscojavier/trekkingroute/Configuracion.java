package com.mapas.franciscojavier.trekkingroute;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * Created by nicolas on 10-06-2015.
 */
public class Configuracion extends SherlockFragment implements View.OnClickListener{

    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private Button btnCambiarIdioma;
    private Button btnCancelarIdioma;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        View v = inflater.inflate(R.layout.frament_configuracion, container, false);

        radioSexGroup = (RadioGroup) v.findViewById(R.id.radioIdioma);
        btnCambiarIdioma = (Button) v.findViewById(R.id.btnCambiarIdioma);
        btnCancelarIdioma = (Button) v.findViewById(R.id.btnCancelarIdioma);

        btnCambiarIdioma.setOnClickListener(this);
        btnCancelarIdioma.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCambiarIdioma:
                // get selected radio button from radioGroup
                int selectedId = radioSexGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioSexButton = (RadioButton) getActivity().findViewById(selectedId);

                Toast.makeText(getActivity(),radioSexButton.getText(), Toast.LENGTH_SHORT).show();

                //idioma("en");
        }
    }
}
