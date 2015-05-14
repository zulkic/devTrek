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
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;

import greendao.Coordenada;
import repositorios.CoordenadaRepo;

public class CrearRuta extends Fragment implements LocationListener, View.OnClickListener{
    View rootView;
    private MapView osm;
    private MapController mc;
    private LocationManager locationManager;
    private PathOverlay po;
    private Boolean encendido= false;
    private Integer contador = 1;
    private Integer id_ruta = 1;

    private Location localicacionA = new Location("punto A");
    private Location localicacionB = new Location("punto B");
    private Boolean primerLocalicacion = true;
    private float distancia;

    Long i, f;
    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    // GPSTracker class
    GPS gps;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //rootView = inflater.inflate(R.layout.fragment_crear_ruta);

        gps = new GPS(getActivity());

        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        View view = inflater.inflate(R.layout.fragment_crear_ruta, container, false);
        ImageButton botonGps = (ImageButton) view.findViewById(R.id.imageButtonGPS);
        Button inicio = (Button) view.findViewById(R.id.button_start);
        Button fin = (Button) view.findViewById(R.id.button_end);
        botonGps.setOnClickListener(this);
        inicio.setOnClickListener(this);
        fin.setOnClickListener(this);

        osm = (MapView) view.findViewById(R.id.mapview);
        osm.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);
        mc = (MapController) osm.getController();
        mc.setZoom(15);
        initPathOverlay();
        //GeoPoint center = new GeoPoint(-34.15691, -70.75072);
        GeoPoint center = new GeoPoint(latitude, longitude);
        mc.animateTo(center);
        //addMarket(center);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);


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
        if(encendido)
        {
            osm.getOverlays().add(addPointsLine(center));
            mc.animateTo(center);
        }
        osm.getOverlays().add(po);
        osm.getOverlays().add(marker);
        osm.invalidate();
    }

    public void initPathOverlay(){
        po = new PathOverlay(Color.CYAN,getActivity());
        po.getPaint().setStyle(Paint.Style.STROKE);
        po.getPaint().setStrokeWidth(5);
        //Paint p = new Paint();
        //p.setColor(Color.RED);
        //p.setStyle(Paint.Style.STROKE);
        //p.setStrokeWidth(5);
        //po.setPaint(p);

    }

    public PathOverlay addPointsLine(GeoPoint gp){
        po.addPoint(gp);
        return po;
    }

    public void grabarRecorrido()
    {
        //dentro de la clase gps esta la funcion para apagarlo asiq esta puede q no sea necesaria
        initPathOverlay();
        encendido = true;
    }
    public void apagarRecorrido()
    {
        encendido = false;
        try{
            for(Coordenada coordenada: CoordenadaRepo.getAllCoordenadas(getActivity()))
            {
                    Log.i("coordenada: ", coordenada.getId().toString() + " " + coordenada.getLatitud().toString() + " " + coordenada.getLongitud() + " " + coordenada.getAltitud());

            }
            contador=1;
            id_ruta++;
        }
        catch (Exception e)
        {
            Log.i("Error fin: ", e.toString());
        }
    }

    public void activarGps() {
        if(gps.canGetLocation()){
            GeoPoint punto = new GeoPoint(gps.getLatitude(),gps.getLongitude());
            mc.animateTo(punto);
        }else{
            gps.showSettingsAlert();
        }
    }

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

        //Log.i(">>><<<<<", "---------=========ERROR==========------------");
        gps.setLatitude(location.getLatitude());
        gps.setLongitude(location.getLongitude());
        gps.setAltitude(location.getAltitude());
        GeoPoint punto = new GeoPoint(location.getLatitude(),location.getLongitude());
        //mc.animateTo(punto);
        addMarket(punto,encendido);
        try {

            if(primerLocalicacion){
                localicacionA.setLatitude(location.getLatitude());
                localicacionA.setLongitude(location.getLongitude());
                distancia =0;
                primerLocalicacion=false;
            }
            else{
                localicacionB.setLatitude(location.getLatitude());
                localicacionB.setLongitude(location.getLongitude());

                distancia = distancia + localicacionA.distanceTo(localicacionB);

                localicacionA.setLatitude(location.getLatitude());
                localicacionA.setLongitude(location.getLongitude());
            }
            Coordenada nueva_coordenada = new Coordenada();
            nueva_coordenada.setId((long) contador);
            nueva_coordenada.setLatitud((double) punto.getLatitude());
            nueva_coordenada.setLongitud((double) punto.getLongitude());
            nueva_coordenada.setAltitud((int) punto.getAltitude());
            nueva_coordenada.setId_ruta(id_ruta);
            contador++;
            CoordenadaRepo.insertOrUpdate(getActivity(), nueva_coordenada);
        }
        catch (Exception e)
        {
            Log.i("Error inicio: ", e.toString());
        }

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
        Fragment newFragment=null;
        Date c = null;
        switch (v.getId()){
            case R.id.button_start:
                c = new Date();
                System.out.println("getTimeInicio() => "+c.getTime());
                i = c.getTime();
                //System.out.println("getTimeInicio "+i);
                String tiempoInicioRecorrido= df.format(i);
                System.out.println("getTimeInicio "+tiempoInicioRecorrido);
                grabarRecorrido();
                break;
            case R.id.button_end :
                if(encendido){
                    c = new Date();
                    System.out.println("getTimeFin() => "+c.getTime());
                    f = c.getTime();
                    String tiempoFinRecorrido= df.format(f);
                    System.out.println("getTimeFin "+tiempoFinRecorrido);

                    f=f-i;
                    String tiempoTotalRecorrido= df.format(f);
                    System.out.println("distancia ----------->"+distancia);

                    apagarRecorrido();

                    newFragment = new DetallesRuta().newInstance(tiempoTotalRecorrido, distancia);
                    //newFragment.setTiempoTotal(tiempoTotalRecorrido);
                    FragmentManager fm1 = getFragmentManager();
                    FragmentTransaction ft1 = fm1.beginTransaction();
                    ft1.replace(R.id.container, newFragment)
                            .addToBackStack(null)
                            .commit();

                }
                else{
                    Toast.makeText(v.getContext(), "Primero inicie un Ruta", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageButtonGPS:
                activarGps();
                break;
        }
    }
}
