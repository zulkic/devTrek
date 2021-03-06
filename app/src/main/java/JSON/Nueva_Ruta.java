package JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mapas.franciscojavier.trekkingroute.Utility.Wrapper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import greendao.Ruta;
import repositorios.RutaRepo;

//clase para crear una nueva ruta
public class Nueva_Ruta extends AsyncTask<Void, Void, Wrapper> {

    private Ruta ruta;
    private JSONParser jsonParser;
    private Context context;
    private static String url_agregar_ruta = "http://trythistrail.16mb.com/agregar_ruta.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ID= "id_ruta";
    private Boolean internet;
    private Integer id = 0;

    public Nueva_Ruta(Ruta ruta, Context context)
    {
        this.ruta = ruta;
        this.jsonParser = new JSONParser();
        this.context = context;
        hasInternet conexion = new hasInternet(this.context);
        try {
            internet = conexion.execute().get();
        }
        catch(Exception e)
        {
            internet = false;
        }
    }
    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Creating product
     * */
    @Override
     protected Wrapper doInBackground(Void... args) {

        if(internet) {
            Log.i("nueva ruta: ", "agregando una nueva ruta");
            String nombre = this.ruta.getNombre();
            String descripcion = this.ruta.getDescripcion();
            String kms = this.ruta.getKms().toString();
            String tiempo_estimado = this.ruta.getTiempo_estimado();
            String oficial = this.ruta.getOficial().toString();
            String id_region = this.ruta.getId_region().toString();
            String tipo = this.ruta.getTipo();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nombre", nombre));
            params.add(new BasicNameValuePair("descripcion", descripcion));
            params.add(new BasicNameValuePair("kms", kms));
            params.add(new BasicNameValuePair("tiempo_estimado", tiempo_estimado));
            params.add(new BasicNameValuePair("oficial", oficial));
            params.add(new BasicNameValuePair("id_region", id_region));
            params.add(new BasicNameValuePair("tipo", tipo));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_agregar_ruta,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
                id = json.getInt(TAG_ID);
                if (success != 0) {
                    // successfully created product
                    Log.i("nueva_ruta", "creada correctamente " + id.toString() );
                } else {
                    // failed to create product
                    Log.i("nueva_ruta", "algo fallo");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ruta.setSincronizada(true);
            ruta.setFavorita(false);
            ruta.setId(id.longValue());
            Guardar_Rutas guardar_rutas = new Guardar_Rutas(ruta, context);
            guardar_rutas.execute();
        }
        else {
            ruta.setSincronizada(false);
            ruta.setFavorita(false);
            id = RutaRepo.insertOrUpdate(this.context,ruta);
            Log.i("id ruta offnet: ", id.toString());
        }
        Wrapper wrapper = new Wrapper(id, internet);
        return wrapper;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(Wrapper result) {
        super.onPostExecute(result);

    }

}