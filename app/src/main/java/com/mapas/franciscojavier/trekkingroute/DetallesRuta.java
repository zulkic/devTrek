package com.mapas.franciscojavier.trekkingroute;

/**
 * Created by nicolas on 10-05-2015.
 */

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class DetallesRuta extends Fragment implements View.OnClickListener{

    String tiempoTotal;
    int LARGO_NOMBRE_RUTA=1;
    private static final String ARG_PARAM1 = "nada";
    private static final String ARG_PARAM2 = "vacio";
    private static String ARG_PARAM3 = "hollow";
    private Spinner spinnerReco;
    private Button btnSubmit;
    private String Caminando="\nCaminando";
    private String Trotando="\nTrotando";
    private String Corriendo="\nCorriendo";
    private String Bicicleta="\nBicicleta";
    private String Caballo="\nCaballo";
    private String Auto="\nAuto";

    String[] listRecorido = {Caminando, Trotando, Corriendo,Bicicleta, Caballo, Auto};

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
        spinnerReco = (Spinner) v.findViewById(R.id.spinner_recorrido);
        /**List<String> listRecorido = new ArrayList<String>();
        listRecorido.add("Caminando");
        listRecorido.add("Trotando");
        listRecorido.add("Corriendo");
        listRecorido.add("En Bicicleta");
        listRecorido.add("A Caballo");
        listRecorido.add("En Auto");*/

        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,listRecorido);

        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //spinnerReco.setAdapter(dataAdapter);
        spinnerReco.setAdapter(new MyCustomAdapter(getActivity(), R.layout.row, listRecorido));

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
                spinnerReco = (Spinner) getActivity().findViewById(R.id.spinner_recorrido);
                if(e.getText().length()<LARGO_NOMBRE_RUTA){
                    Toast.makeText(getActivity().getBaseContext(), "Ingrese un Nombre", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(getActivity().getBaseContext(), e.getText()+" \n "+ ARG_PARAM3, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity().getBaseContext(),
                            "Datos: "+"\n"
                                   + e.getText()+"\n"
                                   + textFragment.getText()+"\n"
                                   + String.valueOf(spinnerReco.getSelectedItem())
                            ,Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                return;
        }
    }
    // Add spinner data
    public void addListenerOnSpinnerItemSelection(){

        spinnerReco.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public class MyCustomAdapter extends ArrayAdapter<String>{

        public MyCustomAdapter(Context context, int textViewResourceId,
                               String[] objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater=getActivity().getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false);
            TextView label=(TextView)row.findViewById(R.id.tipo_recorrido);
            label.setText(listRecorido[position]);

            ImageView icon=(ImageView)row.findViewById(R.id.icon);

            if (listRecorido[position]==Caminando){
                icon.setImageResource(R.drawable.ic_caminando);
            }
            else if (listRecorido[position]==Trotando){
                icon.setImageResource(R.drawable.ic_trotando);
            }
            else if (listRecorido[position]==Corriendo){
                icon.setImageResource(R.drawable.ic_corriendo);
            }
            else if (listRecorido[position]==Bicicleta){
                icon.setImageResource(R.drawable.ic_bicicleta);
            }
            else if (listRecorido[position]==Caballo){
                icon.setImageResource(R.drawable.ic_caballo);
            }
            else if (listRecorido[position]==Auto){
                icon.setImageResource(R.drawable.ic_auto);
            }
            else{
                icon.setImageResource(R.drawable.ic_droid);
            }

            return row;
        }
    }

}