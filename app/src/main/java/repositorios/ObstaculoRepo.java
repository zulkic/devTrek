package repositorios;

/**
 * Created by juancarlosgonzalezca on 27-04-2015.
 */

import android.content.Context;

import com.mapas.franciscojavier.trekkingroute.MenuPrincipal;

import java.util.List;

import greendao.Obstaculo;
import greendao.ObstaculoDao;

public class ObstaculoRepo {
    public static void insertOrUpdate(Context context, Obstaculo obstaculo) {
        getObstaculoDao(context).insertOrReplace(obstaculo);
    }

    public static void clearObstaculos(Context context) {
        getObstaculoDao(context).deleteAll();
    }

    public static void deleteObstaculoWithId(Context context, long id) {
        getObstaculoDao(context).delete(getObstaculoForId(context, id));
    }

    public static Obstaculo getObstaculoForId(Context context, long id) {
        return getObstaculoDao(context).load(id);
    }

    public static List<Obstaculo> getAllObstaculos(Context context) {
        return getObstaculoDao(context).loadAll();
    }

    private static ObstaculoDao getObstaculoDao(Context c) {
        return ((MenuPrincipal) c).getDaoSession().getObstaculoDao();
    }
}
