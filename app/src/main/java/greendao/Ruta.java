package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table RUTA.
 */
public class Ruta {

    private Long id;
    private String name;
    private String descripcion;
    private Float kms;
    private String tiempo_estimado;
    private Boolean oficial;

    public Ruta() {
    }

    public Ruta(Long id) {
        this.id = id;
    }

    public Ruta(Long id, String name, String descripcion, Float kms, String tiempo_estimado, Boolean oficial) {
        this.id = id;
        this.name = name;
        this.descripcion = descripcion;
        this.kms = kms;
        this.tiempo_estimado = tiempo_estimado;
        this.oficial = oficial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getKms() {
        return kms;
    }

    public void setKms(Float kms) {
        this.kms = kms;
    }

    public String getTiempo_estimado() {
        return tiempo_estimado;
    }

    public void setTiempo_estimado(String tiempo_estimado) {
        this.tiempo_estimado = tiempo_estimado;
    }

    public Boolean getOficial() {
        return oficial;
    }

    public void setOficial(Boolean oficial) {
        this.oficial = oficial;
    }

}
