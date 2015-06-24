package com.mapas.franciscojavier.trekkingroute;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mapas.franciscojavier.trekkingroute.Account.LoginFragment;
import com.mapas.franciscojavier.trekkingroute.Account.MainCalls;
import com.mapas.franciscojavier.trekkingroute.Account.RegisterFragment;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import java.util.ArrayList;

import JSON.Sincronizar_Rutas;
import JSON.Sincronizar_Tipos_Indicadores;
import greendao.DaoMaster;
import greendao.DaoSession;
import greendao.Sync;
import greendao.Usuario;
import repositorios.SyncRepo;
import repositorios.Tipo_ObstaculoRepo;
import repositorios.Tipo_Puntos_InteresRepo;


public class MenuPrincipal extends Activity
        implements //NavigationDrawerFragment.NavigationDrawerCallbacks,
        RoutesFragment.OnFragmentInteractionListener,EliminarRuta.OnFragmentInteractionListener, MainCalls,Favoritas.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    //private NavigationDrawerFragment mNavigationDrawerFragment;
//     DECLARACIONES del NUEVO NAVIGATION
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence activityTitle;
    private CharSequence itemTitle;
    private String[] tagTitles;
    /**
     * Used to store the last screen title. For use in {@link ()}.
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
        Globals.context = this;
        setContentView(R.layout.activity_menu_principal);

//        mNavigationDrawerFragment = (NavigationDrawerFragment)
//                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
//        mTitle = getTitle();

        // Set up the drawer.
//        mNavigationDrawerFragment.setUp(
//                R.id.navigation_drawer,
//                (DrawerLayout) findViewById(R.id.drawer_layout));
        itemTitle = activityTitle = getTitle();
        tagTitles = getResources().getStringArray(R.array.Tags);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);// Setear una sombra sobre el contenido principal cuando el drawer se despliegue
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        //Crear elementos de la lista
        ArrayList<DrawerItem> items = new ArrayList<DrawerItem>();

        if(rol.equals(Globals.ADMIN)) {
            items.add(new DrawerItem(tagTitles[0],R.drawable.ic_ruby));
            items.add(new DrawerItem(tagTitles[1],R.drawable.ic_ruby));
            items.add(new DrawerItem(tagTitles[2],R.drawable.ic_ruby));
            items.add(new DrawerItem(tagTitles[3],R.drawable.ic_ruby));
            items.add(new DrawerItem(tagTitles[4],R.drawable.ic_ruby));
            items.add(new DrawerItem(tagTitles[6],R.drawable.ic_ruby));
            items.add(new DrawerItem(tagTitles[7],R.drawable.ic_ruby));
        }
        else if(rol.equals(Globals.CLIENTE)) {
            items.add(new DrawerItem(tagTitles[0],R.drawable.ic_ruby));
            items.add(new DrawerItem(tagTitles[1],R.drawable.ic_ruby));
            items.add(new DrawerItem(tagTitles[2],R.drawable.ic_ruby));
            items.add(new DrawerItem(tagTitles[3],R.drawable.ic_ruby));
            items.add(new DrawerItem(tagTitles[4],R.drawable.ic_ruby));
            items.add(new DrawerItem(tagTitles[6],R.drawable.ic_ruby));
            items.add(new DrawerItem(tagTitles[7],R.drawable.ic_ruby));
        }
        else{
            items.add(new DrawerItem(tagTitles[0],R.drawable.ic_ruby));
            items.add(new DrawerItem(tagTitles[1],R.drawable.ic_ruby));
            items.add(new DrawerItem(tagTitles[5],R.drawable.ic_ruby));
        }

        // Relacionar el adaptador y la escucha de la lista del drawer
        drawerList.setAdapter(new DrawerListAdapter(this, items));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Habilitar el icono de la app por si hay algún estilo que lo deshabilitó
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // Crear ActionBarDrawerToggle para la apertura y cierre
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(itemTitle);

                /*Usa este método si vas a modificar la action bar
                con cada fragmento
                 */
                //invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(activityTitle);

                /*Usa este método si vas a modificar la action bar
                con cada fragmento
                 */
                //invalidateOptionsMenu();
            }
        };
        //Seteamos la escucha
        drawerLayout.setDrawerListener(drawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

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

//    @Override
//    public void onNavigationDrawerItemSelected(int position) {
//    }

    public void onSectionAttached(int number) {
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
        if (drawerToggle.onOptionsItemSelected(item)) {
            // Toma los eventos de selección del toggle aquí
            return true;
        }
        //noinspection SimplifiableIfStatement
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* La escucha del ListView en el Drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // Reemplazar el contenido del layout principal por un fragmento


        // update the main content by replacing fragments
        onSectionAttached(position);
        Fragment fragment= null;
        FragmentManager fragmentManager = getFragmentManager();
        Boolean logout = true;
        Boolean flagInicioRecorrido = true;
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
                    fragment = new Favoritas();
                    /*Intent i = new Intent(this, FgmAcInicioRecorrido.class);
                    startActivity(i);
                    flagInicioRecorrido = false;*/
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
                    fragment = new Favoritas();
                    /*Intent i = new Intent(this, FgmAcInicioRecorrido.class);
                    startActivity(i);
                    flagInicioRecorrido = false;*/
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
            if(flagInicioRecorrido)
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }

        // Se actualiza el item seleccionado y el título, después de cerrar el drawer
        drawerList.setItemChecked(position, true);
        setTitle(tagTitles[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    /* Método auxiliar para setear el titulo de la action bar */
    @Override
    public void setTitle(CharSequence title) {
        itemTitle = title;
        getActionBar().setTitle(itemTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sincronizar el estado del drawer
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Cambiar las configuraciones del drawer si hubo modificaciones
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
