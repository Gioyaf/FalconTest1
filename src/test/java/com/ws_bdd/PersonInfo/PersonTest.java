package com.ws_bdd.PersonInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;
//import com.jayway.jsonpath.JsonPath;

import java.io.File;
import java.io.IOException;

public class PersonTest {
    public static void main(String[] args) throws IOException {
        Person person = new Person();

        person.personInfo.name = "Elad";
        person.personInfo.lastName = "Or";
        person.address.address = "Mexico, Mexico, México, Naucalpan de juárez, Naucalpan de juárez Tegucigalpa";
        person.address.pais = "Mexico";
        person.address.estado = "México";
        person.address.delegaci = "Nezahualcóyotl";
        person.address.colonia = "Ciudad Nezahualcóyotl";
        person.address.coorLat = "19.425994";
        person.address.coorlong = "-99.041372";

        person.arma.tipo = "Ametralladora";
        person.arma.marca = "Adi";

        person.activo.nombre = "Elad";
        person.activo.tipo = "Casa";

        person.telefono.numero = "97250558877";

        person.vehiculo.matricula = "6766676";
        person.vehiculo.tipo = "Autobus";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json;
        try {
            json = objectMapper.writeValueAsString(person);
            //from json
            Person jsonPersonData = objectMapper.readValue(json, Person.class);
            //get from url
            File file = new File("D:\\git-ripo\\ws_bdd\\src\\main\\resources\\personInfoData.json");
            Person jsonPersonFromFile = objectMapper.readValue(file, Person.class);
            System.out.println(jsonPersonFromFile.personInfo.name);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        File file2 = new File("D:\\git-ripo\\ws_bdd\\src\\main\\resources\\personInfoData.json");
        JsonNode jsonNode = objectMapper.readTree(file2);
        String jsonEx = "address/organization";
        Object result = getVaule(jsonNode, jsonEx);
        System.out.println("Daniel: " + result);


    }

    public static Object getVaule(JsonNode root, String pathExpr){
        String[] paths = pathExpr.split("/");
        JsonNode result = root;
        for (String path : paths){
            result = result.path(path);
        }

        return result;
    }
}
