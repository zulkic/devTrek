package repositorios;

import android.content.Context;

import com.mapas.franciscojavier.trekkingroute.MenuPrincipal;

import java.util.List;

import greendao.Region;
import greendao.RegionDao;

/**
 * Created by juancarlosgonzalezca on 01-07-2015.
 */
public class RegionRepo {
    public static void insertOrUpdate(Context context, Region region) {
        getRegionDao(context).insertOrReplace(region);
    }

    public static void clearRegiones(Context context) {
        getRegionDao(context).deleteAll();
    }

    public static void deleteRegionWithId(Context context, long id) {
        getRegionDao(context).delete(getRegionForId(context, id));
    }

    public static Region getRegionForId(Context context, long id) {
        return getRegionDao(context).load(id);
    }

    public static List<Region> getAllRegiones(Context context) {
        return getRegionDao(context).loadAll();
    }

    private static RegionDao getRegionDao(Context c) {
        return ((MenuPrincipal) c).getDaoSession().getRegionDao();
    }
}
