package JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import greendao.Coordenada;
import greendao.Punto_interes;
import greendao.Ruta;
import repositorios.CoordenadaRepo;
import repositorios.Punto_interesRepo;
import repositorios.RutaRepo;

/**
 * Created by juancarlosgonzalezca on 09-06-2015.
 */
public class Guardar_Rutas extends AsyncTask<Void,Void,Void> {

    private ArrayList<Ruta> rutas;
    private Context context;

    public Guardar_Rutas(ArrayList<Ruta> rutas, Context context)
    {
        this.rutas = rutas;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... params) {
        Long new_id = this.rutas.get(this.rutas.size()-1).getId();
        new_id+=1l;
        for(Ruta ruta : this.rutas)
        {
            int result = RutaRepo.isValid(context, ruta.getId());
            if(result == -1 )
            {
                Log.i("guardar ruta: ", ruta.getOficial().toString());
                RutaRepo.insertOrUpdate(context,ruta);
            }
            if(result == 0)
            {
                Long id = ruta.getId();
                Ruta ruta_aux = RutaRepo.getRutaForId(context, id);
                List<Coordenada> coordenadas_aux = CoordenadaRepo.coordenadas_ruta(context,id);
                List<Punto_interes> puntos_interes_aux = Punto_interesRepo.punto_intereses_ruta(context,id);
                for(Punto_interes punto_interes : puntos_interes_aux)
                {
                    punto_interes.setId_ruta(new_id.intValue());
                    Punto_interesRepo.insertOrUpdate(context, punto_interes);
                }
                for(Coordenada coordenada : coordenadas_aux)
                {
                    coordenada.setId_ruta(new_id.intValue());
                    CoordenadaRepo.insertOrUpdate(context,coordenada);
                }
                RutaRepo.deleteRutaWithId(context,id);
                ruta_aux.setId(new_id);
                RutaRepo.insertOrUpdate(context,ruta);
            }
        }
        Log.i("Rutas guardadas: ", "rutas guardadas exitosamente");
        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
