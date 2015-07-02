package com.mapas.franciscojavier.trekkingroute;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.mapas.franciscojavier.trekkingroute.Account.LoginFragment;
import com.mapas.franciscojavier.trekkingroute.Account.MainCalls;
import com.mapas.franciscojavier.trekkingroute.Account.RegisterFragment;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import java.util.Locale;

import JSON.Sincronizar_Rutas;
import JSON.Sincronizar_Tipos_Indicadores;
import greendao.DaoMaster;
import greendao.DaoSession;
import greendao.Sync;
import greendao.Usuario;
import repositorios.SyncRepo;
import repositorios.Tipo_ObstaculoRepo;
import repositorios.Tipo_Puntos_InteresRepo;



public class MenuPrincipal extends SherlockFragmentActivity implements RoutesFragment.OnFragmentInteractionListener, EliminarRuta.OnFragmentInteractionListener, MainCalls,Favoritas.OnFragmentInteractionListener {

    // Declare Variables
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private MenuListAdapter mMenuAdapter;
    private String[] title;
    private String[] subtitle;
    private int[] icon;
    Fragment fragment1 = new PantallaInicio();
    Fragment fragment2 = new Frag_Iniciar_Rec();
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    /**
     * Used to store the last screen title. For use in {@link ()}.
     */
    public DaoSession daoSession;
    private SharedPreferences pref;
    private String rol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDatabase();
        pref = this.getSharedPreferences(Globals.PREF, Context.MODE_PRIVATE);
        rol = pref.getString(Globals.ROL, "invitado");
        Globals.context = this;
        Globals.ft = getSupportFragmentManager();
        setContentView(R.layout.activity_menu_principal);



        mTitle = mDrawerTitle = getTitle();

        if(rol.equals(Globals.ADMIN)) {
            // Generate title
            title = new String[] {
                    getString(R.string.title_section0),
                    getString(R.string.title_section1),
                    getString(R.string.title_section2),
                    getString(R.string.title_section3),
                    getString(R.string.title_section4),
                    getString(R.string.title_section6),
                    getString(R.string.title_section7)
                                  };

            // Generate icon
            icon = new int[] { R.drawable.ic_inicio, R.drawable.ic_visualizar_map, R.drawable.ic_planificar, R.drawable.ic_crear, R.drawable.ic_iniciar_rec, R.drawable.ic_inicia_sesion, R.drawable.ic_cerrar_sesion };
        }
        else if(rol.equals(Globals.CLIENTE)) {
            // Generate title
            title = new String[] {
                    getString(R.string.title_section0),
                    getString(R.string.title_section1),
                    getString(R.string.title_section2),
                    getString(R.string.title_section3),
                    getString(R.string.title_section4),
                    getString(R.string.title_section6),
                    getString(R.string.title_section7)
            };

            // Generate icon
            icon = new int[] { R.drawable.ic_inicio, R.drawable.ic_visualizar_map, R.drawable.ic_planificar, R.drawable.ic_crear, R.drawable.ic_iniciar_rec, R.drawable.ic_inicia_sesion, R.drawable.ic_cerrar_sesion  };
        }
        else{
            // Generate title
            title = new String[] {
                    getString(R.string.title_section0),
                    getString(R.string.title_section1),
                    getString(R.string.title_section5)
            };

            // Generate icon
            icon = new int[] { R.drawable.ic_inicio, R.drawable.ic_visualizar_map, R.drawable.ic_inicia_sesion  };
        }

        // Locate DrawerLayout in drawer_main.xml
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Locate ListView in drawer_main.xml
        mDrawerList = (ListView) findViewById(R.id.listview_drawer);

        // Set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        // Pass string arrays to MenuListAdapter
        mMenuAdapter = new MenuListAdapter(MenuPrincipal.this, title, subtitle,icon);

        // Set the MenuListAdapter to the ListView
        mDrawerList.setAdapter(mMenuAdapter);

        // Capture listview menu item click
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                // TODO Auto-generated method stub
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub
                // Set the title on the action when drawer open
                getSupportActionBar().setTitle(mDrawerTitle);
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if(SyncRepo.getSyncForId(this, (long) 1) == null)
        {
            Sync sync = new Sync();
            sync.setId((long) 1);
            sync.setTabla("rutas");
            sync.setTiempo("inicializado");
            SyncRepo.insertOrUpdate(this, sync);
        }

        try{
            if(Tipo_ObstaculoRepo.getAllTipos_Obstaculos(this).size() == 0  || Tipo_Puntos_InteresRepo.getAllTipos_Puntos_Interes(this).size() == 0 ) {
                Sincronizar_Tipos_Indicadores tarea = new Sincronizar_Tipos_Indicadores(this);
                tarea.execute();
            }

        }
        catch (Exception e)
        {
            Log.i("error sync inicial: ", e.toString());
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new PantallaInicio());
        ft.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {

            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }

        if (id == R.id.action_settings) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new Configuracion());
            ft.commit();

            //return true;
        }

        if (id == R.id.action_sync) {
            try {
                new Sincronizar_Rutas(this);
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Error al sincronizar", Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    // ListView click listener in the navigation drawer
    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Locate Position
        Boolean logout = true;
        Boolean flagInicioRecorrido = true;
        if(rol.equals(Globals.ADMIN)) {
            switch (position) {
                case 0:
                    ft.replace(R.id.content_frame, new PantallaInicio());
                    break;
                case 1:
                    ft.replace(R.id.content_frame, new VisualizarMapa());
                    break;
                case 2:
                    ft.replace(R.id.content_frame, new RoutesFragment());
                    break;
                case 3:
                    ft.replace(R.id.content_frame, new CrearRuta());
                    break;
                case 4:
                    ft.replace(R.id.content_frame, new Favoritas());
                    break;
                case 5:
                    ft.replace(R.id.content_frame, new LoginFragment());
                    break;
                case 6:
                    Cerrar_Sesion();
                    logout = false;
                    break;
            }
        }
        else if(rol.equals(Globals.CLIENTE)) {
            switch (position) {
                case 0:
                    ft.replace(R.id.content_frame, new PantallaInicio());
                    break;
                case 1:
                    ft.replace(R.id.content_frame, new VisualizarMapa());
                    break;
                case 2:
                    ft.replace(R.id.content_frame, new RoutesFragment());
                    break;
                case 3:
                    ft.replace(R.id.content_frame, new CrearRuta());
                    break;
                case 4:
                    ft.replace(R.id.content_frame, new Favoritas());
                    break;
                case 5:
                    ft.replace(R.id.content_frame, new LoginFragment());
                    break;
                case 6:
                    Cerrar_Sesion();
                    logout = false;
                    break;
            }
        }
        else{
            switch (position) {
                case 0:
                    ft.replace(R.id.content_frame, new PantallaInicio());
                    break;
                case 1:
                    ft.replace(R.id.content_frame, new VisualizarMapa());
                    break;
                case 2:
                    ft.replace(R.id.content_frame, new LoginFragment());
                    break;
            }
        }
        //restoreActionBar();
        if(logout) {

        }
        ft.commit();
        mDrawerList.setItemChecked(position, true);
        // Get the title followed by the position
        setTitle(title[position]);
        // Close drawer
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
// Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
// Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public void onBackPressed() {

        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
    // If there are back-stack entries, leave the FragmentActivity
    // implementation take care of them.
            manager.popBackStack();

        } else {
    // Otherwise, ask user if he wants to leave :)
            super.onBackPressed();
        }
    }

    public void onSectionAttached(int number) {
            }


    @Override
    public void onFragmentInteraction(String id) {

    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "devtrek-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    @Override
    public void goToRegister(String email, String password) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, RegisterFragment.newInstance(email, password));
        ft.commit();

    }

    @Override
    public void signup(Usuario client) {
        Toast.makeText(getApplicationContext(), "Crear cuenta", Toast.LENGTH_LONG).show();
    }

    @Override
    public void login(String email, String password) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame,  LoginFragment.newInstance(email, password));
        ft.commit();

    }

    public Context context()
    {
        return this;
    }

    @Override
    public void goToHome() {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }

    public void Cerrar_Sesion()
    {
        SharedPreferences sp = this.getSharedPreferences(Globals.PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Globals.EMAIL, "VACIO");
        editor.putString(Globals.NOMBRE, "VACIO");
        editor.putString(Globals.ROL, Globals.INVITADO);
        editor.commit();
        goToHome();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void idioma(String i)
    {
        String languageToLoad  = i;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        goToHome();

    }

}
