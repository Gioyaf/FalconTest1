package com.ws_bdd;
//import com.sun.java.swing.plaf.windows.TMSchema;

import java.io.InputStream;
import java.util.Properties;

public class ConfigProp {
    private static ConfigProp cp = new ConfigProp();
    private static Properties prop;

    private ConfigProp()
    {
        GetProp();
    }

    public static Properties GetInstance()
    {
        return prop;
    }

    private void GetProp()
    {
        if (prop ==null)
            prop = new Properties();
        InputStream is =  getClass().getClassLoader().getResourceAsStream("config.properties");

        try {
            prop.load(is);
        }
        catch (Exception e) {
            System.out.println("Fail at loading Config.properties file.");
        }
    }
}
