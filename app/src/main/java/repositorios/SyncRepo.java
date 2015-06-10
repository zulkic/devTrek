package repositorios;

import android.content.Context;

import com.mapas.franciscojavier.trekkingroute.MenuPrincipal;

import greendao.Sync;
import greendao.SyncDao;

/**
 * Created by juancarlosgonzalezca on 09-06-2015.
 */
public class SyncRepo {
    public static void insertOrUpdate(Context context, Sync sync) {
        getSyncDao(context).insertOrReplace(sync);
    }

    public static void deleteUsuarioWithId(Context context, long id) {
        getSyncDao(context).delete(getSyncForId(context, id));
    }

    public static Sync getSyncForId(Context context, long id) {
        return getSyncDao(context).load(id);
    }

    private static SyncDao getSyncDao(Context c) {
        return ((MenuPrincipal) c).getDaoSession().getSyncDao();
    }
}
