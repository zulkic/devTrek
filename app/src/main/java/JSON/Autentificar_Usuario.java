package JSON;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juancarlosgonzalezca on 03-06-2015.
 */
public class Autentificar_Usuario extends AsyncTask<Void, Void, Boolean> {

    private String email;
    private String contrasenia;
    private JSONParser jsonParser;
    private Context context;
    private static String url_autentificar_usuario = "http://trythistrail.16mb.com/autentificar_usuario.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_AUTENT = "autentificado";
    private Boolean internet;

    public Autentificar_Usuario(String email, String contrasenia, Context context)
    {
        this.email = email;
        this.contrasenia = contrasenia;
        this.jsonParser = new JSONParser();
        this.context = context;
        hasInternet conexion = new hasInternet(this.context);
        try {
            internet = conexion.execute().get();
        }
        catch(Exception e)
        {
            internet = false;
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
    protected Boolean doInBackground(Void... args) {

        Boolean autentificado = false;
        if(internet) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", this.email));
            params.add(new BasicNameValuePair("contrasenia", this.contrasenia));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_autentificar_usuario,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
                String aux = json.getString(TAG_AUTENT);
                if(aux.equals("1")) {
                    autentificado = true;
                    SharedPreferences sharedpreferences;
                    SharedPreferences.Editor editor;
                    sharedpreferences = context.getSharedPreferences(Globals.PREF, Context.MODE_PRIVATE);
                    editor = sharedpreferences.edit();
                    editor.putString(Globals.PASS, this.contrasenia);
                    editor.commit();
                }
                if (success != 0) {
                    // successfully created product
                    Log.i("autentificar usuario", "autentificado correctamente");
                } else {
                    // failed to create product
                    Log.i("autentificar usuario", "algo fallo");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return autentificado;
    }

    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

    }

}
