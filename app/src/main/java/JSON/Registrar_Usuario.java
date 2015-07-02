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
import repositorios.UsuarioRepo;

/**
 * Created by juancarlosgonzalezca on 03-06-2015.
 */
public class Registrar_Usuario extends AsyncTask<Void, Void, Void> {

    private Usuario usuario;
    private JSONParser jsonParser;
    private Context context;
    private static String url_registrar_usuario = "http://trythistrail.16mb.com/registrar_usuario.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private Boolean internet;

    public Registrar_Usuario(Usuario usuario, Context context)
    {
        this.usuario = usuario;
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
    protected Void doInBackground(Void... args) {

        if(internet) {
            String nombre = this.usuario.getNombre();
            String email = this.usuario.getEmail();
            String contrasenia = this.usuario.getContrasenia();
            String rol = this.usuario.getRol();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("nombre", nombre));
            params.add(new BasicNameValuePair("contrasenia", contrasenia));
            params.add(new BasicNameValuePair("rol", rol));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_registrar_usuario,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success != 0) {
                    // successfully created product
                    Log.i("nuevo usuario", "creada correctamente");
                } else {
                    // failed to create product
                    Log.i("nuevo usuario", "algo fallo");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            UsuarioRepo.insertOrUpdate(this.context, usuario);
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