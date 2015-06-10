package com.mapas.franciscojavier.trekkingroute;

import android.app.Fragment;
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

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
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
import java.util.concurrent.ExecutionException;

import JSON.Coordenadas_Ruta;
import JSON.Obtener_Rutas;
import greendao.Coordenada;
import greendao.Ruta;
import repositorios.CoordenadaRepo;

/**
 * Created by FranciscoJavier on 28-04-2015.
 */
public class VisualizarMapa extends Fragment implements LocationListener ,  View.OnClickListener{
    private MapView osm;
    private MapController mc;
    private LocationManager locationManager;
    private PathOverlay po;
    private Boolean encendido= false;
    private OverlayItem inicio;
    private OverlayItem fin;
    private ArrayList<Ruta> rutas = new ArrayList<>();
    private ArrayList<OverlayItem> ind_ini = new ArrayList<>();
    private ArrayList<OverlayItem> ind_fin = new ArrayList<>();
    // GPSTracker class
    GPS gps;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_visualizar_mapa, container, false);

        osm = (MapView) view.findViewById(R.id.mapview);
        osm.setTileSource(TileSourceFactory.CYCLEMAP);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);
        mc = (MapController) osm.getController();
        mc.setZoom(20);
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(getActivity());
        this.osm.getOverlays().add(myScaleBarOverlay);
        GeoPoint center = new GeoPoint(-34.98604036, -71.24007225);
        mc.setCenter(center);

        addLineOverlay();

        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
    }

    private void addLineOverlay() {
        // set custom line style
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);

        // list of GeoPoint objects to be used to draw line
        try {
            Obtener_Rutas task = new Obtener_Rutas(getActivity());
            this.rutas = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.i("rutas:",  Integer.toString(rutas.size()));
        for(Ruta ruta : rutas) {
            ArrayList<Coordenada> lista_coordenadas = new ArrayList<>();
            if(ruta.getSincronizada())
            {
                try
                {
                    Coordenadas_Ruta tarea_get_coordenadas = new Coordenadas_Ruta(ruta.getId().intValue(),getActivity());
                    lista_coordenadas = tarea_get_coordenadas.execute().get();
                }
                catch (Exception e)
                {
                    Log.i("Error: ", "Imposible obtener las coordenadas y puntos");
                }
            }
            else
            {
                for(Coordenada coordenada : CoordenadaRepo.coordenadas_ruta(getActivity(), ruta.getId()))
                {
                    lista_coordenadas.add(coordenada);
                }
                Log.i("coordenadas offline: ", "obtiene coordenadas offline");
            }
            List lineData = new ArrayList();
            this.inicio = new OverlayItem("Inicio ruta", ruta.getNombre(), new GeoPoint(lista_coordenadas.get(0).getLatitud(), lista_coordenadas.get(0).getLongitud()));
            this.ind_ini.add(this.inicio);
            this.fin = new OverlayItem("Fin ruta", ruta.getNombre(), new GeoPoint(lista_coordenadas.get(lista_coordenadas.size() - 1).getLatitud(), lista_coordenadas.get(lista_coordenadas.size() - 1).getLongitud()));
            this.ind_fin.add(this.fin);
            for (Coordenada coordenada : lista_coordenadas) {
                lineData.add(new GeoPoint(coordenada.getLatitud(), coordenada.getLongitud()));
            }
            // apply line style & data and add to map

            PathOverlay lineOverlay = new PathOverlay(Color.MAGENTA, getActivity());
            lineOverlay.setPaint(paint);
            lineOverlay.addPoints(lineData);
            //lineOverlay.setData(lineData);
            osm.getOverlays().add(lineOverlay);
        }
        addPoiOverlay();
    }

    private void addPoiOverlay() {
        List<Overlay> mapOverlays = osm.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.location_marker);
        for(OverlayItem item : this.ind_ini)
        {
            Indicador ini = new Indicador(drawable,new ResourceProxyImpl(getActivity()),getActivity(), item.getTitle(), item.getSnippet(), item.getPoint());
            mapOverlays.add(ini);
        }
        drawable = this.getResources().getDrawable(R.drawable.ic_fin);
        for(OverlayItem item : this.ind_fin) {
            Indicador fi = new Indicador(drawable,new ResourceProxyImpl(getActivity()),getActivity(), item.getTitle(), item.getSnippet(), item.getPoint());
            mapOverlays.add(fi);
        }
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
    }

    public void onRoadClicked(int id)
    {
        Log.i("on road: ", Integer.toString(id));
    }

}
