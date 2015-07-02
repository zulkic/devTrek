package JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import greendao.Region;
import repositorios.RegionRepo;

/**
 * Created by juancarlosgonzalezca on 01-07-2015.
 */
public class Sincronizar_Regiones extends AsyncTask<Void, Void, Void> {

    private Context context;
    private JSONParser jsonParser;
    private static String url_obtener_regiones = "http://trythistrail.16mb.com/obtener_regiones.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_REGIONES = "regiones";
    private static final String TAG_ID = "id_region";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_ORDINAL = "ordinal";
    private Boolean internet;

    /**
     * Before starting background thread Show Progress Dialog
     */

    public Sincronizar_Regiones(Context context)
    {
        this.context = context;
        this.jsonParser = new JSONParser();
        hasInternet conexion = new hasInternet(this.context);
        try {
            internet = conexion.execute().get();
        }
        catch(Exception e)
        {
            internet = false;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * getting All products from url
     */
    protected Void doInBackground(Void... args) {

        if(internet) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_obtener_regiones, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Regiones: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    JSONArray regiones = json.getJSONArray(TAG_REGIONES);

                    // looping through All Products
                    for (int i = 0; i < regiones.length(); i++) {
                        JSONObject c = regiones.getJSONObject(i);
                        Region region = new Region();
                        // Storing each json item in variable
                        Integer id = Integer.parseInt(c.getString(TAG_ID));
                        region.setId(Long.valueOf(id));
                        region.setNombre(c.getString(TAG_NOMBRE));
                        region.setOrdinal(c.getString(TAG_ORDINAL));
                        RegionRepo.insertOrUpdate(this.context, region);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.i("error: ", e.toString());
            }
        }
        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}

