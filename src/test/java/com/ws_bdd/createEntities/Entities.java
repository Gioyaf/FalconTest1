package com.ws_bdd.createEntities;

import com.ws_bdd.CucumberToSeleniumMethods;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class Entities {
    String map;
    CucumberToSeleniumMethods func;

    {
        try {
            func = new CucumberToSeleniumMethods();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void createPerson(String entityName) throws Throwable {
        func.typeInputTextIntoField("person_FirstNameField", entityName);
        func.threadSleep(2);
        func.pressOnButton("person_profileImage");
        func.pressOnButton("person_chooseImage");
        func.threadSleep(2);
        func.uploadImageFrom("src\\main\\resources\\photos\\profileImage.jpg");
        func.threadSleep(2);
        func.pressOnButton("common_saveButton");
        func.threadSleep(2);
    }

    public void createTelephone(String entityName) throws Throwable {
        func.typeInputTextIntoField("Telephone_telephoneNumber", entityName);
        func.threadSleep(2);
        func.pressOnButton("common_saveButton");
        func.threadSleep(2);
    }

    public void createVehicle(String entityName) throws Throwable {
        func.typeInputTextIntoField("vehicle_Registration", entityName);
        func.typeInputTextIntoField("vehicle_Tipo", "Autobus");
        func.pressOnENTER();
        func.threadSleep(2);
        func.pressOnButton("common_saveButton");
        func.threadSleep(2);
    }

    public void createProperty(String entityName) throws Throwable {
        func.typeInputTextIntoField("Activo_Nombre", entityName);
        func.typeInputTextIntoField("Activo_Tipo", "Casa");
        func.threadSleep(2);
        func.pressOnENTER();
        func.threadSleep(2);
        func.pressOnButton("common_saveButton");
        func.threadSleep(2);
    }

    public void createOrganization(String entityName) throws Throwable {
        func.typeInputTextIntoField("organization_Registration", entityName);
        func.typeInputTextIntoField("organization_Tipo", "Banco");
        func.threadSleep(2);
        func.pressOnENTER();
        func.threadSleep(2);
        func.pressOnButton("common_saveButton");
        func.threadSleep(2);
    }

    public void createWeapon(String entityName) throws Throwable {
        func.typeInputTextIntoField("Weapon_weaponSeries", entityName);
        func.typeInputTextIntoField("Weapon_weaponType", "Carabina");

        func.threadSleep(1);
        func.pressOnENTER();
        func.threadSleep(1);
        func.typeInputTextIntoField("Weapon_weaponMark", "Adi");
        func.threadSleep(1);
        func.pressOnENTER();
        func.threadSleep(10);
        func.pressOnButton("common_saveButton");

    }
}
