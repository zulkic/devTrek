package com.mapas.franciscojavier.trekkingroute.Utility;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import greendao.Coordenada;
import greendao.Ruta;

/**
 * Created by Nicolas on 06-06-2015.
 */
public class Globals {

    public static String EMAIL_REGEX = "\\b(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])\\b";
    public static String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";
    public static String EMAIL_REGEX2 = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]{2,}+)*@[A-Za-z0-9-]{2,}+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public static final OnlineTileSourceBase MAPQUESTOSM = new XYTileSource("MapQuest",
            ResourceProxy.string.mapquest_osm, 0, 18, 256, ".jpg", new String[] {
            "http://otile1.mqcdn.com/tiles/1.0.0/map/",
            "http://otile2.mqcdn.com/tiles/1.0.0/map/",
            "http://otile3.mqcdn.com/tiles/1.0.0/map/",
            "http://otile4.mqcdn.com/tiles/1.0.0/map/" });

    //Preferencias del usuario
    public static final String EMAIL = "Email";
    public static final String NOMBRE = "Nombre";
    public static final String ROL = "Rol";
    public static final String PASS = "Pass";
    public static final String PREF = "Prefs";
    public static final String INVITADO = "invitado";
    public static final String CLIENTE = "cliente";
    public static final String ADMIN = "admin";
    public static Context context;
    public static FragmentManager ft;
    public static Ruta ini_rec;
    public static final Boolean internet = false;
    public static Boolean inicio_fin = true;
    public static ArrayList<Coordenada> coordenadas_inic_rec = new ArrayList<>();
    public static GeoPoint gps;
}