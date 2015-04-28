package repositorios;

/**
 * Created by juancarlosgonzalezca on 27-04-2015.
 */

import android.content.Context;
import java.util.List;
import greendao.Estadistica;
import greendao.EstadisticaDao;
import com.mapas.franciscojavier.trekkingroute.MainActivity;

public class EstadisticaRepo {
    public static void insertOrUpdate(Context context, Estadistica estadistica) {
        getEstadisticaDao(context).insertOrReplace(estadistica);
    }

    public static void clearEstadisticas(Context context) {
        getEstadisticaDao(context).deleteAll();
    }

    public static void deleteEstadisticaWithId(Context context, long id) {
        getEstadisticaDao(context).delete(getEstadisticaForId(context, id));
    }

    public static Estadistica getEstadisticaForId(Context context, long id) {
        return getEstadisticaDao(context).load(id);
    }

    public static List<Estadistica> getAllEstadisticas(Context context) {
        return getEstadisticaDao(context).loadAll();
    }

    private static EstadisticaDao getEstadisticaDao(Context c) {
        return ((MainActivity) c).getDaoSession().getEstadisticaDao();
    }
}