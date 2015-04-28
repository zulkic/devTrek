package repositorios;

/**
 * Created by juancarlosgonzalezca on 27-04-2015.
 */

import android.content.Context;
import java.util.List;
import greendao.Usuario;
import greendao.UsuarioDao;
import com.mapas.franciscojavier.trekkingroute.MainActivity;

public class UsuarioRepo {
    public static void insertOrUpdate(Context context, Usuario usuario) {
        getUsuarioDao(context).insertOrReplace(usuario);
    }

    public static void clearUsuarios(Context context) {
        getUsuarioDao(context).deleteAll();
    }

    public static void deleteUsuarioWithId(Context context, long id) {
        getUsuarioDao(context).delete(getUsuarioForId(context, id));
    }

    public static Usuario getUsuarioForId(Context context, long id) {
        return getUsuarioDao(context).load(id);
    }

    public static List<Usuario> getAllUsuarios(Context context) {
        return getUsuarioDao(context).loadAll();
    }

    private static UsuarioDao getUsuarioDao(Context c) {
        return ((MainActivity) c).getDaoSession().getUsuarioDao();
    }
}