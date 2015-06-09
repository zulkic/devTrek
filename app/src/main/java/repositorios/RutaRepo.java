package repositorios;

import android.content.Context;
import android.util.Log;

import com.mapas.franciscojavier.trekkingroute.MenuPrincipal;

import java.util.List;

import greendao.Ruta;
import greendao.RutaDao;
/**
 * Created by juancarlosgonzalezca on 24-04-2015.
 */

public class RutaRepo {
    public static int insertOrUpdate(Context context, Ruta ruta) {
        getRutaDao(context).insertOrReplace(ruta);
        return ruta.getId().intValue();
    }

    public static void clearRutas(Context context) {
        getRutaDao(context).deleteAll();
    }

    public static void deleteRutaWithId(Context context, long id) {
        getRutaDao(context).delete(getRutaForId(context, id));
    }

    public static Ruta getRutaForId(Context context, long id) {
        return getRutaDao(context).load(id);
    }

    public static List<Ruta> getAllRutas(Context context) {
        return getRutaDao(context).loadAll();
    }

    private static RutaDao getRutaDao(Context c) {
        return ((MenuPrincipal) c).getDaoSession().getRutaDao();
    }

    public static boolean isValid(Context context, Long id) {
        Ruta ruta = getRutaForId(context, id);
        if(ruta != null)
        {
            if(ruta.getSincronizada())
            {
                Log.i("valid: ", "is valid");
                return true;
            }
            Log.i("valid: ", "is not valid");
            return false;
        }
        Log.i("valid: ", "is null");
        return false;
    }
}





