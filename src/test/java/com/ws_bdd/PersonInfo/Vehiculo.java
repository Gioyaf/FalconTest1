package com.ws_bdd.PersonInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehiculo implements Serializable {
    public String matricula;
    public String tipo;
    public String tag;
}
