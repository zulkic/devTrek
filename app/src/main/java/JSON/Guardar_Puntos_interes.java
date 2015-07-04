package JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import greendao.Punto_interes;
import repositorios.Punto_interesRepo;

/**
 * Created by juancarlosgonzalezca on 10-06-2015.
 */
public class Guardar_Puntos_interes extends AsyncTask<Void,Void,Void> {

    private ArrayList<Punto_interes> punto_intereses;
    private Context context;
    private Long id_ruta;

    public Guardar_Puntos_interes(ArrayList<Punto_interes> punto_intereses, Context context, Long id_ruta)
    {
        this.punto_intereses = punto_intereses;
        this.context = context;
        this.id_ruta = id_ruta;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... params) {
        for(Punto_interes punto_interes : this.punto_intereses)
        {
            Punto_interes nuevo = new Punto_interes();
            nuevo.setLatitud(punto_interes.getLatitud());
            nuevo.setLongitud(punto_interes.getLatitud());
            nuevo.setId_tipo_punto_interes(punto_interes.getId_tipo_punto_interes());
            nuevo.setId_ruta(id_ruta.intValue());
            nuevo.setDescripcion(punto_interes.getDescripcion());
            Punto_interesRepo.insertOrUpdate(context, punto_interes);
        }
        Log.i("Puntos guardados: ", "puntos de interes guardados exitosamente");
        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
