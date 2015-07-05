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

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import JSON.Obstaculos_Ruta;
import JSON.Puntos_Interes_Ruta;
import greendao.Indicador;
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
    private ArrayList<Indicador> puntos;
    private ArrayList<Indicador> aux;
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
        this.puntos = new ArrayList<>();
        Puntos_Interes_Ruta tarea_get_puntos = new Puntos_Interes_Ruta(Globals.ini_rec.getId().intValue(),getActivity());
        Obstaculos_Ruta tarea_get_obstaculos = new Obstaculos_Ruta(Globals.ini_rec.getId().intValue(),getActivity());
        try {
            for(Indicador indicador : tarea_get_puntos.execute().get())
            {
                this.puntos.add(indicador);
            }
            for(Indicador indicador : tarea_get_obstaculos.execute().get())
            {
                this.puntos.add(indicador);
            }
            ordenarPuntos();
            this.aux = new ArrayList<>(puntos);
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
            Collections.reverse(this.puntos);
        }
        else
        {
            this.puntos = (ArrayList<Indicador>) aux.clone();
        }

        if (this.puntos.isEmpty()) {

        } else {
            this.listView.setAdapter(new DetalleIndicadorItem(Globals.context, this.puntos));
        }
        return view;
    }

    public void ordenarPuntos() {

        GeoPoint gps = new GeoPoint(Globals.coordenadas_inic_rec.get(0).getLatitud(),Globals.coordenadas_inic_rec.get(0).getLongitud());
        Double distanciaEnMetros;

        ArrayList<Double> pos = new ArrayList<>();
        ArrayList<Indicador> ordenados = new ArrayList<>();

        for(Indicador indicador : this.puntos)
        {
            GeoPoint puntoIn = new GeoPoint(indicador.getLatitud(), indicador.getLongitud());
            distanciaEnMetros = 1.0*gps.distanceTo(puntoIn)/1000;
            Log.i("distancia: ", distanciaEnMetros.toString());
            pos.add(distanciaEnMetros);
        }

        ArrayList<Double> aux_pos = new ArrayList<>(pos);
        Collections.sort(pos);

        for(Double i : pos)
        {
            int y = 0;
            for(Double j : aux_pos){
                if(j == i )
                {
                    ordenados.add(this.puntos.get(y));
                    break;
                }
                y++;
            }
        }
        this.puntos = (ArrayList<Indicador>) ordenados.clone();
    }

    @Override
    public void fragmentBecameVisible() {
        if(!Globals.inicio_fin) {
            Collections.reverse(this.puntos);
        }
        else
        {
            this.puntos = (ArrayList<Indicador>) aux.clone();
        }
        if (this.puntos.isEmpty()) {

        } else {
            this.listView.setAdapter(new DetalleIndicadorItem(Globals.context, this.puntos));
        }
    }
}