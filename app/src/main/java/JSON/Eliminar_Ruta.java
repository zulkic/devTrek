package JSON;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class Eliminar_Ruta extends AsyncTask<Void, Void, Void> {

    private int id;
    private JSONParser jsonParser;
    private static String url_eliminar_ruta = "http://trythistrail.16mb.com/eliminar_ruta.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    public Eliminar_Ruta(int id)
    {
        this.id = id;
        this.jsonParser = new JSONParser();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Deleting product
     * */
    protected Void doInBackground(Void... args) {

        // Check for success tag
        int success;
        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_ruta", Integer.toString(id)));

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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {

    }

}