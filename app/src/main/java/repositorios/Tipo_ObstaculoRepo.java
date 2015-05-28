package repositorios;

import android.content.Context;

import com.mapas.franciscojavier.trekkingroute.MenuPrincipal;

import java.util.List;

import greendao.Tipo_obstaculo;
import greendao.Tipo_obstaculoDao;

/**
 * Created by juancarlosgonzalezca on 27-05-2015.
 */
public class Tipo_ObstaculoRepo {
    public static void insertOrUpdate(Context context, Tipo_obstaculo tipo_obstaculo) {
        getTipo_ObstaculoDao(context).insertOrReplace(tipo_obstaculo);
    }

    public static void clearTipos_Obstaculos(Context context) {
        getTipo_ObstaculoDao(context).deleteAll();
    }

    public static void deleteTipo_ObstaculoWithId(Context context, long id) {
        getTipo_ObstaculoDao(context).delete(getTipo_ObstaculoForId(context, id));
    }

    public static Tipo_obstaculo getTipo_ObstaculoForId(Context context, long id) {
        return getTipo_ObstaculoDao(context).load(id);
    }

    public static List<Tipo_obstaculo> getAllTipos_Obstaculos(Context context) {
        return getTipo_ObstaculoDao(context).loadAll();
    }

    private static Tipo_obstaculoDao getTipo_ObstaculoDao(Context c) {
        return ((MenuPrincipal) c).getDaoSession().getTipo_obstaculoDao();
    }
}
