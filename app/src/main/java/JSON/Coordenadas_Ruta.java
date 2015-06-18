package JSON;

/**
 * Created by juancarlosgonzalezca on 13-05-2015.
 */

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

import greendao.Coordenada;
import repositorios.CoordenadaRepo;
import repositorios.RutaRepo;

//clase para obtener rutas
public class Coordenadas_Ruta extends AsyncTask<Void, Void, ArrayList<Coordenada>> {

    private ArrayList<Coordenada> coordenadasList;
    private Integer id;
    private Context context;
    private JSONParser jsonParser;
    private static String url_obtener_coordenadas_rutas = "http://trythistrail.16mb.com/coordenadas_ruta.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_COORDENADAS = "coordenadas";
    private static final String TAG_ID = "id_coordenada";
    private static final String TAG_LATITUD = "latitud";
    private static final String TAG_LONGITUD = "longitud";
    private static final String TAG_ALTITUD = "altitud";
    private static final String TAG_ID_RUTA = "id_ruta";
    private static final String TAG_POSICION = "posicion";

    /**
     * Before starting background thread Show Progress Dialog
     */

    public Coordenadas_Ruta(Integer id, Context context)
    {
        this.id = id;
        this.context = context;
        this.coordenadasList = new ArrayList<Coordenada>();
        this.jsonParser = new JSONParser();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * getting All products from url
     */
    protected ArrayList<Coordenada> doInBackground(Void... args) {
        hasInternet conexion = new hasInternet(this.context);
        Boolean internet = conexion.getInternet();

        if(internet) {
            List<Coordenada> aux = CoordenadaRepo.coordenadas_ruta(context, id.longValue());
            if(RutaRepo.isValid(context, id.longValue()) == 1 && !aux.isEmpty())
            {
                for(Coordenada coordenada : aux)
                {
                    this.coordenadasList.add(coordenada);
                }
                Log.i("coordenadas: ", "obtenidas de manera local");
            }
            else {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id_ruta", Integer.toString(id)));
                // getting JSON string from URL
                JSONObject json = jsonParser.makeHttpRequest(url_obtener_coordenadas_rutas, "GET", params);

                // Check your log cat for JSON reponse
                Log.d("Coordenadas ruta: ", json.toString());

                try {
                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        JSONArray coordenadas = json.getJSONArray(TAG_COORDENADAS);

                        // looping through All Products
                        for (int i = 0; i < coordenadas.length(); i++) {
                            JSONObject c = coordenadas.getJSONObject(i);
                            Coordenada coordenada = new Coordenada();
                            // Storing each json item in variable
                            coordenada.setId((Long.getLong(c.getString(TAG_ID))));
                            coordenada.setLatitud(Double.parseDouble(c.getString(TAG_LATITUD)));
                            coordenada.setLongitud(Double.parseDouble(c.getString(TAG_LONGITUD)));
                            coordenada.setAltitud(Integer.parseInt(c.getString(TAG_ALTITUD)));
                            coordenada.setId_ruta(Integer.parseInt(c.getString(TAG_ID_RUTA)));
                            coordenada.setPosicion(Integer.parseInt(c.getString(TAG_POSICION)));

                            this.coordenadasList.add(coordenada);
                        }
                        Guardar_Coordenadas task_guardar = new Guardar_Coordenadas(this.coordenadasList,context, id.longValue());
                        task_guardar.execute();
                    }
                    Log.i("coordenadas: ", "obtenidas de manera externa");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            for(Coordenada coordenada : CoordenadaRepo.coordenadas_ruta(context, id.longValue()))
            {
                this.coordenadasList.add(coordenada);
            }
            Log.i("coordenadas: ", "obtenidas de manera local");
        }
        return coordenadasList;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * *
     */

    protected void onPostExecute(ArrayList<Coordenada> result) {
        super.onPostExecute(result);
    }
}