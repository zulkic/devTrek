package com.mapas.franciscojavier.trekkingroute.Utility;

/**
 * Created by juancarlosgonzalezca on 01-07-2015.
 */
public class Wrapper {
    private Integer id;
    private Boolean internet;

    public Wrapper(Integer id, Boolean internet)
    {
        this.id = id;
        this.internet = internet;
    }

    public Boolean getInternet() {
        return internet;
    }

    public void setInternet(Boolean internet) {
        this.internet = internet;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
