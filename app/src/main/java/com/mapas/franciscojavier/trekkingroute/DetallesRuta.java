package com.mapas.franciscojavier.trekkingroute;

/**
 * Created by nicolas on 10-05-2015.
 */

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class DetallesRuta extends Fragment implements View.OnClickListener{

    String tiempoTotal;
    int LARGO_NOMBRE_RUTA=1;
    private static final String ARG_PARAM1 = "nada";
    private static final String ARG_PARAM2 = "vacio";
    private static String ARG_PARAM3 = "hollow";
    private Spinner spinner1;
    private Button btnSubmit;
    public DetallesRuta() {
        // Required empty public constructor
    }
    public static DetallesRuta newInstance(String param1, String param2) {
        DetallesRuta fragment = new DetallesRuta();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        ARG_PARAM3=param1;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detalles_ruta, container, false);
        Button b = (Button) v.findViewById(R.id.button_guardar_ruta);
        EditText e = (EditText) v.findViewById(R.id.editText_nombre_ruta);
        TextView textFragment = (TextView)v.findViewById(R.id.editText_tiempo_estimado);

        e.setOnClickListener(this);
        b.setOnClickListener(this);
        textFragment.setText(ARG_PARAM3);
        textFragment.setOnClickListener(this);

        //SPINER
        spinner1 = (Spinner) v.findViewById(R.id.spinner_recorrido);
        List<String> list = new ArrayList<String>();
        list.add("Caminando");
        list.add("Trotando");
        list.add("Corriendo");
        list.add("En Bicicleta");
        list.add("A Caballo");
        list.add("En Auto");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();

        //FIN DEL SPINER

        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_guardar_ruta:
                EditText e = (EditText) getActivity().findViewById(R.id.editText_nombre_ruta);
                TextView textFragment = (TextView)getActivity().findViewById(R.id.editText_tiempo_estimado);
                spinner1 = (Spinner) getActivity().findViewById(R.id.spinner_recorrido);
                if(e.getText().length()<LARGO_NOMBRE_RUTA){
                    Toast.makeText(getActivity().getBaseContext(), "Ingrese un Nombre", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(getActivity().getBaseContext(), e.getText()+" \n "+ ARG_PARAM3, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity().getBaseContext(),
                            "Datos: "+"\n"
                                   + e.getText()+"\n"
                                   + textFragment.getText()+"\n"
                                   + String.valueOf(spinner1.getSelectedItem())
                            ,Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                return;
        }
    }
    // Add spinner data
    public void addListenerOnSpinnerItemSelection(){

        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

}