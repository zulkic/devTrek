package JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import greendao.Obstaculo;
import repositorios.ObstaculoRepo;

/**
 * Created by juancarlosgonzalezca on 27-05-2015.
 */
public class Nuevo_Obstaculo extends AsyncTask<Void, Void, Void> {

    private Obstaculo obstaculo;
    private JSONParser jsonParser;
    private Context context;
    private static String url_agregar_obstaculo = "http://trythistrail.16mb.com/agregar_obstaculo.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    public Nuevo_Obstaculo(Obstaculo obstaculo, Context context)
    {
        this.obstaculo = obstaculo;
        this.jsonParser = new JSONParser();
        this.context = context;
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

        hasInternet conexion = new hasInternet(this.context);
        Boolean internet = conexion.getInternet();
        Integer id = 0;

        if(internet) {
            String descripcion = this.obstaculo.getDescripcion();
            Integer id_tipo_obstaculo = this.obstaculo.getId_tipo_obstaculo();
            Double latitud = this.obstaculo.getLatitud();
            Double longitud = this.obstaculo.getLongitud();
            Integer id_ruta = this.obstaculo.getId_ruta();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("descripcion", descripcion));
            params.add(new BasicNameValuePair("id_tipo_obstaculo", id_tipo_obstaculo.toString()));
            params.add(new BasicNameValuePair("latitud", latitud.toString()));
            params.add(new BasicNameValuePair("longitud", longitud.toString()));
            params.add(new BasicNameValuePair("id_ruta", id_ruta.toString()));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_agregar_obstaculo,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully created product
                    Log.i("nuev obstaculo", "creado correctamente");
                } else {
                    // failed to create product
                    Log.i("nuevo obstaculo", "algo fallo");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            ObstaculoRepo.insertOrUpdate(this.context, obstaculo);
        }
        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

    }

}
