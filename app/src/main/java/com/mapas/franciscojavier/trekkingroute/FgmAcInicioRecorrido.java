package com.mapas.franciscojavier.trekkingroute;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class FgmAcInicioRecorrido extends FragmentActivity implements ActionBar.TabListener, FIRDetalles.OnFragmentInteractionListener {
    private ActionBar actionbar;
    private ViewPager viewpager;
    private FragmentPageAdapter ft;
    private Long id_ruta;
    private Tab tab1;
    private Tab tab2;
    private Tab tab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent(); // gets the previously created intent
        id_ruta = myIntent.getLongExtra("id_ruta", 0);
        Log.i("intent: ", id_ruta.toString());

        setContentView(R.layout.lay_fgm_ac_princ);
        viewpager = (ViewPager) findViewById(R.id.pager);
        ft = new FragmentPageAdapter(getSupportFragmentManager(), id_ruta);
        actionbar = getActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        viewpager.setAdapter(ft);
        viewpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionbar.setSelectedNavigationItem(position);
            }
        });
        //tabs
        tab1 = actionbar.newTab().setText("Detalles");
        tab2 = actionbar.newTab().setText("Mapa");
        tab3 = actionbar.newTab().setText("Histograma");
        tab1.setTabListener(this);
        tab2.setTabListener(this);
        tab3.setTabListener(this);

        actionbar.addTab(tab1);
        actionbar.addTab(tab2);
        actionbar.addTab(tab3);

        actionbar.setSelectedNavigationItem(tab2.getPosition());
        viewpager.setCurrentItem(tab2.getPosition());

    }
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewpager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}