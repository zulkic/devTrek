package com.mapas.franciscojavier.trekkingroute;

/**
 * Created by nicolas on 10-05-2015.
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import JSON.Modificar_Ruta;
import JSON.Nueva_Ruta;
import JSON.Post_Coordenadas_Ruta;
import JSON.Post_Puntos_Interes_Ruta;
import greendao.Coordenada;
import greendao.Punto_interes;
import greendao.Ruta;


public class DetallesCrearRuta extends Fragment implements View.OnClickListener{

    String tiempoTotal;
    int LARGO_NOMBRE_RUTA=1;
    private static final String ARG_PARAM1 = "nada";
    private static final String ARG_PARAM2 = "vacio";
    private static String ARG_TIEMPO_RUTA = "hollow";
    private static String ARG_DISTANCIA_RUTA;
    private static String ARG_NOMBRE_RUTA="";
    private static String ARG_DESCRIPCION_RUTA="";
    private static Integer ARG_ID_RUTA;
    private static boolean ARG_EDITAR = false;
    private Spinner spinnerReco;
    private Button btnSubmit;
    private String Caminando = "Caminando";//getString(R.string.trail_caminando);
    private String Trotando="Trotando";//getString(R.string.trail_trotando);
    private String Corriendo="Corriendo";//getString(R.string.trail_corriendo);
    private String Bicicleta="Bicicleta";//getString(R.string.trail_bicicleta);
    private String Caballo="Caballo";//getString(R.string.trail_caballo);
    private String Auto="Auto";//getString(R.string.trail_auto);
    private static ArrayList<Coordenada> lista_coordenadas;
    private static ArrayList<Punto_interes> lista_puntos_interes;

    String[] listRecorido = {Caminando, Trotando, Corriendo,Bicicleta, Caballo, Auto};

    public DetallesCrearRuta() {
        // Required empty public constructor
    }
    public static DetallesCrearRuta newInstance(String tiempoTotalRuta, float distaciaRuta, ArrayList<Coordenada> coordenadas, ArrayList<Punto_interes> puntos_interes) {
        DetallesCrearRuta fragment = new DetallesCrearRuta();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, tiempoTotalRuta);
        args.putFloat(ARG_PARAM2, distaciaRuta);
        fragment.setArguments(args);
        ARG_TIEMPO_RUTA=tiempoTotalRuta;
        //trunco la distacia con 2 decimales
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
        String mts = df.format(distaciaRuta);
        //float mtrs = Float.valueOf(mts);
        ARG_DISTANCIA_RUTA=mts;
        Log.d("ARG_DISTANCIA_RUTA",ARG_DISTANCIA_RUTA);

        lista_coordenadas = coordenadas;
        lista_puntos_interes = puntos_interes;
        return fragment;
    }
    public static DetallesCrearRuta newInstance(String tiempoTotalRuta, float distaciaRuta,String nombreRuta, String descripcionRuta,Integer id_ruta) {
        DetallesCrearRuta fragment = new DetallesCrearRuta();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, tiempoTotalRuta);
        args.putFloat(ARG_PARAM2, distaciaRuta);
        fragment.setArguments(args);
        ARG_TIEMPO_RUTA=tiempoTotalRuta;
        ARG_NOMBRE_RUTA=nombreRuta;
        ARG_DESCRIPCION_RUTA=descripcionRuta;
        ARG_ID_RUTA=id_ruta;
        ARG_EDITAR=true;
        //trunco la distacia con 2 decimales
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
        String mts = df.format(distaciaRuta);
        //float mtrs = Float.valueOf(mts);
        mts = mts.replace(',', '.');
        ARG_DISTANCIA_RUTA=mts;
        Log.d("ARG_DISTANCIA_RUTA",ARG_DISTANCIA_RUTA);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detalles_ruta, container, false);
        Button botonGuardar = (Button) v.findViewById(R.id.button_guardar_ruta);
        Button botonCancelar = (Button) v.findViewById(R.id.button_cancelar_ruta);
        EditText editNombreRuta = (EditText) v.findViewById(R.id.editText_nombre_ruta);
        EditText textTiempoEstimado = (EditText)v.findViewById(R.id.editTextiempo_estimado);
        EditText textDistanciaRecorrida = (EditText)v.findViewById(R.id.editText_distancia_recorrida);
        EditText editDescripcion = (EditText) v.findViewById(R.id.editText_descripcion);

        editNombreRuta.setOnClickListener(this);
        botonGuardar.setOnClickListener(this);
        botonCancelar.setOnClickListener(this);
        textTiempoEstimado.setText(ARG_TIEMPO_RUTA);
        textTiempoEstimado.setOnClickListener(this);
        editDescripcion.setOnClickListener(this);

        textDistanciaRecorrida.setText(ARG_DISTANCIA_RUTA);
        textDistanciaRecorrida.setOnClickListener(this);
        if(ARG_EDITAR){
            editNombreRuta.setText(ARG_NOMBRE_RUTA);
            editDescripcion.setText(ARG_DESCRIPCION_RUTA);
        }

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
        spinnerReco.setAdapter(new MyCustomAdapter(getActivity(), R.layout.row_spinner, listRecorido));

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();

        //FIN DEL SPINER

        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_guardar_ruta:
                EditText editTextNombreRuta= (EditText) getActivity().findViewById(R.id.editText_nombre_ruta);
                EditText textTiempoEstimado = (EditText)getActivity().findViewById(R.id.editTextiempo_estimado);
                spinnerReco = (Spinner) getActivity().findViewById(R.id.spinner_recorrido);
                EditText editTextDescripcion= (EditText) getActivity().findViewById(R.id.editText_descripcion);
                if(!formHaveErrors(editTextNombreRuta)) {
                    //Toast.makeText(getActivity().getBaseContext(), editTextNombreRuta.getText()+" \n "+ ARG_PARAM3, Toast.LENGTH_SHORT).show();
                    String nombreRuta = editTextNombreRuta.getText().toString();
                    String tiempoRuta = textTiempoEstimado.getText().toString();
                    String tipoRuta = String.valueOf(spinnerReco.getSelectedItem());
                    float mtrs = Float.valueOf(ARG_DISTANCIA_RUTA);
                    String descripcionRuta = editTextDescripcion.getText().toString();

                    Ruta nuevaRuta = new Ruta();
                    nuevaRuta.setNombre(nombreRuta);
                    nuevaRuta.setTiempo_estimado(tiempoRuta);
                    //nuevaRuta.setTipo(tipoRuta);      FALTA AGREGAR ESTA FUNCION
                    nuevaRuta.setDescripcion(descripcionRuta);
                    nuevaRuta.setKms(mtrs);
                    nuevaRuta.setOficial(true);    //verificar mas adelante

                    try
                    {
                        if(ARG_EDITAR)
                        {
                            Log.i("id ruta: ", ARG_ID_RUTA.toString());
                            nuevaRuta.setId( ARG_ID_RUTA.longValue() );
                            Modificar_Ruta tarea_modificar_ruta = new Modificar_Ruta(nuevaRuta, getActivity());
                            tarea_modificar_ruta.execute();

                            Toast.makeText(getActivity().getBaseContext(),"Ruta Modificada con exito ", Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();

                        }
                        else {
                            Nueva_Ruta tarea_agregar_ruta = new Nueva_Ruta(nuevaRuta, getActivity());
                            int id = tarea_agregar_ruta.execute().get();
                            for (Coordenada coordenada : lista_coordenadas) {
                                coordenada.setId_ruta(id);
                            }
                            Post_Coordenadas_Ruta tarea_agregar_coordenadas = new Post_Coordenadas_Ruta(lista_coordenadas, getActivity());
                            tarea_agregar_coordenadas.execute();

                            for(Punto_interes punto_interes : lista_puntos_interes)
                            {
                                punto_interes.setId_ruta(id);
                            }

                            Post_Puntos_Interes_Ruta tarea_agregar_puntos = new Post_Puntos_Interes_Ruta(lista_puntos_interes, getActivity());
                            tarea_agregar_puntos.execute();

                                //obstaculos

                            Toast.makeText(getActivity().getBaseContext(),"Ruta Creada con exito ", Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();

                        }

                    }
                    catch (Exception e)
                    {
                        Log.i("Error post", e.toString());
                    }
                }
                break;
            case R.id.button_cancelar_ruta:
                //getActivity().onBackPressed();Toast.makeText(getActivity().getBaseContext(),
                getFragmentManager().popBackStack();
                break;
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
            View row=inflater.inflate(R.layout.row_spinner, parent, false);
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
    private boolean formHaveErrors(EditText editTextNombreRuta) {
        boolean haveErrors = false;
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        if (editTextNombreRuta.getText().toString().matches("")){
            editTextNombreRuta.setError(getString(R.string.errorEmpty));
            editTextNombreRuta.startAnimation(shake);
            haveErrors = true;
        }

        return haveErrors;
    }


}