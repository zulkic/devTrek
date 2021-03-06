package com.mapas.franciscojavier.trekkingroute;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import java.util.List;

import JSON.Eliminar_Ruta;
import greendao.Region;
import repositorios.RegionRepo;


public class DetallesEliminarRuta extends SherlockFragment implements View.OnClickListener{

    int LARGO_NOMBRE_RUTA=1;
    private static Long ID_RUTA;
    private static String NOMBRE="nada", TIEMPO="nada", TIPO="nada", DESCRIPCION="nada";
    private static int ID_REGION=0;
    private static float KMS=0;


    public DetallesEliminarRuta() {
        // Required empty public constructor
    }
    public static DetallesEliminarRuta newInstance(Long idRuta, String nombre, String tiempo,
                                                   float kms,String tipo, String descripcion, Integer idRegionInt) {
        DetallesEliminarRuta fragment = new DetallesEliminarRuta();
        Bundle args = new Bundle();
        ID_RUTA=idRuta;
        NOMBRE=nombre;
        TIEMPO=tiempo;
        KMS=kms;
        TIPO=tipo;
        DESCRIPCION=descripcion;
        ID_REGION = idRegionInt;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detalles_eliminar_ruta, container, false);
        Button botonAceptar = (Button) v.findViewById(R.id.button_aceptar_eliminar_ruta);
        Button botonCancelar = (Button) v.findViewById(R.id.button_cancelar_eliminar_ruta);
        TextView nombreRuta = (TextView) v.findViewById(R.id.editTextEliminar_nombre_ruta);
        TextView tiempoEstimado = (TextView) v.findViewById(R.id.editTextEliminar_tiempo_estimado);
        TextView distanciaRecorrida = (TextView) v.findViewById(R.id.editTextEliminar_distancia_recorrida);
        TextView tipoRecorrido = (TextView) v.findViewById(R.id.editTextEliminar_recorrido_mediante);
        TextView descripcion = (TextView) v.findViewById(R.id.editTextEliminar_descripcion);
        TextView region = (TextView) v.findViewById(R.id.editTextEliminar_nombre_region);

        String km = Float.toString(KMS);

        nombreRuta.setText(NOMBRE);
        nombreRuta.setOnClickListener(this);
        tiempoEstimado.setText(TIEMPO);
        tiempoEstimado.setOnClickListener(this);
        distanciaRecorrida.setText(km);
        distanciaRecorrida.setOnClickListener(this);
        tipoRecorrido.setText(TIPO);
        tipoRecorrido.setOnClickListener(this);
        descripcion.setText(DESCRIPCION);
        descripcion.setOnClickListener(this);
        String regionNombre = buscarNombreRegion(ID_REGION);
        region.setText(regionNombre);

        botonAceptar.setOnClickListener(this);
        botonCancelar.setOnClickListener(this);

        return v;
    }

    private String buscarNombreRegion(Integer id_region) {
        String nombre="nombre";
        Long idRegionLong = id_region.longValue();
        List<Region> regiones = RegionRepo.getAllRegiones(getActivity());
        for(int i=0; i<regiones.size();i++){
            if(regiones.get(i).getId().equals(idRegionLong)){
                nombre = regiones.get(i).getNombre();
            }
        }
        return nombre;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft;
        switch (v.getId()){
            case R.id.button_aceptar_eliminar_ruta:
                try{
                    Eliminar_Ruta eliminar = new Eliminar_Ruta(ID_RUTA, getActivity());
                    eliminar.execute();
                }
                catch (Exception e)
                {

                }
                //RutaRepo.deleteRutaWithId(getActivity(),ID_RUTA);
                    Toast.makeText(getActivity().getBaseContext(),
                            "Se elimino: "+"\n"
                                    + NOMBRE
                            ,Toast.LENGTH_SHORT).show();

                ft = Globals.ft.beginTransaction();
                ft.replace(R.id.content_frame, new PantallaInicio());
                ft.commit();

                break;
            case R.id.button_cancelar_eliminar_ruta:
                //Toast.makeText(getActivity().getBaseContext(),"Se cancelo: "+"\n"+ NOMBRE+"\n",Toast.LENGTH_SHORT).show();
//                ft = Globals.ft.beginTransaction();
//                Fragment fr = new MostrarRuta();
//                Bundle bundle = new Bundle();
//                bundle.putInt("id_ruta", ID_RUTA.intValue());
//                fr.setArguments(bundle);
//                ft.replace(R.id.content_frame, fr);
//                ft.commit();
                Bundle bundle = new Bundle();
                bundle.putInt("id_ruta", ID_RUTA.intValue());

                Fragment frag = new MostrarRuta();
                frag.setArguments(bundle);

                ft = Globals.ft.beginTransaction();
                ft.replace(R.id.content_frame, frag);
                ft.commit();
                break;
            default:
                return;
        }
    }

}