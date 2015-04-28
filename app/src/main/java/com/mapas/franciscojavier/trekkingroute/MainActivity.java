package com.mapas.franciscojavier.trekkingroute;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

//import android.view.View;
//import android.view.ViewGroup
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;


import org.osmdroid.views.overlay.PathOverlay;


public class MainActivity extends ActionBarActivity implements LocationListener{
    private MapView osm;
    private MapController mc;
    private LocationManager locationManager;
    private PathOverlay po;
    private Boolean encendido= false;
    // GPSTracker class
    GPS gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gps = new GPS(MainActivity.this);

        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        // Verificar si el GPS esta prendido
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            // \n para saltarse una linea
            //Toast.makeText(getApplicationContext(), "Tu ubicacion es - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();


        }else{
            // no tengo la ubicacion
            // GPS o Network no estan activos
            // Pregunto si quiero ir a los Ajustes
            gps.showSettingsAlert();
        }
        osm = (MapView) findViewById(R.id.mapview);
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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);


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
        }
        osm.getOverlays().add(po);
        osm.getOverlays().add(marker);
        osm.invalidate();
    }

    public void initPathOverlay(){
        po = new PathOverlay(Color.RED,this);
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

    public void encenderGps(View view)
    {
        //dentro de la clase gps esta la funcion para apagarlo asiq esta puede q no sea necesaria
        initPathOverlay();
        encendido = true;
    }
    public void apagarGps(View view)
    {
       encendido = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}

