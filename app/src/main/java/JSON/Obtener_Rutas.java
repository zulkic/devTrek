package JSON;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import greendao.Ruta;
import greendao.Sync;
import repositorios.RutaRepo;
import repositorios.SyncRepo;

//clase para obtener rutas
public class Obtener_Rutas extends AsyncTask<Void, Void, ArrayList<Ruta>> {

    private ArrayList<Ruta> rutasList;
    private Context context;
    private JSONParser jsonParser;
    private String tiempo;
    private static String url_obtener_rutas = "http://trythistrail.16mb.com/obtener_rutas.php";
    private static String url_obtener_rutas_invitado = "http://trythistrail.16mb.com/obtener_rutas_invitado.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TIEMPO_SYNC = "tiempo";
    private static final String TAG_RUTAS = "rutas";
    private static final String TAG_ID = "id_ruta";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_DESCRIPCION = "descripcion";
    private static final String TAG_KMS = "kms";
    private static final String TAG_TIEMPO_ESTIMADO = "tiempo_estimado";
    private static final String TAG_OFICIAL = "oficial";
    private static final String TAG_REGION = "id_region";
    private static final String TAG_TIPO = "tipo";
    private String tiempo_sync;
    private Sync sync;
    private SharedPreferences pref;
    private String rol;

    /**
     * Before starting background thread Show Progress Dialog
     */

    public Obtener_Rutas(Context context)
    {
        this.context = context;
        this.rutasList = new ArrayList<Ruta>();
        this.jsonParser = new JSONParser();
        this.sync = SyncRepo.getSyncForId(context, (long) 1);
        Tiempo_Sync task_tiempo = new Tiempo_Sync(context);
        try {
            tiempo_sync = task_tiempo.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * getting All products from url
     */
    protected ArrayList<Ruta> doInBackground(Void... args) {
        if(!tiempo_sync.equals("error") && !tiempo_sync.equals(this.sync.getTiempo())){
            // Building Parameters
            pref = context.getSharedPreferences(Globals.PREF, Context.MODE_PRIVATE);
            rol = pref.getString(Globals.ROL, "invitado");

            Log.i("obtener rutas online: ", "rutas obtenidas online");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json;
            Log.i("rol: "," rol obtener rutas");
            if(!rol.equals("invitado"))
                json = jsonParser.makeHttpRequest(url_obtener_rutas, "GET", params);
            else
                json = jsonParser.makeHttpRequest(url_obtener_rutas_invitado, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Rutas: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                this.tiempo = json.getString(TAG_TIEMPO_SYNC);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    JSONArray rutas = json.getJSONArray(TAG_RUTAS);

                    // looping through All Products
                    for (int i = 0; i < rutas.length(); i++) {
                        JSONObject c = rutas.getJSONObject(i);
                        Ruta ruta = new Ruta();
                        // Storing each json item in variable
                        Integer id = Integer.parseInt(c.getString(TAG_ID));
                        ruta.setId( Long.valueOf(id) );
                        ruta.setNombre(c.getString(TAG_NOMBRE));
                        ruta.setDescripcion(c.getString(TAG_DESCRIPCION));
                        ruta.setKms(Float.parseFloat(c.getString(TAG_KMS)));
                        ruta.setTiempo_estimado(c.getString(TAG_TIEMPO_ESTIMADO));
                        Integer oficial = Integer.parseInt(c.getString(TAG_OFICIAL));
                        if(oficial == 1)
                        {
                            ruta.setOficial(true);
                        }
                        else{
                            ruta.setOficial(false);
                        }
                        ruta.setId_region(Integer.parseInt(c.getString(TAG_REGION)));
                        ruta.setTipo(c.getString(TAG_TIPO));
                        ruta.setSincronizada(true);
                        ruta.setFavorita(false);
                        Log.i("obtener ruta: ", ruta.getOficial().toString());
                        this.rutasList.add(ruta);
                    }
                    Log.i("Vamos a guardar: ", "guardaremos las rutas en sqlite");
                    Guardar_Rutas guardar_rutas = new Guardar_Rutas(this.rutasList, context);
                    guardar_rutas.execute();
                    if(!rol.equals("invitado")) {
                        this.sync.setTiempo(tiempo);
                        SyncRepo.insertOrUpdate(context, this.sync);
                    }
                }
                for(Ruta ruta : RutaRepo.getAllRutas(context)) {
                    if(!ruta.getSincronizada()) {
                        this.rutasList.add(ruta);
                        Log.i("Ruta local:", "ruta local agregada al obtener rutas");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Log.i("obtener rutas offline: ", "rutas obtenidas offline");
            for (Ruta ruta : RutaRepo.getAllRutas(context)) {
                Log.i("nombre: ", ruta.getNombre());
                Log.i("oficial", ruta.getOficial().toString());
                this.rutasList.add(ruta);
            }
        }
        return rutasList;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * *
     */

    protected void onPostExecute(ArrayList<Ruta> result) {
        super.onPostExecute(result);
    }
}