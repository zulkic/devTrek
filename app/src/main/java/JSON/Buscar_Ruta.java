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

public class Buscar_Ruta extends AsyncTask<Void, Void, Ruta> {

    private Integer id;
    private Ruta ruta;
    private Context context;
    private JSONParser jsonParser;
    private static String url_buscar_ruta = "http://trythistrail.16mb.com/buscar_ruta.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RUTA = "ruta";
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

    public Buscar_Ruta(Integer id, Context context) {
        this.id = id;
        this.ruta = new Ruta();
        this.context = context;
        this.jsonParser = new JSONParser();
        Ruta aux = RutaRepo.getRutaForId(context, id.longValue());
        if(aux != null)
        {
            this.ruta = aux;
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

    /**
     * getting All products from url
     */
    protected Ruta doInBackground(Void... args) {

        if (internet) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_ruta", Integer.toString(id)));
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_buscar_ruta, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("Ruta: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    JSONArray result = json.getJSONArray(TAG_RUTA);
                    JSONObject c = result.getJSONObject(0);
                    // Storing each json item in variable
                    this.ruta.setId((Long.getLong(c.getString(TAG_ID))));
                    this.ruta.setNombre(c.getString(TAG_NOMBRE));
                    this.ruta.setDescripcion(c.getString(TAG_DESCRIPCION));
                    this.ruta.setKms(Float.parseFloat(c.getString(TAG_KMS)));
                    this.ruta.setTiempo_estimado(c.getString(TAG_TIEMPO_ESTIMADO));
                    Integer oficial = Integer.parseInt(c.getString(TAG_OFICIAL));
                    if (oficial == 1) {
                        ruta.setOficial(true);
                    } else {
                        ruta.setOficial(false);
                    }
                    ruta.setId_region(Integer.parseInt(c.getString(TAG_REGION)));
                    ruta.setTipo(c.getString(TAG_TIPO));
                    ruta.setSincronizada(true);
                    ruta.setFavorita(false);
                    int valid = RutaRepo.isValid(context, ruta.getId());
                    if (valid == -1) {
                        RutaRepo.insertOrUpdate(context, this.ruta);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ruta;
    }

    protected void onPostExecute(Ruta result) {
        super.onPostExecute(result);
    }
}
