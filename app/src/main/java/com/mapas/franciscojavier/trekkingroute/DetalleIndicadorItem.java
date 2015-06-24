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
import android.widget.ListView;
import android.widget.TextView;

import com.mapas.franciscojavier.trekkingroute.Indicador;
import com.mapas.franciscojavier.trekkingroute.R;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import greendao.Punto_interes;
import greendao.Ruta;
import greendao.Tipo_punto_interes;
import repositorios.Tipo_Puntos_InteresRepo;

/**
 * Created by FranciscoJavier on 17-06-2015.
 */
public class DetalleIndicadorItem extends BaseAdapter implements LocationListener {

    private Context context;
    private List<Punto_interes> items;
    private ArrayList<Tipo_punto_interes> tp = new ArrayList<>();
    private GeoPoint gps;
    private LocationManager locationManager;

    public DetalleIndicadorItem(Context context, List<Punto_interes> items) {
        this.context = context;
        this.items = items;
        for(Tipo_punto_interes tipo_punto_interes : Tipo_Puntos_InteresRepo.getAllTipos_Puntos_Interes(context))
        {
            this.tp.add(tipo_punto_interes);
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
        Punto_interes punto = this.items.get(position);
        int i;
        for(i=0;punto.getId_tipo_punto_interes()!=(int)(long)this.tp.get(i).getId();i++);
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

        /*TextView nombre = (TextView) rowView.findViewById(R.id.nombre_ruta);
        TextView distancia = (TextView) rowView.findViewById(R.id.kms_ruta);
        TextView tiempo = (TextView) rowView.findViewById(R.id.tiempo_ruta);
        TextView oficial = (TextView) rowView.findViewById(R.id.oficial_ruta);
        */
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