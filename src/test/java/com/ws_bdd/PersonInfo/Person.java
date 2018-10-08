package com.ws_bdd.PersonInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person implements Serializable {
    public Person()
    {
        this.personInfo = new PersonInfo();
        this.address = new Address();
        this.arma = new Arma();
        this.vehiculo = new Vehiculo();
        this.telefono = new Telefono();
        this.activo = new Activo();
        this.organization = new Organization();
    }
    public PersonInfo personInfo;
    public Address address;
    public Arma arma;
    public Activo activo;
    public Telefono telefono;
    public Vehiculo vehiculo;
    public Organization organization;

}
