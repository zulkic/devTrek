package repositorios;

/**
 * Created by juancarlosgonzalezca on 27-04-2015.
 */
import android.content.Context;

import com.mapas.franciscojavier.trekkingroute.MenuPrincipal;

import java.util.List;

import greendao.Punto_interes;
import greendao.Punto_interesDao;

public class Punto_interesRepo {
    public static void insertOrUpdate(Context context, Punto_interes punto_interes) {
        getPunto_interesDao(context).insertOrReplace(punto_interes);
    }

    public static void clearPuntos_interes(Context context) {
        getPunto_interesDao(context).deleteAll();
    }

    public static void deletePunto_interesWithId(Context context, long id) {
        getPunto_interesDao(context).delete(getPunto_interesForId(context, id));
    }

    public static Punto_interes getPunto_interesForId(Context context, long id) {
        return getPunto_interesDao(context).load(id);
    }

    public static List<Punto_interes> getAllPuntos_interes(Context context) {
        return getPunto_interesDao(context).loadAll();
    }

    private static Punto_interesDao getPunto_interesDao(Context c) {
        return ((MenuPrincipal) c).getDaoSession().getPunto_interesDao();
    }

    public static List<Punto_interes> punto_intereses_ruta(Context context, Long id) {
        return getPunto_interesDao(context).queryBuilder().where(Punto_interesDao.Properties.Id_ruta.eq(id.intValue())).list();
    }
}
