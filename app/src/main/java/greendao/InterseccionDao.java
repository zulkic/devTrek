package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Interseccion;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table INTERSECCION.
*/
public class InterseccionDao extends AbstractDao<Interseccion, Void> {

    public static final String TABLENAME = "INTERSECCION";

    /**
     * Properties of entity Interseccion.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id_coordenada = new Property(0, Integer.class, "id_coordenada", false, "ID_COORDENADA");
        public final static Property Id_ruta = new Property(1, Integer.class, "id_ruta", false, "ID_RUTA");
    };


    public InterseccionDao(DaoConfig config) {
        super(config);
    }
    
    public InterseccionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'INTERSECCION' (" + //
                "'ID_COORDENADA' INTEGER," + // 0: id_coordenada
                "'ID_RUTA' INTEGER);"); // 1: id_ruta
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'INTERSECCION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Interseccion entity) {
        stmt.clearBindings();
 
        Integer id_coordenada = entity.getId_coordenada();
        if (id_coordenada != null) {
            stmt.bindLong(1, id_coordenada);
        }
 
        Integer id_ruta = entity.getId_ruta();
        if (id_ruta != null) {
            stmt.bindLong(2, id_ruta);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public Interseccion readEntity(Cursor cursor, int offset) {
        Interseccion entity = new Interseccion( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // id_coordenada
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1) // id_ruta
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Interseccion entity, int offset) {
        entity.setId_coordenada(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setId_ruta(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(Interseccion entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(Interseccion entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}