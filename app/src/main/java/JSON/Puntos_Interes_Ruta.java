package JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import greendao.Punto_interes;
import repositorios.Punto_interesRepo;

/**
 * Created by juancarlosgonzalezca on 28-05-2015.
 */
public class Puntos_Interes_Ruta extends AsyncTask<Void, Void, ArrayList<Punto_interes>> {

    private ArrayList<Punto_interes> puntos_interesList;
    private Context context;
    private int id;
    private JSONParser jsonParser;
    private static String url_obtener_puntos_interes_ruta = "http://trythistrail.16mb.com/puntos_interes_ruta.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PUNTOS_INTERES = "puntos_interes";
    private static final String TAG_ID = "id_punto_interes";
    private static final String TAG_DESCRIPCION = "descripcion";
    private static final String TAG_ID_TIPO_PUNTO_INTERES = "id_tipo_punto_interes";
    private static final String TAG_LATITUD = "latitud";
    private static final String TAG_LONGITUD = "longitud";
    private static final String TAG_ID_RUTA = "id_ruta";

    /**
     * Before starting background thread Show Progress Dialog
     */

    public Puntos_Interes_Ruta(int id, Context context)
    {
        this.id = id;
        this.context = context;
        this.puntos_interesList = new ArrayList<Punto_interes>();
        this.jsonParser = new JSONParser();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * getting All products from url
     */
    protected ArrayList<Punto_interes> doInBackground(Void... args) {
        hasInternet conexion = new hasInternet(this.context);
        Boolean internet = conexion.getInternet();
        if(internet) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_ruta", Integer.toString(id)));
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_obtener_puntos_interes_ruta, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("Puntos interes ruta: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    JSONArray puntos_interes = json.getJSONArray(TAG_PUNTOS_INTERES);

                    // looping through All Products
                    for (int i = 0; i < puntos_interes.length(); i++) {
                        JSONObject c = puntos_interes.getJSONObject(i);
                        Punto_interes punto_interes = new Punto_interes();
                        // Storing each json item in variable
                        punto_interes.setId((Long.getLong(c.getString(TAG_ID))));
                        punto_interes.setDescripcion(c.getString(TAG_DESCRIPCION));
                        punto_interes.setId_tipo_punto_interes(Integer.parseInt(c.getString(TAG_ID_TIPO_PUNTO_INTERES)));
                        punto_interes.setLatitud(Double.parseDouble(c.getString(TAG_LATITUD)));
                        punto_interes.setLongitud(Double.parseDouble(c.getString(TAG_LONGITUD)));
                        punto_interes.setId_ruta(Integer.parseInt(c.getString(TAG_ID_RUTA)));

                        this.puntos_interesList.add(punto_interes);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            for(Punto_interes punto_interes : Punto_interesRepo.punto_intereses_ruta(id, context))
            {
                this.puntos_interesList.add(punto_interes);
            }
        }
        return puntos_interesList;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * *
     */

    protected void onPostExecute(ArrayList<Punto_interes> result) {
        super.onPostExecute(result);
    }
}
