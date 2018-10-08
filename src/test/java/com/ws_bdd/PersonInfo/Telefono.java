package com.ws_bdd.PersonInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Telefono {
    public String numero;
    public String type;
    public String tag;
}
