package com.mapas.franciscojavier.trekkingroute;

import android.content.Context;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;
import com.mapas.franciscojavier.trekkingroute.Utility.RefreshListener;

import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.util.ArrayList;
import java.util.List;

import JSON.Coordenadas_Ruta;
import JSON.Obstaculos_Ruta;
import JSON.Puntos_Interes_Ruta;
import greendao.Coordenada;
import greendao.Obstaculo;
import greendao.Punto_interes;
import greendao.Ruta;
import greendao.Tipo_obstaculo;
import greendao.Tipo_punto_interes;
import repositorios.CoordenadaRepo;
import repositorios.ObstaculoRepo;
import repositorios.Punto_interesRepo;
import repositorios.Tipo_ObstaculoRepo;
import repositorios.Tipo_Puntos_InteresRepo;

public class FIRMapa extends SherlockFragment implements LocationListener, AdapterView.OnClickListener, RefreshListener, SensorEventListener {

    private MapView osm;
    private MapController mc;
    private LocationManager locationManager;
    // GPSTracker class
    private Ruta ruta;
    private ArrayList<Coordenada> lista_coordenadas = new ArrayList<>();
    private ArrayList<Punto_interes> lista_puntos = new ArrayList<>();
    private ArrayList<Obstaculo> lista_obstaculos = new ArrayList<>();
    private List<Overlay> puntosDeInteres;
    private List<Overlay> obstaculos;
    private OverlayItem inicio;
    private OverlayItem fin;
    private Marker aux;
    private PathOverlay po;
    private GeoPoint punto;
    private View view;
    private Boolean enabled = true;

    //SENSOR
    private ImageView mPointer;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    // TODO Auto-generated method stub
        Log.i("Mapa: ", "me han llamado");
        ruta = Globals.ini_rec;
        double latitude;
        double longitude;
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        else{
            //posicion curico
            latitude = -34.98605794;
            longitude = -71.24138117;
        }


        view = inflater.inflate(R.layout.fir_layout_mapa, container, false);
        ImageButton botonGps = (ImageButton) view.findViewById(R.id.imageButtonGPS);
        ToggleButton BtnIniFin = (ToggleButton) view.findViewById(R.id.BtnIniFin);
        ToggleButton direction = (ToggleButton) view.findViewById(R.id.direction);
        direction.setOnClickListener(this);
        BtnIniFin.setOnClickListener(this);
        botonGps.setOnClickListener(this);

        osm = (MapView) view.findViewById(R.id.mapview);
        osm.setTileSource(Globals.MAPQUESTOSM);
        osm.setUseDataConnection(true);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);
        mc = (MapController) osm.getController();
        mc.setZoom(15);
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(getActivity());
        this.osm.getOverlays().add(myScaleBarOverlay);

        initPathOverlay();
        addLineOverlay();
        puntosDeInteres = osm.getOverlays();

        GeoPoint center = new GeoPoint(latitude, longitude);
        mc.animateTo(center);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPointer = (ImageView) view.findViewById(R.id.pointer);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    public void initPathOverlay(){
        po = new PathOverlay(Color.CYAN,getActivity());
        po.getPaint().setStyle(Paint.Style.STROKE);
        po.getPaint().setStrokeWidth(5);
    }

    private void addPolyOverlay() {
        // set custom polygon style
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(30);
        paint.setPathEffect(new CornerPathEffect(10));
        paint.setAntiAlias(true);

        // list of GeoPoint objects to be used to draw polygon
        List<GeoPoint> polyData = new ArrayList<GeoPoint>();
        polyData.add(new GeoPoint(-34.98489765,-71.240201));
        polyData.add(new GeoPoint(-34.98479217, -71.23884916));
        polyData.add(new GeoPoint(-34.98602278, -71.24007225));
        polyData.add(new GeoPoint(-34.98589972,-71.2386775));

        // apply polygon style & data and add to map
        Polygon polyOverlay = new Polygon(getActivity());
        polyOverlay.setPoints(polyData);
        osm.getOverlays().add(polyOverlay);
    }

    public void addMarket(GeoPoint center)
    {
        Marker marker = new Marker(osm);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.ic_me));
        if(osm.getOverlays().size()>0 && aux!=null)
            osm.getOverlays().remove(aux);
        osm.getOverlays().add(po);
        osm.getOverlays().add(marker);
        osm.invalidate();
        aux = marker;
    }

    public PathOverlay addPointsLine(GeoPoint gp){
        po.addPoint(gp);
        return po;
    }

    private void addLineOverlay() {
        // set custom line style
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);

        // list of GeoPoint objects to be used to draw line
        if(this.ruta.getSincronizada())
        {
            try
            {
                Coordenadas_Ruta tarea_get_coordenadas = new Coordenadas_Ruta(this.ruta.getId().intValue(),Globals.context);
                lista_coordenadas = tarea_get_coordenadas.execute().get();

                Puntos_Interes_Ruta tarea_get_puntos = new Puntos_Interes_Ruta(this.ruta.getId().intValue(),Globals.context);
                lista_puntos = tarea_get_puntos.execute().get();

                Obstaculos_Ruta tarea_get_obstaculos = new Obstaculos_Ruta(this.ruta.getId().intValue(),Globals.context);
                lista_obstaculos = tarea_get_obstaculos.execute().get();

            }
            catch (Exception e)
            {
                Log.i("Error: ", "Imposible obtener las coordenadas y puntos");
            }
        }
        else
        {
            for(Coordenada coordenada : CoordenadaRepo.coordenadas_ruta(Globals.context, this.ruta.getId()))
            {
                lista_coordenadas.add(coordenada);
            }

            for(Punto_interes punto_interes : Punto_interesRepo.punto_intereses_ruta(Globals.context, this.ruta.getId()))
            {
                lista_puntos.add(punto_interes);
            }

            for(Obstaculo obstaculo : ObstaculoRepo.obstaculos_ruta(Globals.context, this.ruta.getId()))
            {
                lista_obstaculos.add(obstaculo);
            }

            Log.i("coordenadas offline: ", "obtiene coordenadas offline");
        }

        if(lista_coordenadas.size() > 0)
        {
            List lineData = new ArrayList();
            this.inicio = new OverlayItem("Inicio " + ruta.getNombre(), ruta.getDescripcion() ,new GeoPoint (lista_coordenadas.get(0).getLatitud(),lista_coordenadas.get(0).getLongitud()));
            this.fin = new OverlayItem("Fin ruta " + ruta.getDescripcion(), "termino de ruta",new GeoPoint (lista_coordenadas.get(lista_coordenadas.size()-1).getLatitud(), lista_coordenadas.get(lista_coordenadas.size()-1).getLongitud()));
            for( Coordenada coordenada : lista_coordenadas)
            {
                lineData.add(new GeoPoint(coordenada.getLatitud(), coordenada.getLongitud()));
            }
            // apply line style & data and add to map

            PathOverlay lineOverlay = new PathOverlay(Color.MAGENTA,getActivity());
            lineOverlay.setPaint(paint);
            lineOverlay.addPoints(lineData);
            //lineOverlay.setData(lineData);
            GeoPoint point = new GeoPoint(lista_coordenadas.get(0).getLatitud(),lista_coordenadas.get(0).getLongitud());
            mc.setCenter(point);
            osm.getOverlays().add(lineOverlay);
            addPoiOverlay();
        }
        else
        {
            Log.i("error: ", "error al obtener los datos de la ruta");
        }

    }

    // add an itemized overlay to map
    private void addPoiOverlay() {

        List<Overlay> mapOverlays = osm.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.location_marker);
        Indicador ini = new Indicador(drawable,new ResourceProxyImpl(getActivity()),getActivity(), this.inicio.getTitle(), this.inicio.getSnippet(), this.inicio.getPoint());
        drawable = this.getResources().getDrawable(R.drawable.ic_fin);
        Indicador fi = new Indicador(drawable,new ResourceProxyImpl(getActivity()),getActivity(), this.fin.getTitle(), this.fin.getSnippet(), this.fin.getPoint());
        mapOverlays.add(ini);
        mapOverlays.add(fi);

        for(Punto_interes pi : lista_puntos)
        {
            Tipo_punto_interes tpi = Tipo_Puntos_InteresRepo.getTipo_Punto_InteresForId(Globals.context, pi.getId_tipo_punto_interes().longValue());
            String titulo = tpi.getNombre();
            String icono = tpi.getNombre_icono();
            GeoPoint gp = new GeoPoint(pi.getLatitud(), pi.getLongitud());
            int resID = getActivity().getResources().getIdentifier(icono.trim(), "drawable", getActivity().getPackageName());
            drawable= this.getResources().getDrawable(resID);
            ResourceProxy rp = new ResourceProxyImpl(getActivity());
            Indicador in = new Indicador(drawable,rp,getActivity(),titulo,pi.getDescripcion(),gp);
            mapOverlays.add(in);
        }

        for(Obstaculo pi : lista_obstaculos)
        {
            Tipo_obstaculo tpi = Tipo_ObstaculoRepo.getTipo_ObstaculoForId(getActivity(), pi.getId_tipo_obstaculo().longValue());
            String titulo = tpi.getNombre();
            String icono = tpi.getNombre_icono();
            GeoPoint gp = new GeoPoint(pi.getLatitud(), pi.getLongitud());
            int resID = getActivity().getResources().getIdentifier(icono.trim(), "drawable", getActivity().getPackageName());
            drawable= this.getResources().getDrawable(resID);
            ResourceProxy rp = new ResourceProxyImpl(getActivity());
            Indicador in = new Indicador(drawable,rp,getActivity(),titulo,pi.getDescripcion(),gp);
            mapOverlays.add(in);
        }

    }


    @Override
    public void onLocationChanged(Location location) {
        /*gps.setLatitude(location.getLatitude());
        gps.setLongitude(location.getLongitude());
        gps.setAltitude(location.getAltitude());*/
        this.punto = new GeoPoint(location.getLatitude(), location.getLongitude(), location.getAltitude());
        //mc.animateTo(punto);
        addMarket(punto);
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
        switch (v.getId()){
            case R.id.imageButtonGPS:
                activarGps();
                break;
            case R.id.BtnIniFin:
                if(enabled)
                {
                    //Cosas para iniciar el recorrido
                }
                else
                {
                    //Cosas para finalizar el recorrido
                }
            case R.id.direction:
                if(Globals.inicio_fin)
                    Globals.inicio_fin = false;
                else
                    Globals.inicio_fin = true;
        }
    }

    public void activarGps() {
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            GeoPoint punto = new GeoPoint(location.getLatitude(),location.getLongitude());
            mc.animateTo(punto);
        }else{
            //gps.showSettingsAlert();
        }
    }

    @Override
    public void fragmentBecameVisible() {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -azimuthInDegress,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(250);

            ra.setFillAfter(true);

            mPointer.startAnimation(ra);
            mCurrentDegree = -azimuthInDegress;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }
}