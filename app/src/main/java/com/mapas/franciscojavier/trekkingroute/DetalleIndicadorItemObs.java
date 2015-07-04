package com.mapas.franciscojavier.trekkingroute;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import greendao.Obstaculo;
import greendao.Tipo_obstaculo;
import repositorios.Tipo_ObstaculoRepo;

/**
 * Created by FranciscoJavier on 17-06-2015.
 */
public class DetalleIndicadorItemObs extends BaseAdapter implements LocationListener {

    private Context context;
    private List<Obstaculo> items;
    private ArrayList<Tipo_obstaculo> tp = new ArrayList<>();
    private GeoPoint gps;
    private LocationManager locationManager;

    public DetalleIndicadorItemObs(Context context, List<Obstaculo> items) {
        this.context = context;
        this.items = items;
        for(Tipo_obstaculo tipo_obstaculo : Tipo_ObstaculoRepo.getAllTipos_Obstaculos(context))
        {
            this.tp.add(tipo_obstaculo);
        }
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        this.locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.indicador_detalles_row, parent, false);
        }

        // Set data into the view.
        ImageView imgPunto = (ImageView) rowView.findViewById(R.id.imageView_indicador);
        TextView titulo = (TextView) rowView.findViewById(R.id.textView_nombre_indicador);
        TextView descripcion = (TextView) rowView.findViewById(R.id.textView_descripcion);
        TextView distancia = (TextView) rowView.findViewById(R.id.textView_distancia_al_punto);
        //imgPunto.setImageDrawable(context.getResources().getDrawable(R.drawable.zoom_in));
        Obstaculo punto = this.items.get(position);
        int i;
        for(i=0;punto.getId_tipo_obstaculo()!=(int)(long)this.tp.get(i).getId();i++);
        int resID = context.getResources().getIdentifier(this.tp.get(i).getNombre_icono().trim(),"drawable",context.getPackageName());
        imgPunto.setImageResource(resID);
        titulo.setText(this.tp.get(i).getNombre());
        descripcion.setText(punto.getDescripcion());
        GeoPoint puntoIn = new GeoPoint(punto.getLatitud(),punto.getLongitud());
        if(this.gps==null)
        {
            this.gps = new GeoPoint(-34.978,-71.2529);
        }
        double distanciaEnMetros = 1.0*this.gps.distanceTo(puntoIn)/1000;

        distancia.setText("Esta a: "+ String.format("%.2f",distanciaEnMetros)+" Kms");
        if(position%2==0)
        {
            rowView.setBackgroundColor(context.getResources().getColor(R.color.background_pair_list));
            titulo.setTextColor(context.getResources().getColor(R.color.text_pair_list));
            distancia.setTextColor(context.getResources().getColor(R.color.text_pair_list));
            descripcion.setTextColor(context.getResources().getColor(R.color.text_pair_list));
        }
        else{
            rowView.setBackgroundColor(context.getResources().getColor(R.color.background_odd_list));
            titulo.setTextColor(context.getResources().getColor(R.color.white));
            distancia.setTextColor(context.getResources().getColor(R.color.white));
            descripcion.setTextColor(context.getResources().getColor(R.color.white));
        }

        return rowView;
    }


    @Override
    public void onLocationChanged(Location location) {
        this.gps = new GeoPoint(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}