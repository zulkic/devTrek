package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table RUTA.
 */
public class Ruta {

    private Long id;
    private String nombre;
    private String descripcion;
    private Float kms;
    private String tiempo_estimado;
    private Boolean oficial;
    private Boolean sincronizada;
    private Boolean favorita;

    public Ruta() {
    }

    public Ruta(Long id) {
        this.id = id;
    }

    public Ruta(Long id, String nombre, String descripcion, Float kms, String tiempo_estimado, Boolean oficial, Boolean sincronizada, Boolean favorita) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.kms = kms;
        this.tiempo_estimado = tiempo_estimado;
        this.oficial = oficial;
        this.sincronizada = sincronizada;
        this.favorita = favorita;
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

    public Boolean getSincronizada() {
        return sincronizada;
    }

    public void setSincronizada(Boolean sincronizada) {
        this.sincronizada = sincronizada;
    }

    public Boolean getFavorita() {
        return favorita;
    }

    public void setFavorita(Boolean favorita) {
        this.favorita = favorita;
    }

}
