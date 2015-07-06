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

import greendao.Obstaculo;
import repositorios.ObstaculoRepo;
import repositorios.RutaRepo;

/**
 * Created by juancarlosgonzalezca on 28-05-2015.
 */
public class Obstaculos_Ruta extends AsyncTask<Void, Void, ArrayList<Obstaculo>> {

    private ArrayList<Obstaculo> obstaculosList;
    private Integer id;
    private Context context;
    private JSONParser jsonParser;
    private static String url_obtener_obstaculos_ruta = "http://trythistrail.16mb.com/obstaculos_ruta.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_OBSTACULOS = "obstaculos";
    private static final String TAG_ID = "id_obstaculo";
    private static final String TAG_DESCRIPCION = "descripcion";
    private static final String TAG_ID_TIPO_OBSTACULO = "id_tipo_obstaculo";
    private static final String TAG_LATITUD = "latitud";
    private static final String TAG_LONGITUD = "longitud";
    private static final String TAG_ID_RUTA = "id_ruta";
    private Boolean internet;

    /**
     * Before starting background thread Show Progress Dialog
     */

    public Obstaculos_Ruta(Integer id, Context context)
    {
        this.id = id;
        this.context = context;
        this.obstaculosList = new ArrayList<Obstaculo>();
        this.jsonParser = new JSONParser();
        List<Obstaculo> aux = ObstaculoRepo.obstaculos_ruta(context, id.longValue());
        if(RutaRepo.isValid(context, id.longValue()) != -1 && !aux.isEmpty())
        {
            for(Obstaculo obsculo : aux)
            {
                this.obstaculosList.add(obsculo);
            }
            internet = false;
            Log.i("Obstaculos: ", "obtenidos de manera local");
        }
        else
        {
            hasInternet conexion = new hasInternet(this.context);
            try {
                internet = conexion.execute().get();
            }
            catch(Exception e)
            {
                internet = false;
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * getting All products from url
     */
    protected ArrayList<Obstaculo> doInBackground(Void... args) {

        if(internet) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_ruta", Integer.toString(id)));
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_obtener_obstaculos_ruta, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("Obstaculos ruta: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    JSONArray obstaculos = json.getJSONArray(TAG_OBSTACULOS);

                    // looping through All Products
                    for (int i = 0; i < obstaculos.length(); i++) {
                        JSONObject c = obstaculos.getJSONObject(i);
                        Obstaculo obstaculo = new Obstaculo();
                        // Storing each json item in variable
                        obstaculo.setId((Long.getLong(c.getString(TAG_ID))));
                        obstaculo.setDescripcion(c.getString(TAG_DESCRIPCION));
                        obstaculo.setId_tipo_obstaculo(Integer.parseInt(c.getString(TAG_ID_TIPO_OBSTACULO)));
                        obstaculo.setLatitud(Double.parseDouble(c.getString(TAG_LATITUD)));
                        obstaculo.setLongitud(Double.parseDouble(c.getString(TAG_LONGITUD)));
                        obstaculo.setId_ruta(Integer.parseInt(c.getString(TAG_ID_RUTA)));
                        this.obstaculosList.add(obstaculo);
                    }
                    Guardar_Obstaculos task_guardar = new Guardar_Obstaculos(this.obstaculosList,context, id.longValue());
                    task_guardar.execute();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return obstaculosList;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * *
     */

    protected void onPostExecute(ArrayList<Obstaculo> result) {
        super.onPostExecute(result);
    }
}