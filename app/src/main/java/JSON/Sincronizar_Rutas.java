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
 * Created by juancarlosgonzalezca on 10-06-2015.
 */
public class Sincronizar_Rutas extends AsyncTask<Void, Void, Boolean> {

    private Context context;

    public Sincronizar_Rutas(Context context)
    {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        hasInternet conexion = new hasInternet(this.context);
        Boolean internet = conexion.getInternet();

        if(internet) {

            List<Ruta> rutas = RutaRepo.noSincronizadas(context);
            for(Ruta ruta : rutas) {
                Nueva_Ruta tarea_agregar_ruta = new Nueva_Ruta(ruta, context);
                try {
                    Integer id = tarea_agregar_ruta.execute().get();
                    ArrayList<Coordenada> lista_coordenadas = (ArrayList<Coordenada>) CoordenadaRepo.coordenadas_ruta(context, id.longValue());
                    for (Coordenada coordenada : lista_coordenadas) {
                        coordenada.setId_ruta(id);
                    }
                    Post_Coordenadas_Ruta tarea_agregar_coordenadas = new Post_Coordenadas_Ruta(lista_coordenadas, context);
                    tarea_agregar_coordenadas.execute();

                    ArrayList<Punto_interes> lista_puntos_interes = (ArrayList<Punto_interes>) Punto_interesRepo.punto_intereses_ruta(context, id.longValue());
                    for (Punto_interes punto_interes : lista_puntos_interes) {
                        punto_interes.setId_ruta(id);
                    }

                    Post_Puntos_Interes_Ruta tarea_agregar_puntos = new Post_Puntos_Interes_Ruta(lista_puntos_interes, context);
                    tarea_agregar_puntos.execute();
                } catch (Exception e) {
                    Log.i("Error al sincronizar: ", "no se pudo sincronizar");
                }
            }
            return true;
        }
        else
        {
            Log.i("no hay internet: ", "no se puede sincronizar sin internet");
            return false;
        }
    }
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }
}

