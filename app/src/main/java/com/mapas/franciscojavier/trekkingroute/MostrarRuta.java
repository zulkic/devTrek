package com.mapas.franciscojavier.trekkingroute;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.ResourceProxy;
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
import JSON.Puntos_Interes_Ruta;
import greendao.Coordenada;
import greendao.Punto_interes;
import greendao.Tipo_punto_interes;
import repositorios.CoordenadaRepo;
import repositorios.Tipo_Puntos_InteresRepo;

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
    private Boolean sincronizada;
    private ArrayList<Coordenada> lista_coordenadas = new ArrayList<>();
    private ArrayList<Punto_interes> lista_puntos = new ArrayList<>();
    private List<Overlay> puntosDeInteres;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.id = this.getArguments().getInt("id_ruta");
        this.nombre_ruta = this.getArguments().getString("nombre_ruta");
        this.descripcion_ruta = this.getArguments().getString("descripcion_ruta");
        this.tiempo_ruta = this.getArguments().getString("tiempo_ruta");
        this.kms_ruta= this.getArguments().getFloat("kms_ruta");
        this.sincronizada = this.getArguments().getBoolean("sincronizada");
        View view = inflater.inflate(R.layout.fragment_mostrar_ruta, container, false);

        osm = (MapView) view.findViewById(R.id.mapview);
        osm.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);
        mc = (MapController) osm.getController();
        mc.setZoom(20);
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(getActivity());
        this.osm.getOverlays().add(myScaleBarOverlay);
        //setupMyLocation();
        //addPolyOverlay();
        addLineOverlay();
        puntosDeInteres = osm.getOverlays();
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

    // add line overlay to map
    private void addLineOverlay() {
        // set custom line style
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);

        // list of GeoPoint objects to be used to draw line
        if(sincronizada)
        {
            try
            {
                Coordenadas_Ruta tarea_get_coordenadas = new Coordenadas_Ruta(this.id,getActivity());
                lista_coordenadas = tarea_get_coordenadas.execute().get();

                Puntos_Interes_Ruta tarea_get_puntos = new Puntos_Interes_Ruta(this.id,getActivity());
                lista_puntos = tarea_get_puntos.execute().get();

            }
            catch (Exception e)
            {
                Log.i("Error: ", "Imposible obtener las coordenadas y puntos");
            }
        }
        else
        {
            for(Coordenada coordenada : CoordenadaRepo.coordenadas_ruta(getActivity(), this.id.longValue()))
            {
                lista_coordenadas.add(coordenada);
            }
            Log.i("coordenadas offline: ", "obtiene coordenadas offline");
        }

        if(lista_coordenadas.size() > 0)
        {
            List lineData = new ArrayList();
            this.inicio = new OverlayItem("Inicio " + nombre_ruta, descripcion_ruta ,new GeoPoint (lista_coordenadas.get(0).getLatitud(),lista_coordenadas.get(0).getLongitud()));
            this.fin = new OverlayItem("Fin ruta " + nombre_ruta, "termino de ruta",new GeoPoint (lista_coordenadas.get(lista_coordenadas.size()-1).getLatitud(), lista_coordenadas.get(lista_coordenadas.size()-1).getLongitud()));
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
            Tipo_punto_interes tpi = Tipo_Puntos_InteresRepo.getTipo_Punto_InteresForId(getActivity(), pi.getId_tipo_punto_interes().longValue() );
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
        if (id == R.id.action_delet) {
            //Toast.makeText(getActivity(),"el eliminar", Toast.LENGTH_SHORT).show();
            Long idLong = new Long(this.id);
            Fragment tf = new DetallesEliminarRuta().newInstance(idLong,
                    this.nombre_ruta, this.tiempo_ruta, this.kms_ruta, "tipo", this.descripcion_ruta);
            //pendiente
            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
            ft.replace(R.id.container, tf);
            ft.addToBackStack(null);
            ft.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
