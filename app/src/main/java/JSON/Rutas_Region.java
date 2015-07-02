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

import greendao.Ruta;
import repositorios.RutaRepo;

/**
 * Created by juancarlosgonzalezca on 02-07-2015.
 */
public class Rutas_Region extends AsyncTask<Void, Void, ArrayList<Ruta>> {

    private ArrayList<Ruta> rutasList;
    private Integer id;
    private Context context;
    private JSONParser jsonParser;
    private static String url_rutas_por_region = "http://trythistrail.16mb.com/rutas_region.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RUTAS = "rutas";
    private static final String TAG_ID = "id_ruta";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_DESCRIPCION = "descripcion";
    private static final String TAG_KMS = "kms";
    private static final String TAG_TIEMPO_ESTIMADO = "tiempo_estimado";
    private static final String TAG_OFICIAL = "oficial";
    private static final String TAG_REGION = "id_region";
    private static final String TAG_TIPO = "tipo";
    private Boolean internet;

    /**
     * Before starting background thread Show Progress Dialog
     */

    public Rutas_Region(Integer id, Context context)
    {
        this.id = id;
        this.context = context;
        this.rutasList = new ArrayList<Ruta>();
        this.jsonParser = new JSONParser();
        List<Ruta> aux = RutaRepo.getAllRutas(context);
        if(RutaRepo.isValid(context, id.longValue()) == 1 && aux.size() > 0)
        {
            for(Ruta ruta : aux)
            {
                this.rutasList.add(ruta);
            }
            internet = false;
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

    protected ArrayList<Ruta> doInBackground(Void... args) {
        if(internet) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_region", Integer.toString(id)));
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_rutas_por_region, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("Rutas region: ", json.toString());

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
                        this.rutasList.add(ruta);
                    }
                    Guardar_Rutas task_guardar = new Guardar_Rutas(this.rutasList,context);
                    task_guardar.execute();
                }
                Log.i("coordenadas: ", "obtenidas de manera externa");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rutasList;
    }

    protected void onPostExecute(ArrayList<Ruta> result) {
        super.onPostExecute(result);
    }
}
