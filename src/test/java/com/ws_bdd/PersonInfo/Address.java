package com.ws_bdd.PersonInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Address implements Serializable {
    public String address;
    public String pais;
    public String estado;
    public String delegaci;
    public String colonia;
    public String coorLat;
    public String coorlong;

}
