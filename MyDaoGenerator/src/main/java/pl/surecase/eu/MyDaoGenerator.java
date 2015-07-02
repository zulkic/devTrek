package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(3, "greendao");

        Entity ruta = schema.addEntity("Ruta");
        ruta.addIdProperty();
        ruta.addStringProperty("nombre");
        ruta.addStringProperty("descripcion");
        ruta.addFloatProperty("kms");
        ruta.addStringProperty("tiempo_estimado");
        ruta.addBooleanProperty("oficial");
        ruta.addIntProperty("id_region");
        ruta.addStringProperty("tipo");
        ruta.addBooleanProperty("sincronizada");
        ruta.addBooleanProperty("favorita");

        Entity tipo_ruta = schema.addEntity("Tipo_Ruta");
        tipo_ruta.addIdProperty();
        tipo_ruta.addStringProperty("nombre");

        Entity tipos_de_una_ruta = schema.addEntity("Tipos_de_una_Ruta");
        tipos_de_una_ruta.addIntProperty("id_ruta");
        tipos_de_una_ruta.addIntProperty("id_tipo_ruta");

        Entity coordenada = schema.addEntity("Coordenada");
        coordenada.addIdProperty();
        coordenada.addDoubleProperty("latitud");
        coordenada.addDoubleProperty("longitud");
        coordenada.addIntProperty("altitud");
        coordenada.addIntProperty("id_ruta");
        coordenada.addIntProperty("posicion");

        Entity tipo_obstaculo = schema.addEntity("Tipo_obstaculo");
        tipo_obstaculo.addIdProperty();
        tipo_obstaculo.addStringProperty("nombre");
        tipo_obstaculo.addStringProperty("nombre_icono");

        Entity obstaculo = schema.addEntity("Obstaculo");
        obstaculo.addIdProperty();
        obstaculo.addStringProperty("descripcion");
        obstaculo.addIntProperty("id_tipo_obstaculo");
        obstaculo.addDoubleProperty("latitud");
        obstaculo.addDoubleProperty("longitud");
        obstaculo.addIntProperty("id_ruta");

        Entity tipo_punto_interes = schema.addEntity("Tipo_punto_interes");
        tipo_punto_interes.addIdProperty();
        tipo_punto_interes.addStringProperty("nombre");
        tipo_punto_interes.addStringProperty("nombre_icono");

        Entity punto_interes = schema.addEntity("Punto_interes");
        punto_interes.addIdProperty();
        punto_interes.addStringProperty("descripcion");
        punto_interes.addIntProperty("id_tipo_punto_interes");
        punto_interes.addDoubleProperty("latitud");
        punto_interes.addDoubleProperty("longitud");
        punto_interes.addIntProperty("id_ruta");

        Entity usuario = schema.addEntity("Usuario");
        usuario.addIdProperty();
        usuario.addStringProperty("email");
        usuario.addStringProperty("nombre");
        usuario.addStringProperty("contrasenia");
        usuario.addStringProperty("rol");

        Entity estadistica = schema.addEntity("Estadistica");
        estadistica.addIdProperty();
        estadistica.addStringProperty("tiempo_recorrido");
        estadistica.addDateProperty("fecha");
        estadistica.addIntProperty("id_usuario");
        estadistica.addIntProperty("id_ruta");

        Entity interseccion = schema.addEntity("Interseccion");
        interseccion.addIntProperty("id_coordenada");
        interseccion.addIntProperty("id_ruta");

        Entity sync = schema.addEntity("Sync");
        sync.addIdProperty();
        sync.addStringProperty("tabla");
        sync.addStringProperty("tiempo");

        Entity region = schema.addEntity("Region");
        region.addIdProperty();
        region.addStringProperty("nombre");
        region.addStringProperty("ordinal");

        String filePath = "../devTrek/app/src/main/java";
        //new File(filePath).mkdirs();
        new DaoGenerator().generateAll(schema, filePath);
    }
}
