package repositorios;

/**
 * Created by juancarlosgonzalezca on 27-04-2015.
 */
import android.content.Context;

import com.mapas.franciscojavier.trekkingroute.MenuPrincipal;

import java.util.List;

import greendao.Interseccion;
import greendao.InterseccionDao;

public class InterseccionRepo {
    public static void insertOrUpdate(Context context, Interseccion interseccion) {
        getInterseccionDao(context).insertOrReplace(interseccion);
    }

    public static void clearIntersecciones(Context context) {
        getInterseccionDao(context).deleteAll();
    }

    /*public static void deleteInterseccionWithId(Context context, long id) {
        getInterseccionDao(context).delete(getInterseccionForId(context, id));
    }*/

    /*public static Interseccion getInterseccionForId(Context context, long id) {
        return getInterseccionDao(context).load(id);
    }*/

    public static List<Interseccion> getAllIntersecciones(Context context) {
        return getInterseccionDao(context).loadAll();
    }

    private static InterseccionDao getInterseccionDao(Context c) {
        return ((MenuPrincipal) c).getDaoSession().getInterseccionDao();
    }
}