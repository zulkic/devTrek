package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table INTERSECCION.
 */
public class Interseccion {

    private Integer id_coordenada;
    private Integer id_ruta;

    public Interseccion() {
    }

    public Interseccion(Integer id_coordenada, Integer id_ruta) {
        this.id_coordenada = id_coordenada;
        this.id_ruta = id_ruta;
    }

    public Integer getId_coordenada() {
        return id_coordenada;
    }

    public void setId_coordenada(Integer id_coordenada) {
        this.id_coordenada = id_coordenada;
    }

    public Integer getId_ruta() {
        return id_ruta;
    }

    public void setId_ruta(Integer id_ruta) {
        this.id_ruta = id_ruta;
    }

}