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

import greendao.Usuario;

/**
 * Created by juancarlosgonzalezca on 03-06-2015.
 */
public class Cambiar_Contrasenia extends AsyncTask<Void, Void, Void> {

    private Usuario usuario;
    private Context context;
    private JSONParser jsonParser;
    private static String url_cambiar_contrasenia = "http://trythistrail.16mb.com/cambiar_contrasenia.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    public Cambiar_Contrasenia(Usuario usuario, Context context)
    {
        this.usuario = usuario;
        this.context = context;
        this.jsonParser = new JSONParser();
    }

    /**
     * Saving product
     * */
    protected Void doInBackground(Void... args) {

        hasInternet conexion = new hasInternet(this.context);
        Boolean internet = conexion.getInternet();

        if(internet) {
        }
        // getting updated data from EditTexts
        String email = this.usuario.getEmail();
        String contrasenia = this.usuario.getContrasenia();

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("contrasenia", contrasenia));

        // sending modified data through http request
        // Notice that update product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(url_cambiar_contrasenia,
                "POST", params);

        // check json success tag
        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // successfully created product
                Log.i("usuario modificado", "modificado correctamente");
            } else {
                // failed to create product
                Log.i("usuario modificado", "algo fallo");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(Void file_url) {
    }
}
