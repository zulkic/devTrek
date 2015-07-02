package JSON;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.mapas.franciscojavier.trekkingroute.Account.MainCalls;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import greendao.Usuario;

/**
 * Created by juancarlosgonzalezca on 13-06-2015.
 */
public class Buscar_Usuario extends AsyncTask<Void, Void, Void> {

    private String email;
    private Usuario usuario;
    private Context context;
    private JSONParser jsonParser;
    private static String url_buscar_usuario = "http://trythistrail.16mb.com/buscar_usuario.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USUARIO = "usuario";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_ROL = "rol";
    private MainCalls mListener;
    private Boolean internet;
    /**
     * Before starting background thread Show Progress Dialog
     */

    public Buscar_Usuario(String email, Context context, MainCalls mListener) {
        this.email = email;
        this.usuario = new Usuario();
        this.context = context;
        this.jsonParser = new JSONParser();
        this.mListener = mListener;
        hasInternet conexion = new hasInternet(this.context);
        try {
            internet = conexion.execute().get();
        }
        catch(Exception e)
        {
            internet = false;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * getting All products from url
     */
    protected Void doInBackground(Void... args) {
        if(internet) {
            // Building Parameters
            Log.i("Email: ", "("+this.email+")");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", this.email));
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_buscar_usuario, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("Buscar Usuario: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    JSONArray result = json.getJSONArray(TAG_USUARIO);
                    JSONObject c = result.getJSONObject(0);
                    // Storing each json item in variable
                    this.usuario.setEmail(c.getString(TAG_EMAIL));
                    this.usuario.setNombre(c.getString(TAG_NOMBRE));
                    this.usuario.setRol(c.getString(TAG_ROL));

                    SharedPreferences sharedpreferences;
                    SharedPreferences.Editor editor;
                    sharedpreferences = context.getSharedPreferences(Globals.PREF, Context.MODE_PRIVATE);
                    editor = sharedpreferences.edit();
                    editor.putString(Globals.NOMBRE, usuario.getNombre());
                    editor.putString(Globals.EMAIL, usuario.getEmail());
                    editor.putString(Globals.ROL, usuario.getRol());
                    editor.commit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mListener.goToHome();
    }
}
