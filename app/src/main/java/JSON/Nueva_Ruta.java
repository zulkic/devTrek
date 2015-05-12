package JSON;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import greendao.Ruta;
//clase para crear una nueva ruta
public class Nueva_Ruta extends AsyncTask<Void, Void, Void> {

    private Ruta ruta;
    private JSONParser jsonParser;
    private static String url_agregar_ruta = "http://trythistrail.16mb.com/agregar_ruta.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    public Nueva_Ruta(Ruta ruta)
    {
        this.ruta = ruta;
        this.jsonParser = new JSONParser();
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
     protected Void doInBackground(Void... args) {
        String nombre =  this.ruta.getNombre();
        String descripcion = this.ruta.getDescripcion();
        String kms = this.ruta.getKms().toString();
        String tiempo_estimado = this.ruta.getTiempo_estimado();
        String oficial = this.ruta.getOficial().toString();

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("nombre", nombre));
        params.add(new BasicNameValuePair("descripcion", descripcion));
        params.add(new BasicNameValuePair("kms", kms));
        params.add(new BasicNameValuePair("tiempo_estimado", tiempo_estimado));
        params.add(new BasicNameValuePair("oficial", oficial));

        // getting JSON Object
        // Note that create product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(url_agregar_ruta,
                "POST", params);

        // check log cat fro response
        Log.d("Create Response", json.toString());

        // check for success tag
        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // successfully created product
                Log.i("nueva_ruta", "creada correctamente");
            } else {
                // failed to create product
                Log.i("nueva_ruta", "algo fallo");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {
        Log.d("post execute", "termine");
    }

}