package repositorios;

import android.content.Context;

import com.mapas.franciscojavier.trekkingroute.MenuPrincipal;

import java.util.List;

import greendao.Coordenada;
import greendao.CoordenadaDao;

/**
 * Created by juancarlosgonzalezca on 27-04-2015.
 */

public class CoordenadaRepo {
    public static void insertOrUpdate(Context context, Coordenada coordenada) {
        getCoordenadaDao(context).insertOrReplace(coordenada);
    }

    public static void clearCoordenadas(Context context) {
        getCoordenadaDao(context).deleteAll();
    }

    public static void deleteCoordenadaWithId(Context context, long id) {
        getCoordenadaDao(context).delete(getCoordenadaForId(context, id));
    }

    public static Coordenada getCoordenadaForId(Context context, long id) {
        return getCoordenadaDao(context).load(id);
    }

    public static List<Coordenada> getAllCoordenadas(Context context) {
        return getCoordenadaDao(context).loadAll();
    }

    private static CoordenadaDao getCoordenadaDao(Context c) {
        return ((MenuPrincipal) c).getDaoSession().getCoordenadaDao();
    }

    public static List<Coordenada> coordenadas_ruta(int id, Context context) {
        return getCoordenadaDao(context).queryBuilder().where(CoordenadaDao.Properties.Id_ruta.eq(id)).list();
    }
}
