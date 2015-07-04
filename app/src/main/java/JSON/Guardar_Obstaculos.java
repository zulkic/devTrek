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
            Obstaculo nuevo = new Obstaculo();
            nuevo.setLatitud(obstaculo.getLatitud());
            nuevo.setLongitud(obstaculo.getLongitud());
            nuevo.setId_tipo_obstaculo(obstaculo.getId_tipo_obstaculo());
            nuevo.setId_ruta(id_ruta.intValue());
            nuevo.setDescripcion(obstaculo.getDescripcion());
            ObstaculoRepo.insertOrUpdate(context, nuevo);

        }
        Log.i("Obstaculos guardados: ", "obstaculos guardados exitosamente");
        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
