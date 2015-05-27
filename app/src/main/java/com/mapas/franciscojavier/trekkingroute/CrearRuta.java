package com.mapas.franciscojavier.trekkingroute;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import greendao.Coordenada;
import greendao.Punto_interes;
import repositorios.CoordenadaRepo;

public class CrearRuta extends Fragment implements LocationListener, AdapterView.OnClickListener{
    View rootView;
    private MapView osm;
    private MapController mc;
    private LocationManager locationManager;
    private PathOverlay po;
    private Boolean encendido= false;
    private Integer contador = 1;
    private Integer id_ruta = 1;
    private ArrayList<Punto_interes> puntos = new ArrayList<>();
    private ArrayList<Coordenada> coordenadas;
    private GridView lv;

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
        ImageButton botonIndicador = (ImageButton) view.findViewById(R.id.imageButton_Indicadores);
        Button inicio = (Button) view.findViewById(R.id.button_start);
        Button fin = (Button) view.findViewById(R.id.button_end);
        botonGps.setOnClickListener(this);
        botonIndicador.setOnClickListener(this);
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
        Punto_interes punto1 = new Punto_interes(Long.parseLong("1"),"Punto 1",1,1);
        Punto_interes punto2 = new Punto_interes(Long.parseLong("2"),"Punto 2",2,1);
        Punto_interes punto3 = new Punto_interes(Long.parseLong("3"),"Punto 3",3,1);
        Punto_interes punto4 = new Punto_interes(Long.parseLong("4"),"Punto 4",4,1);
        Punto_interes punto5 = new Punto_interes(Long.parseLong("5"),"Punto 5",5,1);
        Punto_interes punto6 = new Punto_interes(Long.parseLong("6"),"Punto 6",6,1);
        Punto_interes punto7 = new Punto_interes(Long.parseLong("7"),"Punto 7",7,1);
        Punto_interes punto8 = new Punto_interes(Long.parseLong("8"),"Punto 8",8,1);
        Punto_interes punto9 = new Punto_interes(Long.parseLong("9"),"Punto 9",9,1);
        Punto_interes punto10 = new Punto_interes(Long.parseLong("10"),"Punto 10",10,1);
        puntos.add(punto1);
        puntos.add(punto2);
        puntos.add(punto3);
        puntos.add(punto4);
        puntos.add(punto5);
        puntos.add(punto6);
        puntos.add(punto7);
        puntos.add(punto8);
        puntos.add(punto9);
        puntos.add(punto10);
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
        this.coordenadas = new ArrayList<>();
        this.contador = 1;
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
    private void agregarIndicadorAPosicion() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setIcon(R.drawable.ic_bicicleta);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.popup_crear_indicadores, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Indicadores");
        this.lv = (GridView) convertView.findViewById(R.id.lista_item);
        this.lv.setAdapter(new ItemIndicador(getActivity(), puntos));
        alertDialog.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View view, int position, long arg) {
                // Loads the given URL
                //puntos.get(position).getId_tipo_punto_interes()
                Toast.makeText(getActivity(), "Accediendo a: "+ puntos.get(position).getDescripcion(), Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }
    private void addPoiOverlay() {

        List<Overlay> puntosDeInteres = osm.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.location_marker);
        Indicador itemizedoverlay = new Indicador(drawable,new ResourceProxyImpl(getActivity()),getActivity());
        Indicador aguas = new Indicador(this.getResources().getDrawable(R.drawable.ic_parque),new ResourceProxyImpl(getActivity()),getActivity());
        Indicador parque = new Indicador(this.getResources().getDrawable(R.drawable.ic_agua),new ResourceProxyImpl(getActivity()),getActivity());
        OverlayItem poi1 = new OverlayItem("Inicio ruta", "ruta inicio prueba 1",new GeoPoint (-34.98720064,-71.24133825));
        OverlayItem poi2 = new OverlayItem("Fin ruta", "termino de ruta",new GeoPoint (-34.9867963, -71.23584509));
        OverlayItem poi3 = new OverlayItem("Parque", "Parque en Argomedo con Carmen",new GeoPoint (-34.98709516, -71.2385273));
        OverlayItem poi4 = new OverlayItem("Agua Potable", "Agua en Membrillar con Carmen",new GeoPoint (-34.98698968, -71.23715401));
        itemizedoverlay.addOverlay(poi1);
        itemizedoverlay.addOverlay(poi2);
        parque.addOverlay(poi4);
        aguas.addOverlay(poi3);
        puntosDeInteres.add(itemizedoverlay);
        puntosDeInteres.add(aguas);
        puntosDeInteres.add(parque);
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
        GeoPoint punto = new GeoPoint(location.getLatitude(), location.getLongitude());
        //mc.animateTo(punto);
        addMarket(punto, encendido);
        if(encendido) {
            try {

                if (primerLocalicacion) {
                    localicacionA.setLatitude(location.getLatitude());
                    localicacionA.setLongitude(location.getLongitude());
                    distancia = 0;
                    primerLocalicacion = false;
                } else {
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
                nueva_coordenada.setPosicion(contador);
                this.coordenadas.add(nueva_coordenada);
                contador++;
            } catch (Exception e) {
                //Log.i("Error inicio: ", e.toString());
            }
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

                    newFragment = new DetallesCrearRuta().newInstance(tiempoTotalRecorrido, distancia, this.coordenadas);
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
            case R.id.imageButton_Indicadores:
                agregarIndicadorAPosicion();
        }
    }
}
