package com.mapas.franciscojavier.trekkingroute;

/**
 * Created by nicolas on 10-05-2015.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.Nullable;

import com.actionbarsherlock.app.SherlockFragment;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;
import com.mapas.franciscojavier.trekkingroute.Utility.Wrapper;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import JSON.Modificar_Ruta;
import JSON.Nueva_Ruta;
import JSON.Post_Coordenadas_Ruta;
import JSON.Post_Obstaculos_Ruta;
import JSON.Post_Puntos_Interes_Ruta;
import greendao.Coordenada;
import greendao.Obstaculo;
import greendao.Punto_interes;
import greendao.Region;
import greendao.Ruta;
import repositorios.RegionRepo;


public class DetallesCrearRuta extends SherlockFragment implements AdapterView.OnClickListener{

    String tiempoTotal;
    private static final String ARG_PARAM1 = "nada";
    private static final String ARG_PARAM2 = "vacio";
    private static String ARG_TIEMPO_RUTA = "hollow";
    private static String ARG_DISTANCIA_RUTA;
    private static String ARG_NOMBRE_RUTA="";
    private static String ARG_DESCRIPCION_RUTA="";
    private static Integer ARG_ID_RUTA;
    private static boolean ARG_EDITAR;
    private Spinner spinnerReco;
    private Spinner spinnerRegion;
    private Button btnSubmit;
    private String Caminando ;
    private String Trotando;
    private String Corriendo;
    private String Bicicleta;
    private String Caballo;
    private String Auto;
    private static ArrayList<Coordenada> lista_coordenadas;
    private static ArrayList<Punto_interes> lista_puntos_interes;
    private static ArrayList<Obstaculo> lista_obstaculos;
    Button botonGuardar;
    Button botonCancelar;
    EditText editNombreRuta;
    TextView textTiempoEstimado;
    TextView textDistanciaRecorrida;
    EditText editDescripcion;

    List<String> listReg = new ArrayList<String>();
    String[] listRecorido = {Caminando, Trotando, Corriendo,Bicicleta, Caballo, Auto};

    String[] listRegion= {Caminando, Trotando, Corriendo,Bicicleta, Caballo, Auto};

    public DetallesCrearRuta() {
        // Required empty public constructor
    }

    public static DetallesCrearRuta newInstance(String tiempoTotalRuta, float distaciaRuta, ArrayList<Coordenada> coordenadas, ArrayList<Punto_interes> puntos_interes, ArrayList<Obstaculo> obstaculos) {
        DetallesCrearRuta fragment = new DetallesCrearRuta();
        ARG_EDITAR = false;
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
        lista_obstaculos = obstaculos;
        return fragment;
    }
    public static DetallesCrearRuta newInstance(String tiempoTotalRuta, float distaciaRuta,
                                                String nombreRuta, String descripcionRuta,
                                                Integer id_ruta) {
        DetallesCrearRuta fragment = new DetallesCrearRuta();
        ARG_EDITAR = true;
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

        ARG_DISTANCIA_RUTA=mts;
        Log.d("ARG_DISTANCIA_RUTA",ARG_DISTANCIA_RUTA);
        Log.d("ARG_DISTANCIA_RUTA",mts);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detalles_ruta, container, false);
        botonGuardar = (Button) v.findViewById(R.id.button_guardar_ruta);
        botonCancelar = (Button) v.findViewById(R.id.button_cancelar_ruta);
        editNombreRuta = (EditText) v.findViewById(R.id.editText_nombre_ruta);
        textTiempoEstimado = (TextView)v.findViewById(R.id.editTextiempo_estimado);
        textDistanciaRecorrida = (TextView)v.findViewById(R.id.editText_distancia_recorrida);
        editDescripcion = (EditText) v.findViewById(R.id.editText_descripcion);


//SPINER
        List<Region> regiones = RegionRepo.getAllRegiones(getActivity());
        int largo = regiones.size();
        int i=0;
        while(i<largo){
            Region reg = regiones.get(i);
            String nom = reg.getNombre();
            listReg.add(nom);
            i++;
        }
        spinnerRegion = (Spinner) v.findViewById(R.id.spinner_region_detalles);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, listReg);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(dataAdapter);

        spinnerRegion = (Spinner) v.findViewById(R.id.spinner_region_detalles);

//        Caminando = getString(R.string.trail_caminando);
//        Trotando=getString(R.string.trail_trotando);
//        Corriendo=getString(R.string.trail_corriendo);
//        Bicicleta=getString(R.string.trail_bicicleta);
//        Caballo=getString(R.string.trail_caballo);
//        Auto=getString(R.string.trail_auto);
//        String[] listRegion = {Caminando, Trotando, Corriendo,Bicicleta, Caballo, Auto};
//        this.listRegion = listRegion;
        //spinnerRegion.setAdapter(new MyCustomAdapter(getActivity(), R.layout.row_spinner, this.listRegion));

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection(spinnerRegion);
        /*spinnerRegion.setOnClickListener(this);*/

//FIN DEL SPINER

        //SPINER
        spinnerReco = (Spinner) v.findViewById(R.id.spinner_recorrido);

        Caminando = getString(R.string.trail_caminando);
        Trotando=getString(R.string.trail_trotando);
        Corriendo=getString(R.string.trail_corriendo);
        Bicicleta=getString(R.string.trail_bicicleta);
        Caballo=getString(R.string.trail_caballo);
        Auto=getString(R.string.trail_auto);
        String[] listReco = {Caminando, Trotando, Corriendo,Bicicleta, Caballo, Auto};
        this.listRecorido = listReco;
        spinnerReco.setAdapter(new MyCustomAdapter(getActivity(), R.layout.row_spinner, this.listRecorido));

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection(spinnerReco);
        /*spinnerReco.setOnClickListener(this);*/

        //FIN DEL SPINER
        textTiempoEstimado.setText(ARG_TIEMPO_RUTA);
        textDistanciaRecorrida.setText(ARG_DISTANCIA_RUTA);

        if(ARG_EDITAR){
            editNombreRuta.setText(ARG_NOMBRE_RUTA);
            editDescripcion.setText(ARG_DESCRIPCION_RUTA);
        }
        botonGuardar.setOnClickListener(this);
        botonCancelar.setOnClickListener(this);
        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_guardar_ruta:
                EditText editTextNombreRuta= (EditText) getActivity().findViewById(R.id.editText_nombre_ruta);
                TextView textTiempoEstimado = (TextView)getActivity().findViewById(R.id.editTextiempo_estimado);
                spinnerReco = (Spinner) getActivity().findViewById(R.id.spinner_recorrido);
                spinnerRegion = (Spinner) getActivity().findViewById(R.id.spinner_region_detalles);
                EditText editTextDescripcion= (EditText) getActivity().findViewById(R.id.editText_descripcion);
                String tipoRuta = String.valueOf(spinnerReco.getSelectedItem());

                Long idRegionLong = buscarRegionSpin(spinnerRegion);
                Integer idRegionInt = Integer.valueOf(idRegionLong.intValue());

                if(!formHaveErrors(editTextNombreRuta)) {
                    String nombreRuta = editTextNombreRuta.getText().toString();
                    String tiempoRuta = textTiempoEstimado.getText().toString();


                    if(ARG_DISTANCIA_RUTA.contains(",")){
                        ARG_DISTANCIA_RUTA = ARG_DISTANCIA_RUTA.replace(',', '.');
                    }
                    float mtrs = Float.valueOf(ARG_DISTANCIA_RUTA);
                    String descripcionRuta = editTextDescripcion.getText().toString();

                    Ruta nuevaRuta = new Ruta();
                    nuevaRuta.setNombre(nombreRuta);
                    nuevaRuta.setTiempo_estimado(tiempoRuta);
                    nuevaRuta.setTipo(tipoRuta);
                    nuevaRuta.setDescripcion(descripcionRuta);
                    nuevaRuta.setKms(mtrs);
                    nuevaRuta.setOficial(true);    //verificar mas adelante
                    nuevaRuta.setSincronizada(false);
                    nuevaRuta.setTipo(tipoRuta);
                    nuevaRuta.setId_region(idRegionInt);
                    nuevaRuta.setFavorita(false);
                    //Toast.makeText(getActivity().getBaseContext(),tipoRuta+"+ "+idRegionInt, Toast.LENGTH_SHORT).show();

                    try
                    {
                        if(ARG_EDITAR)
                        {
                            Log.i("id ruta: ", ARG_ID_RUTA.toString());
                            nuevaRuta.setId( ARG_ID_RUTA.longValue() );
                            Modificar_Ruta tarea_modificar_ruta = new Modificar_Ruta(nuevaRuta, getActivity());
                            tarea_modificar_ruta.execute();

                            Toast.makeText(getActivity().getBaseContext(),"Ruta Modificada con exito ", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = Globals.ft.beginTransaction();
                            ft.replace(R.id.content_frame, new PantallaInicio());
                            ft.commit();

                        }
                        else {
                            if(lista_coordenadas.size() >= 2) {
                                Nueva_Ruta tarea_agregar_ruta = new Nueva_Ruta(nuevaRuta, getActivity());
                                Wrapper wrapper = tarea_agregar_ruta.execute().get();
                                int id = wrapper.getId();
                                if (id > 0) {
                                    Post_Coordenadas_Ruta tarea_agregar_coordenadas = new Post_Coordenadas_Ruta(lista_coordenadas, getActivity(), wrapper);
                                    tarea_agregar_coordenadas.execute();

                                    Post_Puntos_Interes_Ruta tarea_agregar_puntos = new Post_Puntos_Interes_Ruta(lista_puntos_interes, getActivity(), wrapper);
                                    tarea_agregar_puntos.execute();

                                    Post_Obstaculos_Ruta tarea_agregar_obstaculos = new Post_Obstaculos_Ruta(lista_obstaculos, getActivity(), wrapper);
                                    tarea_agregar_obstaculos.execute();

                                    Toast.makeText(getActivity().getBaseContext(), "Ruta Creada con exito ", Toast.LENGTH_SHORT).show();
                                    FragmentTransaction ft = Globals.ft.beginTransaction();
                                    ft.replace(R.id.content_frame, new PantallaInicio());
                                    ft.commit();
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity().getBaseContext(), "No hay suficientes puntos para crear la ruta ", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    catch (Exception e)
                    {
                        Log.i("Error post", e.toString());
                        Toast.makeText(getActivity().getBaseContext(), "Error en la creación de la ruta ", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.button_cancelar_ruta:
                //getActivity().onBackPressed();Toast.makeText(getActivity().getBaseContext(),
                if(ARG_EDITAR) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id_ruta", ARG_ID_RUTA);
                    Fragment frag = new MostrarRuta();
                    frag.setArguments(bundle);
                    FragmentTransaction ft = Globals.ft.beginTransaction();
                    ft.replace(R.id.content_frame, frag);
                    ft.commit();
                }
                else{
                    FragmentTransaction ft = Globals.ft.beginTransaction();
                    ft.replace(R.id.content_frame, new CrearRuta());
                    ft.commit();

                }
                break;
        }
    }

    private Long buscarRegionSpin(Spinner spinnerRegion) {

        Long id=0l;
        String stgRegion = String.valueOf(spinnerRegion.getSelectedItem());
        List<Region> regiones = RegionRepo.getAllRegiones(getActivity());
        for(int i=0; i<regiones.size();i++){
            if(regiones.get(i).getNombre().equals(stgRegion)){
                id = regiones.get(i).getId();
                break;
            }
        }
        return id;

    }

    // Add spinner data
    public void addListenerOnSpinnerItemSelection(Spinner spin){

        spin.setOnItemSelectedListener(new CustomOnItemSelectedListener());
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
//            else if(listRe[position]==list.get(position)){
//                icon.setImageResource(R.drawable.ic_auto);
//            }
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