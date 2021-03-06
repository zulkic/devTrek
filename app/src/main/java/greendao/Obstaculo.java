package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table OBSTACULO.
 */
public class Obstaculo extends Indicador {

    private Long id;
    private String descripcion;
    private Integer id_tipo_obstaculo;
    private Double latitud;
    private Double longitud;
    private Integer id_ruta;

    public Obstaculo() {
    }

    public Obstaculo(Long id) {
        this.id = id;
    }

    public Obstaculo(Long id, String descripcion, Integer id_tipo_obstaculo, Double latitud, Double longitud, Integer id_ruta) {
        this.id = id;
        this.descripcion = descripcion;
        this.id_tipo_obstaculo = id_tipo_obstaculo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.id_ruta = id_ruta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getId_tipo_obstaculo() {
        return id_tipo_obstaculo;
    }

    public void setId_tipo_obstaculo(Integer id_tipo_obstaculo) {
        this.id_tipo_obstaculo = id_tipo_obstaculo;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Integer getId_ruta() {
        return id_ruta;
    }

    public void setId_ruta(Integer id_ruta) {
        this.id_ruta = id_ruta;
    }

}
