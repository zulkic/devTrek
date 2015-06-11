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

import greendao.Coordenada;
import repositorios.CoordenadaRepo;

//clase para crear una nueva ruta
public class Post_Coordenadas_Ruta extends AsyncTask<Void, Void, Void> {

    private ArrayList<Coordenada> coordenadas;
    private Context context;
    private JSONParser jsonParser;
    private static String url_agregar_coordenadas_ruta = "http://trythistrail.16mb.com/agregar_coordenadas_ruta.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    public Post_Coordenadas_Ruta(ArrayList<Coordenada> coordenadas, Context context)
    {
        this.coordenadas = coordenadas;
        this.context = context;
        this.jsonParser = new JSONParser();
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

        if(internet) {
            // Building Parameters
            for(Coordenada coordenada : this.coordenadas) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("latitud", coordenada.getLatitud().toString()));
                params.add(new BasicNameValuePair("longitud", coordenada.getLongitud().toString() ));
                params.add(new BasicNameValuePair("altitud", coordenada.getAltitud().toString()));
                params.add(new BasicNameValuePair("id_ruta", coordenada.getId_ruta().toString()));
                params.add(new BasicNameValuePair("posicion", coordenada.getPosicion().toString() ));
                JSONObject json = jsonParser.makeHttpRequest(url_agregar_coordenadas_ruta,
                        "POST", params);

                // check log cat fro response
                Log.d("Create Response", json.toString());

                // check for success tag
                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        // successfully created product
                        Log.i("coordenadas ruta", "creadas correctamente");
                    } else {
                        // failed to create product
                        Log.i("coordenadas ruta", "algo fallo");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            for(Coordenada coordenada : this.coordenadas)
            {
                CoordenadaRepo.insertOrUpdate(context,coordenada);
            }
        }
        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {

    }

}