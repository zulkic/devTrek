package com.mapas.franciscojavier.trekkingroute;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

import greendao.Obstaculo;
import greendao.Punto_interes;

/**
 * Created by FranciscoJavier on 10-05-2015.
 */
public class Indicadores extends ItemizedOverlay {
    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    private OverlayItem overlay;
    private Context mContext;
    private ArrayList<Punto_interes> puntos = new ArrayList<>();
    private ArrayList<Obstaculo> obstaculos = new ArrayList<>();

    public Indicadores(Drawable pDefaultMarker, ResourceProxy pResourceProxy, Context context) {
        super(pDefaultMarker, pResourceProxy);
        mContext = context;
    }

    public Indicadores(Drawable pDefaultMarker, ResourceProxy pResourceProxy, Context context, String titulo, String descripcion, GeoPoint gp) {
        super(pDefaultMarker, pResourceProxy);
        mContext = context;
        overlay = new OverlayItem(titulo,descripcion,gp);
        addOverlay(overlay);
        //populate();
    }
    public void createIndicador(Drawable marker,String titulo, String descripcion, GeoPoint gp,Long id_tipo){
        Punto_interes pto = new Punto_interes();
        pto.setId_tipo_punto_interes((int)(long)id_tipo);
        pto.setDescripcion(descripcion);
        pto.setLatitud(gp.getLatitude());
        pto.setLongitud(gp.getLongitude());
        overlay= new OverlayItem(titulo,descripcion,gp);
        overlay.setMarker(marker);
        addOverlay(overlay);
        puntos.add(pto);
    }

    public void createIndicadorObs(Drawable marker,String titulo, String descripcion, GeoPoint gp,Long id_tipo){
        Obstaculo pto = new Obstaculo();
        pto.setId_tipo_obstaculo((int)(long)id_tipo);
        pto.setDescripcion(descripcion);
        pto.setLatitud(gp.getLatitude());
        pto.setLongitud(gp.getLongitude());
        overlay= new OverlayItem(titulo,descripcion,gp);
        overlay.setMarker(marker);
        addOverlay(overlay);
        obstaculos.add(pto);
    }


    public ArrayList<Punto_interes> getPuntos() {
        return puntos;
    }
    public ArrayList<Obstaculo> getObstaculos() {
        return obstaculos;
    }

    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return mOverlays.get(i);
    }

    @Override
    public int size() {
        return mOverlays.size();
    }

    @Override
    protected boolean onTap(final int index) {
        OverlayItem item = mOverlays.get(index);
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(item.getTitle());
        /*
        dialog.setNeutralButton(R.string.action_delet,
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        mOverlays.remove(index);
                        puntos.remove(index);
                        populate();
                    }
                });
        */
        dialog.setNegativeButton(R.string.button_cancelar_ruta,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.setMessage(item.getSnippet());
        dialog.show();
        return true;
    }

    @Override
    public boolean onSnapToItem(int i, int i1, Point point, IMapView iMapView) {
        return false;
    }
}