package com.ws_bdd.PersonInfo;

import com.ws_bdd.RandomString;

import java.io.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Help_Functions {
    RandomString randomString = new RandomString();
    DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH;mm;ss");
    Date date = new Date();

    public void makeDir(String DirName) {
        String CurrentDate = dateFormat.format(date);

        File file = new File("target\\" + CurrentDate + "\\" + DirName);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Directory is already exist !");
            }
        }
    }

    public String ReadFile() {
        File file = new File(System.getProperty("user.home")+"\\userName.txt");
        String FILENAME = file.getAbsolutePath();

        String sCurrentLine = "";
        {

            try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {

                sCurrentLine = br.readLine();
                System.out.println(sCurrentLine);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return sCurrentLine;
    }

}