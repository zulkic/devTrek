package JSON;

/**
 * Created by juancarlosgonzalezca on 13-05-2015.
 */
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import greendao.Ruta;

public class Modificar_Ruta extends AsyncTask<Void, Void, Void> {

    private Ruta ruta;
    private JSONParser jsonParser;
    private static String url_modificar_ruta = "http://trythistrail.16mb.com/modificar_ruta.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    public Modificar_Ruta(Ruta ruta)
    {
        this.ruta = ruta;
        this.jsonParser = new JSONParser();
    }

    /**
     * Saving product
     * */
    protected Void doInBackground(Void... args) {

        // getting updated data from EditTexts
        String id_ruta = this.ruta.getId().toString();
        String nombre =  this.ruta.getNombre();
        String descripcion = this.ruta.getDescripcion();
        String kms = this.ruta.getKms().toString();
        String tiempo_estimado = this.ruta.getTiempo_estimado();
        String oficial = this.ruta.getOficial().toString();

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_ruta", id_ruta));
        params.add(new BasicNameValuePair("nombre", nombre));
        params.add(new BasicNameValuePair("descripcion", descripcion));
        params.add(new BasicNameValuePair("kms", kms));
        params.add(new BasicNameValuePair("tiempo_estimado", tiempo_estimado));
        params.add(new BasicNameValuePair("oficial", oficial));

        // sending modified data through http request
        // Notice that update product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(url_modificar_ruta,
                "POST", params);

        // check json success tag
        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // successfully created product
                Log.i("ruta modificada", "modificada correctamente");
            } else {
                // failed to create product
                Log.i("ruta modificada", "algo fallo");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(Void file_url) {
    }
}