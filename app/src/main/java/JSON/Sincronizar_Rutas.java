package JSON;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mapas.franciscojavier.trekkingroute.R;
import com.mapas.franciscojavier.trekkingroute.Utility.Wrapper;

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
public class Sincronizar_Rutas {

    private Context context;

    public Sincronizar_Rutas(Context context)
    {
        this.context = context;
        this.doInBackground();
    }

    protected void doInBackground() {

        Boolean internet;
        hasInternet conexion = new hasInternet(this.context);
        try {
            internet = conexion.execute().get();
        }
        catch(Exception e)
        {
            internet = false;
        }

        if(internet) {

            List<Ruta> rutas = RutaRepo.noSincronizadas(context);
            for(Ruta ruta : rutas) {
                try {
                    Nueva_Ruta tarea_agregar_ruta = new Nueva_Ruta(ruta, context);
                    Wrapper wp = tarea_agregar_ruta.execute().get();
                    Integer id = wp.getId();
                    ruta.setId(id.longValue());
                    ruta.setSincronizada(true);
                    RutaRepo.insertOrUpdate(context,ruta);
                    ArrayList<Coordenada> lista_coordenadas = (ArrayList<Coordenada>) CoordenadaRepo.coordenadas_ruta(context, id.longValue());

                    Post_Coordenadas_Ruta tarea_agregar_coordenadas = new Post_Coordenadas_Ruta(lista_coordenadas, context, wp);
                    tarea_agregar_coordenadas.execute();

                    ArrayList<Punto_interes> lista_puntos_interes = (ArrayList<Punto_interes>) Punto_interesRepo.punto_intereses_ruta(context, id.longValue());

                    Post_Puntos_Interes_Ruta tarea_agregar_puntos = new Post_Puntos_Interes_Ruta(lista_puntos_interes, context, wp);
                    tarea_agregar_puntos.execute();
                } catch (Exception e) {
                    Log.i("Error al sincronizar: ", "no se pudo sincronizar");
                }
            }
            Toast.makeText(context, R.string.sincronizadas, Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
        }
    }
}

