package com.ws_bdd;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.sql.Driver;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class commonMethods {

    /**
     * @param objectRepoPath The file name of the relevant Object Repository.
     * @param fieldNameParam The key Value of the wanted element in the Object Repository.
     * @return Mapping expression of the wanted element.
     * @throws Throwable
     */
    List<List<String>> lines = new ArrayList<>();
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
    public static String parseItemFromObjectRepository(String objectRepoPath, String fieldNameParam) throws Throwable {

        String path = ConfigProp.GetInstance().getProperty("repositoryRelativePath");
        JSONParser parser = new JSONParser();
  //      Object obj = parser.parse(new FileReader(System.getProperty("user.dir")+path+objectRepoPath));
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(System.getProperty("user.dir")+path+objectRepoPath), StandardCharsets.UTF_8);
        Object obj = parser.parse(inputStreamReader);
        JSONObject jsonObject = (JSONObject) obj;
        String fieldName = (String) jsonObject.get(fieldNameParam);
        return fieldName;
    }

    public void csvReaderLine() throws Exception {
        BufferedReader crunchifyBuffer = null;

        try {
            String crunchifyLine;
            crunchifyBuffer = new BufferedReader(new FileReader("D:\\git-ripo\\ws\\ws_bdd\\ws_bdd\\src\\main\\resources\\caseConfigJson\\EntitiesData.csv"));

            // How to read file in java line by line?
            while ((crunchifyLine = crunchifyBuffer.readLine()) != null) {
                System.out.println("Raw CSV data: " + crunchifyLine);
                System.out.println("Converted ArrayList data: " + crunchifyCSVtoArrayList(crunchifyLine) + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (crunchifyBuffer != null) crunchifyBuffer.close();
            } catch (IOException crunchifyException) {
                crunchifyException.printStackTrace();
            }
        }
    }

    // Utility which converts CSV to ArrayList using Split Operation
    public static ArrayList<String> crunchifyCSVtoArrayList(String crunchifyCSV) {
        ArrayList<String> crunchifyResult = new ArrayList<String>();

        if (crunchifyCSV != null) {
            String[] splitData = crunchifyCSV.split("\\s*,\\s*");
            for (int i = 0; i < splitData.length; i++) {
                if (!(splitData[i] == null) || !(splitData[i].length() == 0)) {
                    crunchifyResult.add(splitData[i].trim());
                }
            }
        }
        System.out.println(crunchifyResult.get(0));
        return crunchifyResult;
    }


    public void csvReader() throws Exception {

        String csvFile = "D:\\git-ripo\\ws\\ws_bdd\\ws_bdd\\src\\main\\resources\\caseConfigJson\\EntitiesData.csv";
        List<String> personLine = null;
        Scanner scanner = new Scanner(new File(csvFile));
        while (scanner.hasNext()) {
            List<String> line = parseLine(scanner.nextLine());
            System.out.println("Country [id= " + line.get(0) + ", code= " + line.get(1) + " , name=" + line.get(2) + "]");

        }
        scanner.close();

    }
    ///////// assaf //////////
    public static List<ArrayList<String>> MycsvReader(String path) throws Exception {
        String csvFile = path;
        List<ArrayList<String>> personLine = new ArrayList<>();
        Scanner scanner = new Scanner(new File(csvFile));
        while (scanner.hasNext()) {
            List<String> line = parseLine(scanner.nextLine());
            ArrayList<String> temp = new ArrayList<>(line);
            personLine.add(temp);
        }
        scanner.close();
        return personLine;
    }
    
    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }
public List<List<String>> csvPars()
{
    String fileName= "src\\main\\resources\\caseConfigJson\\EntitiesData.csv";
    File file= new File(fileName);
    // this gives you a 2-dimensional array of strings

    Scanner inputStream;

    try{
        inputStream = new Scanner(file);

        while(inputStream.hasNext()){
            String line= inputStream.next();
            String[] values = line.split(",");
            // this adds the currently parsed line to the 2-dimensional string array
            lines.add(Arrays.asList(values));
        }

        inputStream.close();
    }catch (FileNotFoundException e) {
        e.printStackTrace();
    }

    // the following code lets you iterate through the 2-dimensional array
    int lineNo = 1;
    for(List<String> line: lines) {
        int columnNo = 1;
        for (String value: line) {
         //   System.out.println("name " + lineNo + " last name " + columnNo + "Nationality: " + value);
            columnNo++;
        }
        lineNo++;
    }
    return lines;
}
}

