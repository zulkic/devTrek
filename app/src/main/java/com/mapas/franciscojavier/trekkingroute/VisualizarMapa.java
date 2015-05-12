package com.mapas.franciscojavier.trekkingroute;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;

/**
 * Created by FranciscoJavier on 28-04-2015.
 */
public class VisualizarMapa extends Fragment implements LocationListener ,  View.OnClickListener{
    private MapView osm;
    private MapController mc;
    private LocationManager locationManager;
    private PathOverlay po;
    private Boolean encendido= false;
    // GPSTracker class
    GPS gps;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        gps = new GPS(getActivity());

        double latitude = -35.40472228;
        double longitude = -71.04858398;
        // Verificar si el GPS esta prendido
        /*if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            // \n para saltarse una linea
            //Toast.makeText(getApplicationContext(), "Tu ubicacion es - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();


        }else{
            // no tengo la ubicacion
            // GPS o Network no estan activos
            // Pregunto si quiero ir a los Ajustes
            gps.showSettingsAlert();
        }*/
        View view = inflater.inflate(R.layout.fragment_visualizar_mapa, container, false);

       // ImageButton botonGps = (ImageButton) view.findViewById(R.id.imageButtonGPS);
        //Button fin = (Button) view.findViewById(R.id.button_end);
       // botonGps.setOnClickListener(this);
        //inicio.setOnClickListener(this);
        //fin.setOnClickListener(this);

        osm = (MapView) view.findViewById(R.id.mapview);
        osm.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);
        mc = (MapController) osm.getController();
        mc.setZoom(10);
        //GeoPoint center = new GeoPoint(-34.15691, -70.75072);
        GeoPoint center = new GeoPoint(latitude, longitude);
        mc.animateTo(center);
        //addMarket(center);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,1000,10,this);


        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);



    }
    public void addMarket(GeoPoint center, Boolean encendido)
    {
        Marker marker = new Marker(osm);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        //marker.setIcon(getResources().getDrawable(R.drawable.zoom_in));

        osm.getOverlays().clear();
        osm.getOverlays().add(marker);
        osm.invalidate();
    }




    /*public void activarGps() {
        if (gps.canGetLocation()) {
            gps.showSettingsAlert();
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {

        GeoPoint punto = new GeoPoint(location.getLatitude(),location.getLongitude());
        //mc.animateTo(punto);
        addMarket(punto,encendido);

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

    @Override
    public void onClick(View v) {
        /*switch (v.getId()){
            case R.id.imageButtonGPS:
                activarGps();
                break;
        }*/
    }
}
