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

import greendao.Tipo_obstaculo;
import greendao.Tipo_punto_interes;
import repositorios.Tipo_ObstaculoRepo;
import repositorios.Tipo_Puntos_InteresRepo;

/**
 * Created by juancarlosgonzalezca on 27-05-2015.
 */
public class Sincronizar_Tipos_Indicadores extends AsyncTask<Void, Void, Void> {

    private Context context;
    private JSONParser jsonParser;
    private static String url_obtener_tipos_obstaculos = "http://trythistrail.16mb.com/obtener_tipos_obstaculos.php";
    private static String url_obtener_tipos_puntos_interes = "http://trythistrail.16mb.com/obtener_tipos_puntos_interes.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TIPOS_OBSTACULOS = "tipos_obstaculos";
    private static final String TAG_TIPOS_PUNTOS_INTERES = "tipos_puntos_interes";
    private static final String TAG_ID_TIPO_OBSTACULO = "id_tipo_obstaculo";
    private static final String TAG_ID_TIPO_PUNTO_INTERES = "id_tipo_punto_interes";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_ICONO = "nombre_icono";
    private Boolean internet;

    /**
     * Before starting background thread Show Progress Dialog
     */

    public Sincronizar_Tipos_Indicadores(Context context)
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
            JSONObject json = jsonParser.makeHttpRequest(url_obtener_tipos_obstaculos, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Tipos Obstaculos: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    JSONArray tipos_obstaculos = json.getJSONArray(TAG_TIPOS_OBSTACULOS);

                    // looping through All Products
                    for (int i = 0; i < tipos_obstaculos.length(); i++) {
                        JSONObject c = tipos_obstaculos.getJSONObject(i);
                        Tipo_obstaculo tipo_obstaculo = new Tipo_obstaculo();
                        // Storing each json item in variable
                        Integer id = Integer.parseInt(c.getString(TAG_ID_TIPO_OBSTACULO));
                        tipo_obstaculo.setId(Long.valueOf(id));
                        tipo_obstaculo.setNombre(c.getString(TAG_NOMBRE));
                        tipo_obstaculo.setNombre_icono(c.getString(TAG_ICONO));
                        Tipo_ObstaculoRepo.insertOrUpdate(this.context, tipo_obstaculo);
                    }
                    Log.i("Tipos obstaculos: "," sincronizados exitosamente");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch(Exception e)
            {
                Log.i("error: ", e.toString());
            }

            params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            json = jsonParser.makeHttpRequest(url_obtener_tipos_puntos_interes, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("Tipos puntos interes: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    JSONArray tipos_puntos_interes = json.getJSONArray(TAG_TIPOS_PUNTOS_INTERES);

                    // looping through All Products
                    for (int i = 0; i < tipos_puntos_interes.length(); i++) {
                        JSONObject c = tipos_puntos_interes.getJSONObject(i);
                        Tipo_punto_interes tipo_punto_interes = new Tipo_punto_interes();
                        // Storing each json item in variable
                        Integer id = Integer.parseInt(c.getString(TAG_ID_TIPO_PUNTO_INTERES));
                        tipo_punto_interes.setId(Long.valueOf(id));
                        tipo_punto_interes.setNombre(c.getString(TAG_NOMBRE));
                        tipo_punto_interes.setNombre_icono(c.getString(TAG_ICONO));
                        Tipo_Puntos_InteresRepo.insertOrUpdate(this.context, tipo_punto_interes);
                    }
                    Log.i("Tipos puntos: "," sincronizados exitosamente");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * *
     */

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
