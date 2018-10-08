package com.ws_bdd.PersonInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Activo {
    public String nombre;
    public String tipo;
    public String tag;
}
