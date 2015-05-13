package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Punto_interes;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PUNTO_INTERES.
*/
public class Punto_interesDao extends AbstractDao<Punto_interes, Long> {

    public static final String TABLENAME = "PUNTO_INTERES";

    /**
     * Properties of entity Punto_interes.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Descripcion = new Property(1, String.class, "descripcion", false, "DESCRIPCION");
        public final static Property Id_tipo_punto_interes = new Property(2, Integer.class, "id_tipo_punto_interes", false, "ID_TIPO_PUNTO_INTERES");
        public final static Property Id_coordenada = new Property(3, Integer.class, "id_coordenada", false, "ID_COORDENADA");
    };


    public Punto_interesDao(DaoConfig config) {
        super(config);
    }
    
    public Punto_interesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PUNTO_INTERES' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'DESCRIPCION' TEXT," + // 1: descripcion
                "'ID_TIPO_PUNTO_INTERES' INTEGER," + // 2: id_tipo_punto_interes
                "'ID_COORDENADA' INTEGER);"); // 3: id_coordenada
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PUNTO_INTERES'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Punto_interes entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String descripcion = entity.getDescripcion();
        if (descripcion != null) {
            stmt.bindString(2, descripcion);
        }
 
        Integer id_tipo_punto_interes = entity.getId_tipo_punto_interes();
        if (id_tipo_punto_interes != null) {
            stmt.bindLong(3, id_tipo_punto_interes);
        }
 
        Integer id_coordenada = entity.getId_coordenada();
        if (id_coordenada != null) {
            stmt.bindLong(4, id_coordenada);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Punto_interes readEntity(Cursor cursor, int offset) {
        Punto_interes entity = new Punto_interes( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // descripcion
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // id_tipo_punto_interes
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3) // id_coordenada
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Punto_interes entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDescripcion(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setId_tipo_punto_interes(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setId_coordenada(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Punto_interes entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Punto_interes entity) {
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