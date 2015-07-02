package JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import greendao.Obstaculo;
import repositorios.ObstaculoRepo;

/**
 * Created by juancarlosgonzalezca on 10-06-2015.
 */
public class Guardar_Obstaculos extends AsyncTask<Void,Void,Void> {

    private ArrayList<Obstaculo> obstaculos;
    private Context context;
    private Long id_ruta;

    public Guardar_Obstaculos(ArrayList<Obstaculo> obstaculos, Context context, Long id_ruta)
    {
        this.obstaculos = obstaculos;
        this.context = context;
        this.id_ruta = id_ruta;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... params) {
        for(Obstaculo obstaculo : this.obstaculos)
        {
            obstaculo.setId_ruta(id_ruta.intValue());
            ObstaculoRepo.insertOrUpdate(context, obstaculo);

        }
        Log.i("Obstaculos guardados: ", "obstaculos guardados exitosamente");
        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
