package com.mapas.franciscojavier.trekkingroute;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import org.osmdroid.bonuspack.overlays.Marker;
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
public class VisualizarMapa extends SherlockFragment implements LocationListener ,  View.OnClickListener{
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
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_visualizar_mapa, container, false);
        ImageButton botonDescarga = (ImageButton) view.findViewById(R.id.imageButton_download);
        botonDescarga.setOnClickListener(this);
        osm = (MapView) view.findViewById(R.id.mapview);
        osm.setTileSource(Globals.MAPQUESTOSM);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);
        mc = (MapController) osm.getController();
        mc.setZoom(14);
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

    public void descargarMapa() {

        boolean sdDisponible = false;
        boolean sdAccesoEscritura = false;

        //Comprobamos el estado de la memoria externa (tarjeta SD)
        String estado = Environment.getExternalStorageState();

        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            sdDisponible = true;
            sdAccesoEscritura = true;
        } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            sdDisponible = true;
            sdAccesoEscritura = false;
        } else {
            sdDisponible = false;
            sdAccesoEscritura = false;
        }
        String url = "http://trythistrail.16mb.com/Maps/cco.zip";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Descargando mapa offline");
        request.setTitle("TryThisTrail");
        // in order for this if to run, you must use the android 3.2 to compile your app
        String ruta_sd = Environment.getExternalStorageDirectory() + "/osmdroid";
        Log.d("Directorio:", ruta_sd);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir("/osmdroid", "2.zip");

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        /*try
        {
            ruta_sd = Environment.getExternalStorageDirectory()+"/osmdroid";
            Log.d("Directorio:",ruta_sd);

            File f = new File(ruta_sd, "prueba_sd.txt");

            OutputStreamWriter fout =
                    new OutputStreamWriter(
                            new FileOutputStream(f));

            fout.write("Texto de prueba.");
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
        }*/
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
            Indicadores ini = new Indicadores(drawable,new ResourceProxyImpl(getActivity()),getActivity(), item.getTitle(), item.getSnippet(), item.getPoint());
            mapOverlays.add(ini);
        }
        drawable = this.getResources().getDrawable(R.drawable.ic_fin);
        for(OverlayItem item : this.ind_fin) {
            Indicadores fi = new Indicadores(drawable,new ResourceProxyImpl(getActivity()),getActivity(), item.getTitle(), item.getSnippet(), item.getPoint());
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
        switch (v.getId()){
            case R.id.imageButton_download:
                descargarMapa();
                break;
        }
    }

    public void onRoadClicked(int id)
    {
        Log.i("on road: ", Integer.toString(id));
    }

}
