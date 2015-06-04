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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.PathOverlay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import greendao.Coordenada;
import greendao.Punto_interes;
import greendao.Tipo_punto_interes;
import repositorios.CoordenadaRepo;
import repositorios.Tipo_Puntos_InteresRepo;

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
    private ArrayList<Tipo_punto_interes> tipo_puntos = new ArrayList<>();
    private ArrayList<Coordenada> coordenadas;
    private GridView lv;
    private List<Overlay> puntosDeInteres;
    private Location localicacionA = new Location("punto A");
    private Location localicacionB = new Location("punto B");
    private Boolean primerLocalicacion = true;
    private float distancia;
    private Marker aux;
    Long i, f;
    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    // GPSTracker class
    GPS gps;
    private GeoPoint punto;

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
        puntosDeInteres = osm.getOverlays();
        //GeoPoint center = new GeoPoint(-34.15691, -70.75072);
        GeoPoint center = new GeoPoint(latitude, longitude);
        mc.animateTo(center);
        //addMarket(center);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        for(Tipo_punto_interes tipo_punto_interes : Tipo_Puntos_InteresRepo.getAllTipos_Puntos_Interes(getActivity()))
//        {
//            tipo_puntos.add(tipo_punto_interes);
//        }

        //setContentView(R.layout.activity_main);



    }
    public void addMarket(GeoPoint center, Boolean encendido)
    {
        Marker marker = new Marker(osm);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        //marker.setIcon(getResources().getDrawable(R.drawable.zoom_in));
        if(osm.getOverlays().size()>0 && aux!=null)
            osm.getOverlays().remove(aux);
        if(encendido)
        {
            osm.getOverlays().add(addPointsLine(center));
            mc.animateTo(center);
        }
        osm.getOverlays().add(po);
        osm.getOverlays().add(marker);
        osm.invalidate();
        aux = marker;
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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        final AlertDialog alert = alertDialog.create();
        alert.setIcon(R.drawable.ic_bicicleta);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.popup_crear_indicadores, null);
        alert.setView(convertView);
        alert.setTitle("Indicadores");
        this.lv = (GridView) convertView.findViewById(R.id.lista_item);
        this.lv.setAdapter(new ItemIndicador(getActivity(), tipo_puntos));
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
                Toast.makeText(getActivity(), "Accediendo a: "+ tipo_puntos.get(position).getNombre(), Toast.LENGTH_SHORT).show();
                addPoiOverlay(new GeoPoint(punto.getLatitude(), punto.getLongitude()), tipo_puntos.get(position).getNombre(),tipo_puntos.get(position).getNombre_icono(),
                        tipo_puntos.get(position).getId());
                alert.dismiss();
            }
        });
        if(encendido)
            alert.show();
        else
            Toast.makeText(getActivity(), "Primero inicie un Ruta", Toast.LENGTH_SHORT).show();

    }
    private void addPoiOverlay(GeoPoint gp, String titulo,String icono,Long id_tipo) {
        int resID = getActivity().getResources().getIdentifier(icono.trim(), "drawable", getActivity().getPackageName());
        Drawable drawable= this.getResources().getDrawable(resID);
        ResourceProxy rp = new ResourceProxyImpl(getActivity());
        Indicador in = new Indicador(drawable,rp,getActivity(),titulo,"descripcion",gp);
        Punto_interes pi = new Punto_interes();
        pi.setDescripcion("opcional...");
        pi.setId_tipo_punto_interes((int)(long) id_tipo);
        pi.setLatitud(gp.getLatitude());
        pi.setLongitud(gp.getLongitude());
        puntos.add(pi);
        puntosDeInteres.add(in);
        //osm.invalidate();
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
        this.punto = new GeoPoint(location.getLatitude(), location.getLongitude());
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
                if(encendido){
                    Toast.makeText(v.getContext(), "La ruta ya comenzo a grabarse", Toast.LENGTH_SHORT).show();
                }
                else{
                    c = new Date();
                    System.out.println("getTimeInicio() => "+c.getTime());
                    i = c.getTime();
                    //System.out.println("getTimeInicio "+i);
                    String tiempoInicioRecorrido= df.format(i);
                    System.out.println("getTimeInicio "+tiempoInicioRecorrido);
                    grabarRecorrido();
                }
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

                    newFragment = new DetallesCrearRuta().newInstance(tiempoTotalRecorrido, distancia, this.coordenadas, this.puntos);
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
