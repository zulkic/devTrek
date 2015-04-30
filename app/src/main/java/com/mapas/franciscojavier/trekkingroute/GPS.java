package com.mapas.franciscojavier.trekkingroute;


import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPS extends Service implements LocationListener {

    private final Context mContext;

    // flag para el GPS
    boolean isGPSEnabled = false;

    // flag para el network
    boolean isNetworkEnabled = false;

    // flag para la localicacion GPS
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;
    double altitude;

    // La distancia mínima para cambiar Actualizaciones en metros
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters

    // El tiempo mínimo entre actualizaciones en milisegundos
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1; // 1 seg

    // Declarando un Location Manager
    protected LocationManager locationManager;

    public GPS(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // obteniendo el estado del GPS
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // obteniendo el estado del network
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // Ningún proveedor de red está habilitada
                Log.i("proveedor de red: ", "des-habilitada");
            } else {
                this.canGetLocation = true;
                // Primera Ubicación  del Proveedor de la red
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // sil el GPS esta activado obtengo lat/long usando los servicios GPS
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Activado", "GPS Activado");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Dejar de usar GPS
     * Llamar a esta función dejará de usar el GPS en la aplicación
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPS.this);
        }
    }

    /**
     * Funcion para obtener la latitud
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitud
        return latitude;
    }

    /**
     *  Funcion para obtener la longitud
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitud
        return longitude;
    }

    public double getAltitude() {
        if (location != null) {
            altitude = location.getAltitude();
        }

        // return longitud
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(double altitude){
        this.altitude = altitude;
    }


    /**
     * Funcion para revisar si GPS/wifi esta activo
     * @return boolean
     * */

     public boolean canGetLocation() {
        // obteniendo el estado del GPS
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        Log.i("---"," "+isGPSEnabled);
        return isGPSEnabled;
    }

    /**
     * Función para mostrar los ajustes
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Titulo de la alerta
        alertDialog.setTitle("GPS apagado");

        // Mensaje
        alertDialog.setMessage("GPS no esta habilitado. Desea ir al menu de ajustes?");

        // El botón de Configuración
        alertDialog.setPositiveButton("Ajustes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // El botón de Cancelacion
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Mostrando el mensaje de alerta
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
