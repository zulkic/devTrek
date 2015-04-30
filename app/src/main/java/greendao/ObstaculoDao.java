package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Obstaculo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table OBSTACULO.
*/
public class ObstaculoDao extends AbstractDao<Obstaculo, Long> {

    public static final String TABLENAME = "OBSTACULO";

    /**
     * Properties of entity Obstaculo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Descripcion = new Property(1, String.class, "descripcion", false, "DESCRIPCION");
        public final static Property Id_tipo_obstaculo = new Property(2, Integer.class, "id_tipo_obstaculo", false, "ID_TIPO_OBSTACULO");
        public final static Property Id_coordenada = new Property(3, Integer.class, "id_coordenada", false, "ID_COORDENADA");
    };


    public ObstaculoDao(DaoConfig config) {
        super(config);
    }
    
    public ObstaculoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'OBSTACULO' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'DESCRIPCION' TEXT," + // 1: descripcion
                "'ID_TIPO_OBSTACULO' INTEGER," + // 2: id_tipo_obstaculo
                "'ID_COORDENADA' INTEGER);"); // 3: id_coordenada
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'OBSTACULO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Obstaculo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String descripcion = entity.getDescripcion();
        if (descripcion != null) {
            stmt.bindString(2, descripcion);
        }
 
        Integer id_tipo_obstaculo = entity.getId_tipo_obstaculo();
        if (id_tipo_obstaculo != null) {
            stmt.bindLong(3, id_tipo_obstaculo);
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
    public Obstaculo readEntity(Cursor cursor, int offset) {
        Obstaculo entity = new Obstaculo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // descripcion
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // id_tipo_obstaculo
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3) // id_coordenada
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Obstaculo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDescripcion(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setId_tipo_obstaculo(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setId_coordenada(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Obstaculo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Obstaculo entity) {
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
