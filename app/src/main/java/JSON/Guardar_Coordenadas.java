package JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import greendao.Coordenada;
import repositorios.CoordenadaRepo;

/**
 * Created by juancarlosgonzalezca on 10-06-2015.
 */
public class Guardar_Coordenadas extends AsyncTask<Void,Void,Void> {

    private ArrayList<Coordenada> coordenadas;
    private Context context;
    private Long id_ruta;

    public Guardar_Coordenadas(ArrayList<Coordenada> coordenadas, Context context, Long id_ruta)
    {
        this.coordenadas = coordenadas;
        this.context = context;
        this.id_ruta = id_ruta;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... params) {
        for(Coordenada coordenada : this.coordenadas)
        {
            coordenada.setId_ruta(id_ruta.intValue());
            CoordenadaRepo.insertOrUpdate(context,coordenada);

        }
        Log.i("Coordenadas guardadas: ", "coordenadas guardadas exitosamente");
        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
