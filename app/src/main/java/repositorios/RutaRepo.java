package repositorios;

import android.content.Context;
import java.util.List;
import greendao.Ruta;
import greendao.RutaDao;
import com.mapas.franciscojavier.trekkingroute.MainActivity;
/**
 * Created by juancarlosgonzalezca on 24-04-2015.
 */

public class RutaRepo {
    public static void insertOrUpdate(Context context, Ruta ruta) {
        getRutaDao(context).insertOrReplace(ruta);
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
        return ((MainActivity) c).getDaoSession().getRutaDao();
    }
}





