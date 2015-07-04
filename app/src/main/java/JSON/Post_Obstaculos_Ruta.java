package JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mapas.franciscojavier.trekkingroute.Utility.Wrapper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import greendao.Obstaculo;
import repositorios.ObstaculoRepo;

/**
 * Created by juancarlosgonzalezca on 28-05-2015.
 */
public class Post_Obstaculos_Ruta extends AsyncTask<Void, Void, Void> {

    private ArrayList<Obstaculo> obstaculos;
    private Context context;
    private JSONParser jsonParser;
    private static String url_agregar_obstaculo_ruta = "http://trythistrail.16mb.com/agregar_obstaculo.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private Wrapper wp;

    public Post_Obstaculos_Ruta(ArrayList<Obstaculo> obstaculos, Context context, Wrapper wp)
    {
        this.obstaculos = obstaculos;
        this.wp = wp;
        this.context = context;
        this.jsonParser = new JSONParser();
        int id = wp.getId();
        for (Obstaculo obstaculo : obstaculos) {
            obstaculo.setId_ruta(id);
        }
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

        if(wp.getInternet()) {
            // Building Parameters
            for(Obstaculo obstaculo : this.obstaculos) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("descripcion", obstaculo.getDescripcion()));
                params.add(new BasicNameValuePair("id_tipo_obstaculo", obstaculo.getId_tipo_obstaculo().toString() ));
                params.add(new BasicNameValuePair("latitud", obstaculo.getLatitud().toString() ));
                params.add(new BasicNameValuePair("longitud", obstaculo.getLongitud().toString() ));
                params.add(new BasicNameValuePair("id_ruta", obstaculo.getId_ruta().toString()));
                JSONObject json = jsonParser.makeHttpRequest(url_agregar_obstaculo_ruta,
                        "POST", params);

                // check log cat fro response
                Log.d("Create Response", json.toString());

                // check for success tag
                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        // successfully created product
                        Log.i("obstaculo ruta", " creados correctamente");
                    } else {
                        // failed to create product
                        Log.i("obstaculo ruta", " algo fallo");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Guardar_Obstaculos guardar_obstaculos = new Guardar_Obstaculos(obstaculos, context, wp.getId().longValue());
            guardar_obstaculos.execute();
        }
        else
        {
            for(Obstaculo obsculo : this.obstaculos)
            {
                ObstaculoRepo.insertOrUpdate(context, obsculo);
            }
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
