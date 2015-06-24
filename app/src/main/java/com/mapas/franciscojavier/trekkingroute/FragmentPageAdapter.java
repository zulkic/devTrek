package com.mapas.franciscojavier.trekkingroute;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentPageAdapter extends FragmentPagerAdapter {

    public Long id_ruta;
    private Fragment detalles;
    private Fragment mapa;
    private Fragment histograma;

    public FragmentPageAdapter(FragmentManager fm, Long id_ruta) {
        super(fm);
        // TODO Auto-generated constructor stub
        this.id_ruta = id_ruta;
        Bundle bundle = new Bundle();
        bundle.putLong("id_ruta", id_ruta );

        detalles = new FIRDetalles();
        detalles.setArguments(bundle);

        mapa = new FIRMapa();
        mapa.setArguments(bundle);

        histograma = new FIRHistograma();
        histograma.setArguments(bundle);
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        switch (arg0) {
            case 0:
                return detalles;
            case 1:
                return mapa;
            case 2:
                return histograma;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }

}
