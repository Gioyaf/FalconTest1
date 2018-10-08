package com.ws_bdd.PersonInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Organization implements Serializable {
    public String tipo;
    public String company;
    public String tag;

}