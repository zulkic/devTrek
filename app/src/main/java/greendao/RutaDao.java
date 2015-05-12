package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table RUTA.
*/
public class RutaDao extends AbstractDao<Ruta, Long> {

    public static final String TABLENAME = "RUTA";

    /**
     * Properties of entity Ruta.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Descripcion = new Property(2, String.class, "descripcion", false, "DESCRIPCION");
        public final static Property Kms = new Property(3, Float.class, "kms", false, "KMS");
        public final static Property Tiempo_estimado = new Property(4, String.class, "tiempo_estimado", false, "TIEMPO_ESTIMADO");
        public final static Property Oficial = new Property(5, Boolean.class, "oficial", false, "OFICIAL");
    };


    public RutaDao(DaoConfig config) {
        super(config);
    }
    
    public RutaDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'RUTA' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'DESCRIPCION' TEXT," + // 2: descripcion
                "'KMS' REAL," + // 3: kms
                "'TIEMPO_ESTIMADO' TEXT," + // 4: tiempo_estimado
                "'OFICIAL' INTEGER);"); // 5: oficial
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'RUTA'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Ruta entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getNombre();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String descripcion = entity.getDescripcion();
        if (descripcion != null) {
            stmt.bindString(3, descripcion);
        }
 
        Float kms = entity.getKms();
        if (kms != null) {
            stmt.bindDouble(4, kms);
        }
 
        String tiempo_estimado = entity.getTiempo_estimado();
        if (tiempo_estimado != null) {
            stmt.bindString(5, tiempo_estimado);
        }
 
        Boolean oficial = entity.getOficial();
        if (oficial != null) {
            stmt.bindLong(6, oficial ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Ruta readEntity(Cursor cursor, int offset) {
        Ruta entity = new Ruta( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // descripcion
            cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3), // kms
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // tiempo_estimado
            cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0 // oficial
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Ruta entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNombre(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDescripcion(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setKms(cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3));
        entity.setTiempo_estimado(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setOficial(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Ruta entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Ruta entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
