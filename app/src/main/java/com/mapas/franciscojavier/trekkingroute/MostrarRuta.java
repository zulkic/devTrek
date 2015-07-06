package com.mapas.franciscojavier.trekkingroute;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.Marker;
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
import repositorios.RutaRepo;
import repositorios.Tipo_ObstaculoRepo;
import repositorios.Tipo_Puntos_InteresRepo;

/**
 * Created by FranciscoJavier on 28-04-2015.
 */
public class MostrarRuta extends SherlockFragment {
    private MapView osm;
    private MapController mc;
    private LocationManager locationManager;
    private PathOverlay po;
    private MyLocationNewOverlay myLocationOverlay;
    private OverlayItem inicio;
    private OverlayItem fin;
    private Integer id;
    private Ruta ruta = new Ruta();
    private ArrayList<Coordenada> lista_coordenadas = new ArrayList<>();
    private ArrayList<Punto_interes> lista_puntos = new ArrayList<>();
    private ArrayList<Obstaculo> lista_obstaculos = new ArrayList<>();
    private GestureDetector gestureDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.id = this.getArguments().getInt("id_ruta");

        this.ruta = RutaRepo.getRutaForId(getActivity(),this.id.longValue());

        View view = inflater.inflate(R.layout.fragment_mostrar_ruta, container, false);

        osm = (MapView) view.findViewById(R.id.mapview);
        osm.setTileSource(Globals.MAPQUESTOSM);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);
        mc = (MapController) osm.getController();
        mc.setZoom(15);
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(getActivity());
        this.osm.getOverlays().add(myScaleBarOverlay);
        //setupMyLocation();
        //addPolyOverlay();
        addLineOverlay();
        Double latitude = inicio.getPoint().getLatitude();
        Double longitude = inicio.getPoint().getLongitude();
        GeoPoint center = new GeoPoint(latitude+0.014, longitude-0.018);
        mc.animateTo(center);
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    // add line overlay to map
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
                Coordenadas_Ruta tarea_get_coordenadas = new Coordenadas_Ruta(this.id,getActivity());
                lista_coordenadas = tarea_get_coordenadas.execute().get();

                Puntos_Interes_Ruta tarea_get_puntos = new Puntos_Interes_Ruta(this.id,getActivity());
                lista_puntos = tarea_get_puntos.execute().get();

                Obstaculos_Ruta tarea_get_obstaculos = new Obstaculos_Ruta(this.id,getActivity());
                lista_obstaculos = tarea_get_obstaculos.execute().get();

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

            for(Punto_interes punto_interes : Punto_interesRepo.punto_intereses_ruta(getActivity(), this.id.longValue()))
            {
                lista_puntos.add(punto_interes);
            }

            for(Obstaculo obstaculo : ObstaculoRepo.obstaculos_ruta(getActivity(), this.id.longValue()))
            {
                lista_obstaculos.add(obstaculo);
            }

            Log.i("coordenadas offline: ", "obtiene coordenadas offline");
        }

        if(lista_coordenadas.size() > 0)
        {
            List lineData = new ArrayList();
            this.inicio = new OverlayItem("Inicio " + ruta.getNombre(), ruta.getDescripcion() ,new GeoPoint (lista_coordenadas.get(0).getLatitud(),lista_coordenadas.get(0).getLongitud()));
            this.fin = new OverlayItem("Fin ruta " + ruta.getNombre(), "termino de ruta",new GeoPoint (lista_coordenadas.get(lista_coordenadas.size()-1).getLatitud(), lista_coordenadas.get(lista_coordenadas.size()-1).getLongitud()));
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
        Indicadores ini = new Indicadores(drawable,new ResourceProxyImpl(getActivity()),getActivity(), this.inicio.getTitle(), this.inicio.getSnippet(), this.inicio.getPoint());
        drawable = this.getResources().getDrawable(R.drawable.ic_fin);
        Indicadores fi = new Indicadores(drawable,new ResourceProxyImpl(getActivity()),getActivity(), this.fin.getTitle(), this.fin.getSnippet(), this.fin.getPoint());
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
            Indicadores in = new Indicadores(drawable,rp,getActivity(),titulo,pi.getDescripcion(),gp);
            mapOverlays.add(in);
        }

        for(Obstaculo obs : lista_obstaculos)
        {
            Tipo_obstaculo tpi = Tipo_ObstaculoRepo.getTipo_ObstaculoForId(getActivity(), obs.getId_tipo_obstaculo().longValue());
            String titulo = tpi.getNombre();
            String icono = tpi.getNombre_icono();
            GeoPoint gp = new GeoPoint(obs.getLatitud(), obs.getLongitud());
            int resID = getActivity().getResources().getIdentifier(icono.trim(), "drawable", getActivity().getPackageName());
            drawable= this.getResources().getDrawable(resID);
            ResourceProxy rp = new ResourceProxyImpl(getActivity());
            Indicadores in = new Indicadores(drawable,rp,getActivity(),titulo,obs.getDescripcion(),gp);
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
        inflater  = ((SherlockFragmentActivity)getActivity()).getSupportMenuInflater();
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
            FragmentTransaction ft = Globals.ft.beginTransaction();
            ft.replace(R.id.content_frame, new DetallesCrearRuta().newInstance(ruta.getTiempo_estimado(),ruta.getKms(),ruta.getNombre(),this.ruta.getDescripcion(),ruta.getId().intValue()));
            ft.addToBackStack("Detalles Crear Ruta");
            ft.commit();
            return true;
        }
        if (id == R.id.action_delet) {
            //Toast.makeText(getActivity(),"el eliminar", Toast.LENGTH_SHORT).show();

            FragmentTransaction ft = Globals.ft.beginTransaction();
            ft.replace(R.id.content_frame, new DetallesEliminarRuta().newInstance(ruta.getId(),
                    ruta.getNombre(), ruta.getTiempo_estimado(), ruta.getKms(), ruta.getTipo(), ruta.getDescripcion(), ruta.getId_region()));
            ft.addToBackStack("Eliminar Ruta");
            ft.commit();
            return true;
        }

        if (id == R.id.action_fav)
        {
            this.ruta.setFavorita(true);
            RutaRepo.insertOrUpdate(getActivity(), this.ruta);
            Toast.makeText(getActivity(),
                    "Ruta agregada a favoritos", Toast.LENGTH_LONG)
                    .show();
        }

        if (id == R.id.action_ini)
        {
            Toast.makeText(getActivity(), "Iniciando recorrido : " + ruta.getNombre()
                    , Toast.LENGTH_SHORT).show();
            Globals.ini_rec = this.ruta;
            Fragment frag = new Frag_Iniciar_Rec();
            FragmentTransaction ft = Globals.ft.beginTransaction();
            ft.replace(R.id.content_frame, frag);
            ft.addToBackStack("Iniciar Recorrido");
            ft.commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
