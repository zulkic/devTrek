package JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juancarlosgonzalezca on 09-06-2015.
 */
public class Tiempo_Sync extends AsyncTask<Void, Void, String> {

    private Context context;
    private JSONParser jsonParser;
    private static String url_tiempo_sync = "http://trythistrail.16mb.com/tiempo_sync.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SYNC = "sync";
    private static final String TAG_TIEMPO = "tiempo";

    /**
     * Before starting background thread Show Progress Dialog
     */

    public Tiempo_Sync(Context context) {
        this.context = context;
        this.jsonParser = new JSONParser();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * getting All products from url
     */
    protected String doInBackground(Void... args) {
        hasInternet conexion = new hasInternet(this.context);
        Boolean internet = conexion.getInternet();
        String tiempo = "";

        if(internet) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_tiempo_sync, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("Tiempo sync: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    tiempo = json.getString(TAG_TIEMPO);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            tiempo="error";
        }
        return tiempo;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * *
     */

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
