package com.mapas.franciscojavier.trekkingroute;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapas.franciscojavier.trekkingroute.Account.LoginFragment;
import com.mapas.franciscojavier.trekkingroute.Account.MainCalls;
import com.mapas.franciscojavier.trekkingroute.Account.RegisterFragment;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import JSON.Sincronizar_Tipos_Indicadores;
import greendao.DaoMaster;
import greendao.DaoSession;
import greendao.Sync;
import greendao.Usuario;
import repositorios.SyncRepo;
import repositorios.Tipo_ObstaculoRepo;
import repositorios.Tipo_Puntos_InteresRepo;


public class MenuPrincipal extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        RoutesFragment.OnFragmentInteractionListener,EliminarRuta.OnFragmentInteractionListener,DetalleIndicadorFragment.OnFragmentInteractionListener, MainCalls {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public DaoSession daoSession;
    private SharedPreferences pref;
    private String rol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDatabase();
        pref = this.getSharedPreferences(Globals.PREF, Context.MODE_PRIVATE);;
        rol = pref.getString(Globals.ROL, "invitado");
        Log.i("rol: ", rol);
        setContentView(R.layout.activity_menu_principal);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if(SyncRepo.getSyncForId(this, (long) 1) == null)
        {
            Sync sync = new Sync();
            sync.setId((long) 1);
            sync.setTabla("rutas");
            sync.setTiempo("inicializado");
            SyncRepo.insertOrUpdate(this, sync);
            Log.i("Sync ini", "sync");
        }
        else
        {
            Log.i("Sync ini", "else");
        }

        try{
            if(Tipo_ObstaculoRepo.getAllTipos_Obstaculos(this).size() == 0  || Tipo_Puntos_InteresRepo.getAllTipos_Puntos_Interes(this).size() == 0 ) {
                Sincronizar_Tipos_Indicadores tarea = new Sincronizar_Tipos_Indicadores(this);
                tarea.execute();
                Log.i("Sincrinizar tipos", " exito");
            }

        }
        catch (Exception e)
        {
            Log.i("error: ", e.toString());
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        onSectionAttached(position);
        Fragment fragment= null;
        FragmentManager fragmentManager = getFragmentManager();
        Boolean logout = true;
        if(rol.equals(Globals.ADMIN)) {
            switch (position) {
                case 0:
                    fragment = new PantallaInicio();
                    break;
                case 1:
                    fragment = new VisualizarMapa();
                    break;
                case 2:
                    fragment = new RoutesFragment();
                    break;
                case 3:
                    fragment = new CrearRuta();
                    break;
                case 4:
                    fragment = new DetalleIndicadorFragment();
                    break;
                case 5:
                    fragment = new LoginFragment();
                    //Intent intent = new Intent(MenuPrincipal.this, LoginActivity.class);
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
                    fragment = new PantallaInicio();
                    break;
                case 1:
                    fragment = new VisualizarMapa();
                    break;
                case 2:
                    fragment = new RoutesFragment();
                    break;
                case 3:
                    fragment = new CrearRuta();
                    break;
                case 4:
                    fragment = new DetalleIndicadorFragment();
                    break;
                case 5:
                    fragment = new LoginFragment();
                    //Intent intent = new Intent(MenuPrincipal.this, LoginActivity.class);
                    break;
                case 6:
                    fragment = new Histograma();
                    //Intent intent = new Intent(MenuPrincipal.this, LoginActivity.class);
                    break;
                case 7:
                    Cerrar_Sesion();
                    logout = false;
                    break;
            }
        }
        else{
            switch (position) {
                case 0:
                    fragment = new PantallaInicio();
                    break;
                case 1:
                    fragment = new VisualizarMapa();
                    break;
                case 2:
                    fragment = new LoginFragment();
                    //Intent intent = new Intent(MenuPrincipal.this, LoginActivity.class);
                    break;
            }
        }
        //restoreActionBar();
        if(logout) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    public void onSectionAttached(int number) {
        if(rol.equals(Globals.ADMIN)) {
            switch (number) {
                case 0:
                    mTitle = getString(R.string.title_section0);
                    break;
                case 1:
                    mTitle = getString(R.string.title_section1);
                    break;
                case 2:
                    mTitle = getString(R.string.title_section2);
                    break;
                case 3:
                    mTitle = getString(R.string.title_section3);
                    break;
                case 4:
                    mTitle = getString(R.string.title_section4);
                    break;
                case 5:
                    mTitle = getString(R.string.title_section6);
                    break;

            }
        }
        else if(rol.equals(Globals.CLIENTE)) {
            switch (number) {
                case 0:
                    mTitle = getString(R.string.title_section0);
                    break;
                case 1:
                    mTitle = getString(R.string.title_section1);
                    break;
                case 2:
                    mTitle = getString(R.string.title_section2);
                    break;
                case 3:
                    mTitle = getString(R.string.title_section3);
                    break;
                case 4:
                    mTitle = getString(R.string.title_section4);
                    break;
                case 5:
                    mTitle = getString(R.string.title_section6);
                    break;
                case 6:
                    mTitle = "Histograma";
                    break;
            }
        }
        else {
            switch (number) {
                case 0:
                    mTitle = getString(R.string.title_section0);
                    break;
                case 1:
                    mTitle = getString(R.string.title_section1);
                    break;
                case 2:
                    mTitle = getString(R.string.title_section5);
                    break;
            }
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_principal, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
    public void onFragmentInteraction(String id) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_menu_principal, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MenuPrincipal) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
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
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, RegisterFragment.newInstance(email, password));
        ft.addToBackStack("LOGIN");
        ft.commit();
    }

    @Override
    public void signup(Usuario client) {
        Toast.makeText(getApplicationContext(), "Crear cuenta", Toast.LENGTH_LONG).show();
    }

    @Override
    public void login(String email, String password) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, LoginFragment.newInstance(email, password));
        ft.addToBackStack("LOGIN");
        ft.commit();

    }
    @Override
    public void goToHome() {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }
    @Override
      public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
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


}
