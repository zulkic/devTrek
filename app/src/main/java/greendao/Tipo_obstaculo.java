package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table TIPO_OBSTACULO.
 */
public class Tipo_obstaculo extends Tipo_Indicador {

    private Long id;
    private String nombre;
    private String nombre_icono;

    public Tipo_obstaculo() {
    }

    public Tipo_obstaculo(Long id) {
        this.id = id;
    }

    public Tipo_obstaculo(Long id, String nombre, String nombre_icono) {
        this.id = id;
        this.nombre = nombre;
        this.nombre_icono = nombre_icono;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre_icono() {
        return nombre_icono;
    }

    public void setNombre_icono(String nombre_icono) {
        this.nombre_icono = nombre_icono;
    }

}
