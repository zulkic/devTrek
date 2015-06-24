package com.mapas.franciscojavier.trekkingroute;

import android.content.Context;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

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
import JSON.Puntos_Interes_Ruta;
import greendao.Coordenada;
import greendao.Punto_interes;
import greendao.Ruta;
import greendao.Tipo_punto_interes;
import repositorios.CoordenadaRepo;
import repositorios.RutaRepo;
import repositorios.Tipo_Puntos_InteresRepo;

public class FIRMapa extends Fragment implements LocationListener, AdapterView.OnClickListener {

    private MapView osm;
    private MapController mc;
    private LocationManager locationManager;
    // GPSTracker class
    private GPS gps;
    private Long id_ruta;
    private Ruta ruta;
    private ArrayList<Coordenada> lista_coordenadas = new ArrayList<>();
    private ArrayList<Punto_interes> lista_puntos = new ArrayList<>();
    private List<Overlay> puntosDeInteres;
    private OverlayItem inicio;
    private OverlayItem fin;
    private Marker aux;
    private PathOverlay po;
    private GeoPoint punto;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (view == null) {
            this.id_ruta = this.getArguments().getLong("id_ruta");
            ruta = RutaRepo.getRutaForId(Globals.context, id_ruta);

            gps = new GPS(Globals.context);

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            view = inflater.inflate(R.layout.fir_layout_mapa, container, false);
            ImageButton botonGps = (ImageButton) view.findViewById(R.id.imageButtonGPS);
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

            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        }
        return view;
    }
    public static FIRMapa newInstance(String text) {

        Log.i("mapa: ", "newInstance");
        FIRMapa f = new FIRMapa();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
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
        //marker.setIcon(getResources().getDrawable(R.drawable.zoom_in));
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
                Coordenadas_Ruta tarea_get_coordenadas = new Coordenadas_Ruta(this.id_ruta.intValue(),Globals.context);
                lista_coordenadas = tarea_get_coordenadas.execute().get();

                Puntos_Interes_Ruta tarea_get_puntos = new Puntos_Interes_Ruta(this.id_ruta.intValue(),Globals.context);
                lista_puntos = tarea_get_puntos.execute().get();

            }
            catch (Exception e)
            {
                Log.i("Error: ", "Imposible obtener las coordenadas y puntos");
            }
        }
        else
        {
            for(Coordenada coordenada : CoordenadaRepo.coordenadas_ruta(Globals.context, this.id_ruta))
            {
                lista_coordenadas.add(coordenada);
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
    }


    @Override
    public void onLocationChanged(Location location) {
        gps.setLatitude(location.getLatitude());
        gps.setLongitude(location.getLongitude());
        gps.setAltitude(location.getAltitude());
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

}