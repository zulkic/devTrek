package JSON;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import greendao.Ruta;
//clase para obtener rutas
public class Obtener_Rutas extends AsyncTask<Void, Void, ArrayList<Ruta>> {

    private ArrayList<Ruta> rutasList;
    private JSONParser jsonParser;
    private static String url_obtener_rutas = "http://trythistrail.16mb.com/obtener_rutas.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RUTAS = "rutas";
    private static final String TAG_ID = "id_ruta";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_DESCRIPCION = "descripcion";
    private static final String TAG_KMS = "kms";
    private static final String TAG_TIEMPO_ESTIMADO = "tiempo_estimado";
    private static final String TAG_OFICIAL = "oficial";

    /**
     * Before starting background thread Show Progress Dialog
     */

    public Obtener_Rutas()
    {
        this.rutasList = new ArrayList<Ruta>();
        this.jsonParser = new JSONParser();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * getting All products from url
     */
    protected ArrayList<Ruta> doInBackground(Void... args) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_obtener_rutas, "GET", params);

        // Check your log cat for JSON reponse
        Log.d("All Products: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // products found
                // Getting Array of Products
                JSONArray rutas = json.getJSONArray(TAG_RUTAS);

                // looping through All Products
                for (int i = 0; i < rutas.length(); i++) {
                    JSONObject c = rutas.getJSONObject(i);
                    Ruta ruta = new Ruta();
                    // Storing each json item in variable
                    ruta.setId((Long.getLong(c.getString(TAG_ID))));
                    ruta.setNombre(c.getString(TAG_NOMBRE));
                    ruta.setDescripcion(c.getString(TAG_DESCRIPCION));
                    ruta.setKms(Float.parseFloat(c.getString(TAG_KMS)));
                    ruta.setTiempo_estimado(c.getString(TAG_TIEMPO_ESTIMADO));
                    ruta.setOficial(Boolean.getBoolean(c.getString(TAG_OFICIAL)));

                    this.rutasList.add(ruta);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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