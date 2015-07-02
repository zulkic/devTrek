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
import greendao.Punto_interes;
import greendao.Ruta;
import repositorios.CoordenadaRepo;
import repositorios.Punto_interesRepo;
import repositorios.RutaRepo;

public class Eliminar_Ruta extends AsyncTask<Void, Void, Void> {

    private Long id;
    private Context context;
    private JSONParser jsonParser;
    private static String url_eliminar_ruta = "http://trythistrail.16mb.com/eliminar_ruta.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private Boolean internet;

    public Eliminar_Ruta(Long id, Context context)
    {
        this.id = id;
        this.context = context;
        this.jsonParser = new JSONParser();
        Ruta ruta = RutaRepo.getRutaForId(context, id.longValue());
        if( ruta != null && ruta.getSincronizada() == false )
        {
            //Eliminar primero los puntos, obstaculos, coordenadas y luego la ruta
            for (Punto_interes punto : Punto_interesRepo.punto_intereses_ruta(context, id)) {
                Punto_interesRepo.deletePunto_interesWithId(context, punto.getId());
            }
            for (Coordenada coordenada : CoordenadaRepo.coordenadas_ruta(context, id)) {
                CoordenadaRepo.deleteCoordenadaWithId(context, coordenada.getId());
            }
            RutaRepo.deleteRutaWithId(context, id);
            internet = false;
        }
        else {
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
     * Deleting product
     * */
    protected Void doInBackground(Void... args) {

        if(internet) {
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id_ruta", id.toString()));

                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_eliminar_ruta, "POST", params);

                // check your log for json response
                Log.d("Delete Product", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully created product
                    Log.i("ruta eliminada", "eliminada correctamente");
                } else {
                    // failed to create product
                    Log.i("ruta eliminada", "algo fallo");
                }
                if (RutaRepo.isValid(context, id.longValue()) == 1) {
                    //Eliminar primero los puntos, obstaculos, coordenadas y luego la ruta
                    for (Punto_interes punto : Punto_interesRepo.punto_intereses_ruta(context, id)) {
                        Punto_interesRepo.deletePunto_interesWithId(context, punto.getId());
                    }
                    for (Coordenada coordenada : CoordenadaRepo.coordenadas_ruta(context, id)) {
                        CoordenadaRepo.deleteCoordenadaWithId(context, coordenada.getId());
                    }

                    RutaRepo.deleteRutaWithId(context, id);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected void onPostExecute(Void file_url) {

    }

}