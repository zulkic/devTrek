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

import JSON.Obstaculos_Ruta;
import greendao.Obstaculo;
import greendao.Ruta;

public class FIRObstaculos extends SherlockFragment implements RefreshListener{

    private ListView listView;
    private Ruta ruta;
    private Long id_ruta;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;
    private ArrayList<Obstaculo> obstaculos;
    private ArrayList<Obstaculo> aux;
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
        Obstaculos_Ruta tarea_get_obstaculos = new Obstaculos_Ruta(Globals.ini_rec.getId().intValue(),getActivity());
        try {
            this.obstaculos = tarea_get_obstaculos.execute().get();
            this.aux = new ArrayList<>(this.obstaculos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Obstaculos: ", "me han llamado");
        view = inflater.inflate(R.layout.fragment_detalleindicador, null);

        // Set the adapter
        listView = (ListView) view.findViewById(android.R.id.list);
        if(!Globals.inicio_fin) {
            Log.i("Obstaculos: ", "debo invertir la lista");
            Collections.reverse(this.obstaculos);
        }
        else
        {
            this.obstaculos = (ArrayList<Obstaculo>) this.aux.clone();
        }
        if (this.obstaculos.isEmpty()) {

        } else {
            this.listView.setAdapter(new DetalleIndicadorItemObs(Globals.context, this.obstaculos));
        }
        return view;
    }

    @Override
    public void fragmentBecameVisible() {

    }
}