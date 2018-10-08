package com.ws_bdd.PersonInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Arma implements Serializable {
    public String tipo;
    public String marca;
    public String serie;
    public String tag;

}
