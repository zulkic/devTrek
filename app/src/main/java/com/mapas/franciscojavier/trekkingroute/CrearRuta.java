package com.mapas.franciscojavier.trekkingroute;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import greendao.Coordenada;
import greendao.Tipo_Indicador;
import greendao.Tipo_obstaculo;
import greendao.Tipo_punto_interes;
import repositorios.CoordenadaRepo;
import repositorios.Tipo_ObstaculoRepo;
import repositorios.Tipo_Puntos_InteresRepo;

public class CrearRuta extends SherlockFragment implements LocationListener, AdapterView.OnClickListener{
    private MapView osm;
    private MapController mc;
    private LocationManager locationManager;
    private PathOverlay po;
    private Boolean encendido= false;
    private Integer contador = 1;
    private Integer id_ruta = 1;
    private ArrayList<Tipo_Indicador> tipo_puntos = new ArrayList<>();
    private ArrayList<Tipo_Indicador> tipo_obstaculos = new ArrayList<>();
    private ArrayList<Coordenada> coordenadas;
    private GridView lv;
    private List<Overlay> indicadores;
    private Location localicacionA = new Location("punto A");
    private Location localicacionB = new Location("punto B");
    private Boolean primerLocalicacion = true;
    private float distancia;
    private Marker aux;
    private Indicadores indicador;
    private Boolean enabled = true;
    private Button tBtnIniFin;
    int i, f;
    private GeoPoint punto;

    //Commit para el moya
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 3, this);
        double latitude;
        double longitude;
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            else{
                latitude = -34.98605794;
                longitude = -71.24138117;
            }
        }
        else{
            //posicion curico
            latitude = -34.98605794;
            longitude = -71.24138117;
        }

        View view = inflater.inflate(R.layout.fragment_crear_ruta, container, false);
        ImageButton botonGps = (ImageButton) view.findViewById(R.id.imageButtonGPS);
        ImageButton botonIndicador = (ImageButton) view.findViewById(R.id.imageButton_Indicadores);
        ImageButton botonObstaculo = (ImageButton) view.findViewById(R.id.imageButton_Obstaculos);
        tBtnIniFin = (Button) view.findViewById(R.id.tBtnIniFin);
        tBtnIniFin.setText("Inicio");
        botonGps.setOnClickListener(this);
        botonIndicador.setOnClickListener(this);
        botonObstaculo.setOnClickListener(this);

        tBtnIniFin.setOnClickListener(this);

        osm = (MapView) view.findViewById(R.id.mapview);
        osm.setTileSource(Globals.MAPQUESTOSM);
        //osm.setUseDataConnection(false);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);
        mc = (MapController) osm.getController();
        mc.setZoom(14);
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(getActivity());
        this.osm.getOverlays().add(myScaleBarOverlay);
        initPathOverlay();
        indicadores = osm.getOverlays();
        GeoPoint center = new GeoPoint(latitude,longitude);
        mc.animateTo(center);


        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ResourceProxy rp = new ResourceProxyImpl(getActivity());
        this.indicador = new Indicadores(this.getResources().getDrawable(R.drawable.abs__ab_share_pack_holo_light),rp,getActivity());
        for(Tipo_punto_interes tipo_punto_interes : Tipo_Puntos_InteresRepo.getAllTipos_Puntos_Interes(getActivity()))
        {
            tipo_puntos.add(tipo_punto_interes);
        }
        for(Tipo_obstaculo tipo_obstaculo : Tipo_ObstaculoRepo.getAllTipos_Obstaculos(getActivity()))
        {
            tipo_obstaculos.add(tipo_obstaculo);
        }
        //setContentView(R.layout.activity_main);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }
    public void addMarket(GeoPoint center, Boolean encendido)
    {
        Marker marker = new Marker(osm);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.ic_me));
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
        po = new PathOverlay(Color.MAGENTA,getActivity());
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
        Coordenada nueva_coordenada = new Coordenada();
        nueva_coordenada.setLatitud((double) punto.getLatitude());
        nueva_coordenada.setLongitud((double) punto.getLongitude());
        nueva_coordenada.setAltitud((int) punto.getAltitude());
        nueva_coordenada.setId_ruta(id_ruta);
        nueva_coordenada.setPosicion(contador);
        this.coordenadas.add(nueva_coordenada);
        this.contador = 2;
        encendido = true;
        addMarket(punto, encendido);
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

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                GeoPoint punto = new GeoPoint(location.getLatitude(), location.getLongitude());
                mc.animateTo(punto);
            }
            else{
                Toast.makeText(getActivity(), "Espere a tener señal Gps", Toast.LENGTH_SHORT).show();
            }
        }else{
            showSettingsAlert();
        }
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Titulo de la alerta
        alertDialog.setTitle("GPS apagado");

        // Mensaje
        alertDialog.setMessage("GPS no esta habilitado. Desea ir al menu de ajustes?");

        // El botón de Configuración
        alertDialog.setPositiveButton("Ajustes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(intent);
            }
        });

        // El botón de Cancelación
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Mostrando el mensaje de alerta
        alertDialog.show();
    }

    private void agregarIndicadorAPosicion() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setNegativeButton(R.string.button_cancelar_ruta,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog alert = alertDialog.create();
        alert.setIcon(R.drawable.ic_bicicleta);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.popup_crear_indicadores, null);
        alert.setView(convertView);
        alert.setTitle("Indicadores");
        this.lv = (GridView) convertView.findViewById(R.id.lista_item);
        this.lv.setAdapter(new ItemIndicador(getActivity(), tipo_puntos));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View view, int position, long arg) {
                // Loads the given URL
                //puntos.get(position).getId_tipo_punto_interes()
                alert.dismiss();
                if(!alert.isShowing())
                {
                    Tipo_punto_interes tipo_punto = (Tipo_punto_interes) tipo_puntos.get(position);
                    Toast.makeText(getActivity(), "Accediendo a: " + tipo_punto.getNombre(), Toast.LENGTH_SHORT).show();
                    addPoiOverlay(new GeoPoint(punto.getLatitude(), punto.getLongitude()), tipo_punto.getNombre(), tipo_punto.getNombre_icono(),
                            tipo_punto.getId());
                }

            }
        });
        if(encendido)
            alert.show();
        else
            Toast.makeText(getActivity(), "Primero inicie un Ruta", Toast.LENGTH_SHORT).show();

    }

    private void agregarObstaculoAPosicion() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog alert = alertDialog.create();
        alert.setIcon(R.drawable.ic_bicicleta);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.popup_crear_obstaculos, null);
        alert.setView(convertView);
        alert.setTitle("Obstaculos");
        this.lv = (GridView) convertView.findViewById(R.id.lista_item_obs);
        this.lv.setAdapter(new ItemIndicador(getActivity(), tipo_obstaculos));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapter, View view, int position, long arg) {
                Tipo_obstaculo tipo_obstaculo = (Tipo_obstaculo) tipo_obstaculos.get(position);
                Toast.makeText(getActivity(), "Accediendo a: " + tipo_obstaculo.getNombre(), Toast.LENGTH_SHORT).show();
                addPoiOverlayObs(new GeoPoint(punto.getLatitude(), punto.getLongitude()), tipo_obstaculo.getNombre(), tipo_obstaculo.getNombre_icono(),
                        tipo_obstaculo.getId());
                alert.dismiss();
            }
        });
        if(encendido)
            alert.show();
        else
            Toast.makeText(getActivity(), "Primero inicie un Ruta", Toast.LENGTH_SHORT).show();

    }

    private void addPoiOverlayObs(final GeoPoint gp,final String titulo,final String icono,final Long id_tipo) {
        final AlertDialog.Builder alertDialogo = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View convertView = (View) inflater.inflate(R.layout.popup_detalles_puntos, null);
        int resID = getActivity().getResources().getIdentifier(icono.trim(), "drawable", getActivity().getPackageName());
        final Drawable drawable= this.getResources().getDrawable(resID);
        alertDialogo.setNegativeButton(R.string.button_cancelar_ruta,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Accediendo a: ", Toast.LENGTH_SHORT).show();
                        indicador.createIndicadorObs(drawable, titulo,"Sin Descripción", gp, id_tipo);
                        indicadores.clear();
                        indicadores.add(indicador);
                    }
                });

        alertDialogo.setPositiveButton(R.string.button_aceptar_eliminar_ruta,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        EditText et = (EditText) convertView.findViewById(R.id.editText_popup_descripcion);
                        if(et.getText().toString()!=null && !et.getText().toString().isEmpty())
                            indicador.createIndicadorObs(drawable, titulo, et.getText().toString(), gp, id_tipo);
                        else
                            indicador.createIndicadorObs(drawable, titulo, "Sin Descripción", gp, id_tipo);
                        indicadores.clear();
                        indicadores.add(indicador);
                    }
                });
        final AlertDialog alerta = alertDialogo.create();
        alerta.setIcon(drawable);
        alerta.setView(convertView);
        alerta.setTitle(titulo);
        alerta.show();
    }


    private void addPoiOverlay(final GeoPoint gp, final String titulo,final String icono,final Long id_tipo) {
        final AlertDialog.Builder alertDialogo = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View convertView = (View) inflater.inflate(R.layout.popup_detalles_puntos, null);
        int resID = getActivity().getResources().getIdentifier(icono.trim(), "drawable", getActivity().getPackageName());
        final Drawable drawable= this.getResources().getDrawable(resID);
        alertDialogo.setNegativeButton(R.string.button_cancelar_ruta,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Accediendo a: ", Toast.LENGTH_SHORT).show();
                        indicador.createIndicador(drawable, titulo,"Sin Descripción", gp, id_tipo);
                        indicadores.clear();
                        indicadores.add(indicador);
                    }
                });

        alertDialogo.setPositiveButton(R.string.button_aceptar_eliminar_ruta,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        EditText et = (EditText) convertView.findViewById(R.id.editText_popup_descripcion);
                        if(et.getText().toString()!=null && !et.getText().toString().isEmpty())
                            indicador.createIndicador(drawable, titulo, et.getText().toString(), gp, id_tipo);
                        else
                            indicador.createIndicador(drawable, titulo,"Sin Descripción", gp, id_tipo);
                        indicadores.clear();
                        indicadores.add(indicador);
                    }
                });
        final AlertDialog alerta = alertDialogo.create();
        alerta.setIcon(drawable);
        alerta.setView(convertView);
        alerta.setTitle(titulo);
        alerta.show();


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
        /*gps.setLatitude(location.getLatitude());
        gps.setLongitude(location.getLongitude());
        gps.setAltitude(location.getAltitude());*/
        this.punto = new GeoPoint(location.getLatitude(), location.getLongitude(), location.getAltitude());
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
    public void onPause(){
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onClick(View v) {
        Fragment newFragment=null;
        Date c = null;
        Calendar now = Calendar.getInstance();
        switch (v.getId()){
            case R.id.imageButtonGPS:
                activarGps();
                break;
            case R.id.imageButton_Indicadores:
                agregarIndicadorAPosicion();
                break;
            case R.id.imageButton_Obstaculos:
                agregarObstaculoAPosicion();
                break;
            case R.id.tBtnIniFin:
                if(punto != null) {
                    if (enabled) {
                        enabled = false;
                        tBtnIniFin.setText("Fin");
                        int hour = now.get(Calendar.HOUR_OF_DAY);
                        int minute = now.get(Calendar.MINUTE);
                        int second = now.get(Calendar.SECOND);
                        System.out.printf("INICIO %02d:%02d:%02d", hour, minute, second);
                        i = hour * 3600 + minute * 60 + second;
                        System.out.println("getTimeInicio " + i);

                        grabarRecorrido();
                    } else {
                        enabled = true;
                        tBtnIniFin.setText("Inicio");
                        int hour = now.get(Calendar.HOUR_OF_DAY);
                        int minute = now.get(Calendar.MINUTE);
                        int second = now.get(Calendar.SECOND);
                        System.out.printf("FIN %02d:%02d:%02d", hour, minute, second);
                        f = hour * 3600 + minute * 60 + second;
                        System.out.println("getTimeInicio " + f);

                    f=f-i;
                    hour=f/3600;
                    minute=(f-(3600*hour))/60;
                    second=f-((hour*3600)+(minute*60));
                    System.out.println(hour+"h "+minute+"m "+second+"s");
                    String h,m,s;
                    if(hour<10){
                        h="0"+hour+":";
                    }
                    else{
                        h=hour+":";
                    }
                    if(minute<10){
                        m="0"+minute+":";
                    }
                    else{
                        m=minute+":";
                    }
                    if(second<10){
                        s="0"+second+"";
                    }
                    else{
                        s=second+"";
                    }

                    //String tiempoTotalRecorrido= df.format(f);
                    String tiempoTotalRecorrido= h+m+s;
                    //System.out.println("tiempoTotalRecorrido "+tiempoTotalRecorrido);
                    System.out.println("distancia ----------->"+distancia);
                    distancia = distancia/1000;
                    System.out.println("distancia nueva------>"+distancia);

                        apagarRecorrido();

                        FragmentTransaction ft = Globals.ft.beginTransaction();
                        ft.replace(R.id.content_frame, new DetallesCrearRuta().newInstance(tiempoTotalRecorrido, distancia, this.coordenadas, this.indicador.getPuntos(), this.indicador.getObstaculos()));
                        ft.addToBackStack("Detalle Crear Ruta");
                        ft.commit();
                    }
                }
                else{
                    Toast.makeText(getActivity(),"Espere el GPS",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
