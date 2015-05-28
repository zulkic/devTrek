package repositorios;

import android.content.Context;

import com.mapas.franciscojavier.trekkingroute.MenuPrincipal;

import java.util.List;

import greendao.Tipo_punto_interes;
import greendao.Tipo_punto_interesDao;

/**
 * Created by juancarlosgonzalezca on 27-05-2015.
 */
public class Tipo_Puntos_InteresRepo {
    public static void insertOrUpdate(Context context, Tipo_punto_interes tipo_punto_interes) {
        getTipo_Punto_InteresDao(context).insertOrReplace(tipo_punto_interes);
    }

    public static void clearTipos_Puntos_Interes(Context context) {
        getTipo_Punto_InteresDao(context).deleteAll();
    }

    public static void deleteTipo_Punto_InteresWithId(Context context, long id) {
        getTipo_Punto_InteresDao(context).delete(getTipo_Punto_InteresForId(context, id));
    }

    public static Tipo_punto_interes getTipo_Punto_InteresForId(Context context, long id) {
        return getTipo_Punto_InteresDao(context).load(id);
    }

    public static List<Tipo_punto_interes> getAllTipos_Puntos_Interes(Context context) {
        return getTipo_Punto_InteresDao(context).loadAll();
    }

    private static Tipo_punto_interesDao getTipo_Punto_InteresDao(Context c) {
        return ((MenuPrincipal) c).getDaoSession().getTipo_punto_interesDao();
    }
}