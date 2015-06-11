package com.mapas.franciscojavier.trekkingroute;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import JSON.Eliminar_Ruta;


public class DetallesEliminarRuta extends Fragment implements View.OnClickListener{

    int LARGO_NOMBRE_RUTA=1;
    private static Long ID_RUTA;
    private static String NOMBRE="nada", TIEMPO="nada", TIPO="nada", DESCRIPCION="nada";
    private static float KMS=0;


    public DetallesEliminarRuta() {
        // Required empty public constructor
    }
    public static DetallesEliminarRuta newInstance(Long idRuta, String nombre, String tiempo,
                                                   float kms,String tipo, String descripcion) {
        DetallesEliminarRuta fragment = new DetallesEliminarRuta();
        Bundle args = new Bundle();
        ID_RUTA=idRuta;
        NOMBRE=nombre;
        TIEMPO=tiempo;
        KMS=kms;
        TIPO=tipo;
        DESCRIPCION=descripcion;
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

        /**System.out.println(".setText(NOMBRE); "+NOMBRE);
        System.out.println(".setText(TIEMPO); "+TIEMPO);
        System.out.println(".setText(TIPO); "+TIPO);
        System.out.println(".setText(DESCRIPCION); "+DESCRIPCION);*/

        String km = Float.toString(KMS);

        //System.out.println(".setText(KMS); "+km);

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

        botonAceptar.setOnClickListener(this);
        botonCancelar.setOnClickListener(this);

        return v;
    }
    @Override
    public void onClick(View v) {
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
                //BUSCAR COMO RETROSEDER UNA VEZ Q SE ELIMINA----pendiete
                //getFragmentManager().popBackStack();
                Fragment newFragment = new RoutesFragment();
                //newFragment.setTiempoTotal(tiempoTotalRecorrido);
                FragmentManager fm1 = getFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                ft1.replace(R.id.container, newFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.button_cancelar_eliminar_ruta:
                //Toast.makeText(getActivity().getBaseContext(),"Se cancelo: "+"\n"+ NOMBRE+"\n",Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
                break;
            default:
                return;
        }
    }

}