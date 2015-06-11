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

/**
 * Created by juancarlosgonzalezca on 03-06-2015.
 */
public class Verificar_Usuario extends AsyncTask<Void, Void, Boolean> {

    private String email;
    private JSONParser jsonParser;
    private Context context;
    private static String url_registrar_usuario = "http://trythistrail.16mb.com/validar_usuario.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_AUTENT = "existe";

    public Verificar_Usuario(String email, Context context)
    {
        this.email = email;
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
    protected Boolean doInBackground(Void... args) {

        hasInternet conexion = new hasInternet(this.context);
        Boolean internet = conexion.getInternet();
        Boolean existe = false;
        if(internet) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", this.email));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_registrar_usuario,
                    "GET", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
                String aux = json.getString(TAG_AUTENT);
                if(aux.equals("1"))
                    existe=true;
                if (success != 0) {
                    // successfully created product
                    Log.i("verificar usuario", "creada correctamente");
                } else {
                    // failed to create product
                    Log.i("verificar usuario", "algo fallo");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return existe;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

    }

}
