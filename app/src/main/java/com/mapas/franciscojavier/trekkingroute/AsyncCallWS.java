package com.mapas.franciscojavier.trekkingroute;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by juancarlosgonzalezca on 29-04-2015.
 */
class AsyncCallWS extends AsyncTask<String, Void, Void> {

    private String text;
    @Override
    protected Void doInBackground(String... params) {
        //Invoke webservice
        Log.i("algo sucede", "que sera");
        text = WebService.invokeHelloWorldWS("hello","hello");
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //Set response
        Log.i("algo sucede rest", "que sera :(");
        Log.i("web service test:", text);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

}