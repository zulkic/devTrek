package com.mapas.franciscojavier.trekkingroute;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;
import com.mapas.franciscojavier.trekkingroute.Utility.RefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import JSON.Puntos_Interes_Ruta;
import greendao.Punto_interes;
import greendao.Ruta;

public class FIRDetalles extends SherlockFragment implements RefreshListener{

    private ListView listView;
    private Ruta ruta;
    private Long id_ruta;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;
    private ArrayList<Punto_interes> lista_puntos;
    private ArrayList<Punto_interes> aux;
    private View view;

    // TODO: Rename and change types of parameters
    public static DetalleIndicadorFragment newInstance(String param1, String param2) {
        DetalleIndicadorFragment fragment = new DetalleIndicadorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Puntos_Interes_Ruta tarea_get_puntos = new Puntos_Interes_Ruta(Globals.ini_rec.getId().intValue(),getActivity());
        try {
            this.lista_puntos = tarea_get_puntos.execute().get();
            this.aux = new ArrayList<>(lista_puntos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Detalles: ", "me han llamado");
        view = inflater.inflate(R.layout.fragment_detalleindicador, null);

        // Set the adapter
        listView = (ListView) view.findViewById(android.R.id.list);
        if(!Globals.inicio_fin) {
            Log.i("Puntos: ", "debo invertir la lista");
            Collections.reverse(this.lista_puntos);
        }
        else
        {
            this.lista_puntos = (ArrayList<Punto_interes>) aux.clone();
        }
        if (this.lista_puntos.isEmpty()) {

        } else {
            this.listView.setAdapter(new DetalleIndicadorItem(Globals.context, this.lista_puntos));
        }
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Puntos_Interes_Ruta tarea_get_puntos = new Puntos_Interes_Ruta(Globals.ini_rec.getId().intValue(),getActivity());
        try {
            this.lista_puntos = tarea_get_puntos.execute().get();
            this.aux = new ArrayList<>(lista_puntos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(!Globals.inicio_fin) {
            Log.i("Puntos: ", "debo invertir la lista");
            Collections.reverse(this.lista_puntos);
        }
        else
        {
            this.lista_puntos = (ArrayList<Punto_interes>) aux.clone();
        }
        if (this.lista_puntos.isEmpty()) {

        } else {
            this.listView.setAdapter(new DetalleIndicadorItem(Globals.context, this.lista_puntos));
        }
    }

    @Override
    public void fragmentBecameVisible() {
        Log.i("Aca debo:", " cambiar los datos");
        if(!Globals.inicio_fin) {
            Log.i("Puntos: ", "debo invertir la lista");
            Collections.reverse(this.lista_puntos);
        }
        else
        {
            this.lista_puntos = (ArrayList<Punto_interes>) aux.clone();
        }
        if (this.lista_puntos.isEmpty()) {

        } else {
            this.listView.setAdapter(new DetalleIndicadorItem(Globals.context, this.lista_puntos));
        }
    }
}