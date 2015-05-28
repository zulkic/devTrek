package com.mapas.franciscojavier.trekkingroute;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import JSON.Coordenadas_Ruta;
import greendao.Coordenada;

/**
 * Created by FranciscoJavier on 28-04-2015.
 */
public class MostrarRuta extends Fragment{
    private MapView osm;
    private MapController mc;
    private LocationManager locationManager;
    private PathOverlay po;
    private MyLocationNewOverlay myLocationOverlay;
    private OverlayItem inicio;
    private OverlayItem fin;
    private Integer id;
    private String nombre_ruta;
    private String descripcion_ruta;
    private String tiempo_ruta;
    private Float kms_ruta;
    private ArrayList<Coordenada> lista_coordenadas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.id = this.getArguments().getInt("id_ruta");
        this.nombre_ruta = this.getArguments().getString("nombre_ruta");
        this.descripcion_ruta = this.getArguments().getString("descripcion_ruta");
        this.tiempo_ruta = this.getArguments().getString("tiempo_ruta");
        this.kms_ruta= this.getArguments().getFloat("kms_ruta");
        View view = inflater.inflate(R.layout.fragment_mostrar_ruta, container, false);

        osm = (MapView) view.findViewById(R.id.mapview);
        osm.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);
        mc = (MapController) osm.getController();
        mc.setZoom(20);
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(getActivity());
        this.osm.getOverlays().add(myScaleBarOverlay);
        GeoPoint center = new GeoPoint(-34.98604036, -71.24007225);
        mc.setCenter(center);
        //setupMyLocation();
        //addPolyOverlay();
        addLineOverlay();
        addPoiOverlay();
        //GeoPoint center = new GeoPoint(, );
        //mc.animateTo(center);
        //addMarket(center);

        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);


    }
    // add polygon overlay to map
    private void addPolyOverlay() {
        // set custom polygon style
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(30);

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

    // add line overlay to map
    private void addLineOverlay() {
        // set custom line style
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);

        // list of GeoPoint objects to be used to draw line
        try
        {
            Coordenadas_Ruta tarea_get_coordenadas = new Coordenadas_Ruta(this.id);
            lista_coordenadas = tarea_get_coordenadas.execute().get();
        }
        catch (Exception e)
        {}

        List lineData = new ArrayList();
        this.inicio = new OverlayItem("Inicio ruta", "ruta inicio prueba 1",new GeoPoint (lista_coordenadas.get(0).getLatitud(),lista_coordenadas.get(0).getLongitud()));
        this.fin = new OverlayItem("Fin ruta", "termino de ruta",new GeoPoint (lista_coordenadas.get(lista_coordenadas.size()-1).getLatitud(), lista_coordenadas.get(lista_coordenadas.size()-1).getLongitud()));
        for( Coordenada coordenada : lista_coordenadas)
        {
            lineData.add(new GeoPoint(coordenada.getLatitud(), coordenada.getLongitud()));
        }
        // apply line style & data and add to map

        PathOverlay lineOverlay = new PathOverlay(Color.CYAN,getActivity());
        lineOverlay.setPaint(paint);
        lineOverlay.addPoints(lineData);
        //lineOverlay.setData(lineData);
        osm.getOverlays().add(lineOverlay);
    }

    // add an itemized overlay to map
    private void addPoiOverlay() {

        List<Overlay> mapOverlays = osm.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.location_marker);
        Indicador itemizedoverlay = new Indicador(drawable,new ResourceProxyImpl(getActivity()),getActivity());
        //Indicador aguas = new Indicador(this.getResources().getDrawable(R.drawable.ic_parque),new ResourceProxyImpl(getActivity()),getActivity());
        //Indicador parque = new Indicador(this.getResources().getDrawable(R.drawable.ic_agua),new ResourceProxyImpl(getActivity()),getActivity());
        //OverlayItem poi3 = new OverlayItem("Parque", "Parque en Argomedo con Carmen",new GeoPoint (-34.98709516, -71.2385273));
        //OverlayItem poi4 = new OverlayItem("Agua Potable", "Agua en Membrillar con Carmen",new GeoPoint (-34.98698968, -71.23715401));
        itemizedoverlay.addOverlay(this.inicio);
        itemizedoverlay.addOverlay(this.fin);
        //parque.addOverlay(poi4);
        //aguas.addOverlay(poi3);
        mapOverlays.add(itemizedoverlay);
        //mapOverlays.add(aguas);
        //mapOverlays.add(parque);


        // use a custom POI marker by referencing the bitmap file directly,
        // using the filename as the resource ID
        /*Drawable icon = getResources().getDrawable(R.drawable.location_marker);
        ItemizedOverlay poiOverlay = new ItemizedIconOverlay(icon,,getActivity());

        // set GeoPoints and title/snippet to be used in the annotation view
        OverlayItem poi1 = new OverlayItem("Denver, Colorado", "MapQuest Headquarters",new GeoPoint (39.739983,-104.984727));
        poiOverlay.addItem(poi1);
        OverlayItem poi2 = new OverlayItem("Palo Alto, California", "AOL Offices",new GeoPoint (37.441903,-122.141895));
        poiOverlay.addItem(poi2);

        // add a tap listener for the POI overlay
        poiOverlay.onSingleTapConfirmed()
        poiOverlay.setTapListener(new ItemizedOverlay.OverlayTapListener() {
            @Override
            public void onTap(GeoPoint pt, MapView mapView) {
                // when tapped, show the annotation for the overlayItem
                int lastTouchedIndex = poiOverlay.getLastFocusedIndex();
                if(lastTouchedIndex>-1){
                    OverlayItem tapped = poiOverlay.getItem(lastTouchedIndex);
                    annotation.showAnnotationView(tapped);
                }
            }
        });

        osm.getOverlays().add(poiOverlay);*/
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //onCreateOptionsMenu();
        //setContentView(R.layout.activity_main);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater  = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_mostrar_ruta, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupMyLocation() {
        this.myLocationOverlay = new MyLocationNewOverlay(getActivity(), osm);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                GeoPoint currentLocation = myLocationOverlay.getMyLocation();
                osm.getController().animateTo(currentLocation);
//                osm.getController().setZoom(14);
                myLocationOverlay.setDrawAccuracyEnabled(true);
                osm.getOverlays().add(myLocationOverlay);
                //myLocationOverlay.en;
            }
        });
    }
    public void addMarket(GeoPoint center, Boolean encendido)
    {
        Marker marker = new Marker(osm);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        //marker.setIcon(getResources().getDrawable(R.drawable.zoom_in));

        osm.getOverlays().clear();
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
                Fragment newFragment = new DetallesCrearRuta().newInstance(this.tiempo_ruta,this.kms_ruta,this.nombre_ruta,this.descripcion_ruta,this.id);
            //newFragment.setTiempoTotal(tiempoTotalRecorrido);
                FragmentManager fm1 = getFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                ft1.replace(R.id.container, newFragment)
                        .addToBackStack(null)
                        .commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
