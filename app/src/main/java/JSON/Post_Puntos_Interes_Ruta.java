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

import greendao.Punto_interes;
import repositorios.Punto_interesRepo;

/**
 * Created by juancarlosgonzalezca on 28-05-2015.
 */
public class Post_Puntos_Interes_Ruta extends AsyncTask<Void, Void, Void> {

    private ArrayList<Punto_interes> puntos_interes;
    private Context context;
    private JSONParser jsonParser;
    private static String url_agregar_punto_interes_ruta = "http://trythistrail.16mb.com/agregar_punto_interes.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private Wrapper wp;

    public Post_Puntos_Interes_Ruta(ArrayList<Punto_interes> puntos_interes, Context context, Wrapper wp)
    {
        this.puntos_interes = puntos_interes;
        this.wp = wp;
        this.context = context;
        this.jsonParser = new JSONParser();
        int id = wp.getId();
        for (Punto_interes punto_interes : puntos_interes) {
            punto_interes.setId_ruta(id);
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
            for(Punto_interes punto_interes : this.puntos_interes) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("descripcion", punto_interes.getDescripcion()));
                params.add(new BasicNameValuePair("id_tipo_punto_interes", punto_interes.getId_tipo_punto_interes().toString() ));
                params.add(new BasicNameValuePair("latitud", punto_interes.getLatitud().toString() ));
                params.add(new BasicNameValuePair("longitud", punto_interes.getLongitud().toString() ));
                params.add(new BasicNameValuePair("id_ruta", punto_interes.getId_ruta().toString()));
                JSONObject json = jsonParser.makeHttpRequest(url_agregar_punto_interes_ruta,
                        "POST", params);

                // check log cat fro response
                Log.d("Create Response", json.toString());

                // check for success tag
                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        // successfully created product
                        Log.i("puntos interes ruta", " creados correctamente");
                    } else {
                        // failed to create product
                        Log.i("puntos interes ruta", " algo fallo");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Guardar_Puntos_interes guardar_puntos_interes = new Guardar_Puntos_interes(puntos_interes, context, wp.getId().longValue());
            guardar_puntos_interes.execute();
        }
        else
        {
            for(Punto_interes punto_interes : this.puntos_interes)
            {
                Punto_interesRepo.insertOrUpdate(context, punto_interes);
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
