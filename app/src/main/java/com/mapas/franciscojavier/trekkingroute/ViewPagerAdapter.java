package com.mapas.franciscojavier.trekkingroute;

/**
 * Created by juancarlosgonzalezca on 24-06-2015.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    // Declare the number of ViewPager pages
    private final int PAGE_COUNT = 3;
    //private FIRObstaculos tab1;
    private FIRDetalles tab1;
    private FIRMapa tab2;
    private FIRHistograma tab3;
    private String titles[] = new String[] { "Detalles", "Mapa", "Histograma" };

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                tab1 = new FIRDetalles();
                return tab1;

            case 1:
                tab2 = new FIRMapa();
                return tab2;

            case 2:
                tab3 = new FIRHistograma();
                return tab3;

            default:
                break;
        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
