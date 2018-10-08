package com.ws_bdd;

import com.fasterxml.jackson.databind.JsonNode;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.ws_bdd.Rest.Cnac.cnacJsonParser;
import com.ws_bdd.Rest.RestClient;
import cucumber.api.PendingException;
//import javafx.util.converter.BooleanStringConverter;
//import org.apache.velocity.runtime.directive.Parse;
//import org.apache.xpath.operations.Bool;
//import org.junit.runner.notification.Failure;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ws_bdd.CaseInfo.Case;
import com.ws_bdd.PersonInfo.Help_Functions;
import com.ws_bdd.PersonInfo.Person;
import com.ws_bdd.createEntities.Entities;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;

import org.openqa.selenium.interactions.Actions;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import java.awt.*;
import java.awt.Rectangle;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Driver;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.elasticsearch.transport.client.PreBuiltTransportClient;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


public class CucumberToSeleniumMethods {
    public static String cnacId;
    WebDriver driver;
    public static Map<String, String> MapID = new HashMap<String, String>();
    Robot robot = new Robot();
    public static String wsID = "*";
    public static String elementID = "*";
    public static String PID = "*";
    public static String casePID = "*";
    public static int entitiesEvidence = 0;
    static RandomString randomString = new RandomString();
    String chatName = randomString.nextString();
    static String uniqueEntitiesName[] = randomString.randomArray(7);
    static ArrayList<ArrayList<String>> listOLists = randomString.listOfEntities();
    static ArrayList<ArrayList<String>> listOfEntities = new ArrayList<ArrayList<String>>();
    WebDriverWait wait;
    commonMethods helpFunc = new commonMethods();
    DateFormat dateFormatDay = new SimpleDateFormat("yyyy.MM.dd");
    DateFormat dateFormatHour = new SimpleDateFormat("HH.mm.ss");
    Date date = new Date();
    StopWatch stopWatch = new StopWatch();
    static String objectRepository;
    //  static WebDriver driver = new ChromeDriver();
    private static String lastRepoName = "";
    Help_Functions Helpi = new Help_Functions();
    private Scenario scenario;
    static DesiredCapabilities capability = DesiredCapabilities.chrome();
    private String userNameFromFile;



    @And("^clean cookies$")
    public void cleanCookies() throws Throwable {
        driver.manage().deleteAllCookies();

    }

    @And("if item \"(.*)\" is displaying on screen then click on \"(.*)\"$")
    public void ifItemDisplay(String expectedItemParam, String itemToClick) throws Throwable {

        String itemName = GetMapping(expectedItemParam);
        stopWatch.reset();
        stopWatch.start();
        WebDriverWait wait = new WebDriverWait(driver, 5);

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        } catch (java.lang.RuntimeException timeoutException) {
            // item is not displaying then just return
            stopWatch.stop();
            return;
        }
        // item is displaying then click on itemToClick and throw exception
        stopWatch.stop();
        pressOnButton(itemToClick);
        throw new Exception();
    }

    @Then("^check if the image \"([^\"]*)\" is on screen$")
    public void checkIfTheImageIsOnScreen(String imagePath) throws Throwable {
        Screen s = new Screen();
        String imageName = imagePath.substring(imagePath.lastIndexOf("\\") + 1);
        try {
            if (s.exists(new Pattern(imagePath).similar((float) 0.01)) != null) {
                s.find(imagePath);
                s.hover(imagePath);
                scenario.write("image: " + imageName + " found");
            }

        } catch (Exception e) {

            writeErrorToReport("cant locate the image: " + imageName);
        }

    }

    @Then("^check if element is on the map by <\"([^\"]*)\">$")
    public void checkIfElementIsOnTheMapBy(String id) throws Throwable {
        Map ret = null;

        try {
            if (driver instanceof JavascriptExecutor) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                ret = (Map) js.executeScript(
                        "return KeyLines.charts.kl.getItem('" + MapID.get("person") + "').posList[0]"
                );
                scenario.write("person found on the map  lat = " + ret.get("lat") + " and lng = " + ret.get("lng"));
                ret = (Map) js.executeScript(
                        "return KeyLines.charts.kl.getItem('" + MapID.get("organization") + "').posList[0]"

                );
                scenario.write("organization found on the map  lat = " + ret.get("lat") + " and lng = " + ret.get("lng"));
                ret = (Map) js.executeScript(
                        "return KeyLines.charts.kl.getItem('" + MapID.get("activo") + "').posList[0]"

                );
                scenario.write("activo found on the map  lat = " + ret.get("lat") + " and lng = " + ret.get("lng"));


            }
        } catch (Exception e) {
            writeErrorToReport("cant locate id :" + id);
        }


    }

    @Then("^check if element is on the map by Test <\"([^\"]*)\">$")
    public void checkIfElementIsOnTheMapTest(String id) throws Throwable {
        Map ret = null;

        try {
            if (driver instanceof JavascriptExecutor) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                ret = (Map) js.executeScript(
                        "return KeyLines.charts.kl.getItem('" + id + "').posList[0]"
                );
                scenario.write("person found on the map  lat = " + ret.get("lat") + " and lng = " + ret.get("lng"));
//                ret = (Map) js.executeScript(
//                        "return KeyLines.charts.kl.getItem('" + MapID.get("organization") + "').posList[0]"
//
//                );
//                scenario.write("organization found on the map  lat = " + ret.get("lat") + " and lng = "+ret.get("lng"));
//                ret = (Map) js.executeScript(
//                        "return KeyLines.charts.kl.getItem('" + MapID.get("activo") + "').posList[0]"
//
//                );
//                scenario.write("activo found on the map  lat = " + ret.get("lat") + " and lng = "+ret.get("lng"));


            }
        } catch (Exception e) {
            writeErrorToReport("cant locate id :" + id);
        }
        System.out.println(ret.get("lng"));


    }

    @And("^Insert \"([^\"]*)\" ID to \"([^\"]*)\"$")
    public void insertIDTo(String entity, String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        if (entity.equals("*")) {
            driver.findElement(By.xpath(itemName)).sendKeys(entity);
        } else {
            driver.findElement(By.xpath(itemName)).sendKeys(MapID.get(entity));
        }

        scenario.write("insert to ui PID :" + MapID.get(entity));
    }

    @Then("^check if the text \"([^\"]*)\" appear in \"([^\"]*)\"$")
    public void checkIfTheTextAppearIn(String textTpSearch, String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        WebElement body = driver.findElement(By.xpath(itemName));
        String bodyText = body.getText();
        bodyText = bodyText.toLowerCase();
        textTpSearch = textTpSearch.toLowerCase();
// count occurrences of the string
        int count = 0;

// search for the String within the text
        while (bodyText.contains(textTpSearch)) {

            // when match is found, increment the count
            count++;

            // continue searching from where you left off
            bodyText = bodyText.substring(bodyText.indexOf(textTpSearch) + textTpSearch.length());
        }
        Assert.assertTrue("The word you are looking for " + textTpSearch + " is not found", 1 <= count);
        scenario.write("the text you are looking for found " + count + " times");
    }

    @Then("^check if element is not exists \"([^\"]*)\"$")
    public void checkIfElementIsNotExists(String itemNameParam) throws Throwable {
        String map = GetMapping(itemNameParam);
        if (driver.findElements(By.xpath(map)).size() != 0) {
            writeErrorToReport("the element " + itemNameParam + " found " + " ObjectRepository name is: " + itemNameParam);
        }
        scenario.write("step pass : element not found ");
    }

    @Then("^check if the filed \"([^\"]*)\" is editable$")
    public void checkIfTheFiledIsEditable(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        WebElement elementTocheck = driver.findElement(By.xpath(itemName));
        String readonly = elementTocheck.getAttribute("readonly");
        Assert.assertNull(readonly);
        scenario.write("the field is editable :" + itemNameParam);
    }

    /**
     * send post request to add canc
     * the JsonNode responceJson hold all the data that return from the server
     * cancId will hold the parse data from the json (only the ID)
     *
     * @param environment ip
     */
    @Then("^create cnac entity in \"([^\"]*)\"$")
    public void createCnacEntityIn(String environment) throws Throwable {
        environment = environment.substring(7, 17);
        RestClient restClient = new RestClient();
        cnacJsonParser cnacJsonParser = new cnacJsonParser();
        JsonNode responceJson = restClient.restPostRequest(environment);
        cnacId = cnacJsonParser.getCancId(responceJson);
        scenario.write("cnac id is :" + cnacId);

    }

    @And("^search the text \"([^\"]*)\" in \"([^\"]*)\"$")
    public void searchTheTextIn(String data, String itemNameParam) throws Throwable {

        String map = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(map)));
        WebElement element = driver.findElement(By.xpath(map));
        Actions actions = new Actions(driver);

        actions.moveToElement(element);
        actions.click();
        if (data.equals("cnacId")) {
            actions.sendKeys(cnacId);
        } else {
            actions.sendKeys(data);
        }
        actions.build().perform();

    }

    @Given("^init mapId to astrix$")
    public void initMapIdToAstrix() throws Throwable {
        setEntitiesMapToAsterisk();
    }

    private void setEntitiesMapToAsterisk() {
        scenario.write("set person MAP ID to Asterisk ");
        MapID.put("person", "*");
        scenario.write("set case MAP ID to Asterisk ");
        scenario.write("set case MAP ID to Asterisk ");
        MapID.put("case", "*");
        scenario.write("set vehicle MAP ID to Asterisk ");
        MapID.put("vehicle", "*");
        scenario.write("set phone MAP ID to Asterisk ");
        MapID.put("phone", "*");
        scenario.write("set weapon MAP ID to Asterisk ");
        MapID.put("weapon", "*");
        scenario.write("set organization MAP ID to Asterisk ");
        MapID.put("organization", "*");
        scenario.write("set activo MAP ID to Asterisk ");
        MapID.put("activo", "*");
    }

    @And("^refresh page$")
    public void refrashPage() throws Throwable {
        driver.navigate().refresh();
    }

    @And("^constructor")
    public void constractor() throws Throwable {
        casePID = " CA31";
        MapID.put("person", "PE1");
        MapID.put("case", "CA1");
        MapID.put("vehicle", "VH1");
        MapID.put("phone", "PH1");
        MapID.put("weapon", "WP1");
        MapID.put("organization", "OR1");
        MapID.put("activo", "AS1");
        MapID.put("news", "NI11");

    }

    ///////   assaf   /////
    ////// check if number of element equals to number of iterations ///////////
    @And("^check if number of element \"([^\"]*)\" equals to number \"([^\"]*)\"$")
    public void checkIfNumberOfElementEqualsToNumber(String itemNameParam, String iteration) throws Throwable {
        int _count = 0;
        int _iteration = 0;
        String itemName = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        threadSleep(1);
        List<WebElement> located_elements = driver.findElements(By.xpath(itemName));
        for (WebElement located_element : located_elements) {
            _count++;
        }
        _iteration = Integer.parseInt(iteration);
        Assert.assertTrue("number of elements no equal to iteration", _count == _iteration);
    }

    //////// assaf //////////////
    @And("^insertRandomAddress$")
    public void insertRandomAddress() throws Throwable {
        pressOnButton("map_define");

        List<ArrayList<String>> addressLists;
        addressLists = commonMethods.MycsvReader("src\\main\\resources\\addresses\\addresses.csv");
        int rand = 0;
        while (rand == 0) {
            rand = new Random().nextInt(addressLists.size());
        }
        List<String> address = addressLists.get(rand);
        for (int i = 0; i < address.size(); i++) {
            switch (i) {
                case 0:
                    insertRandomAddressHelper("map_pais", address.get(0));
                    break;
                case 1:
                    insertRandomAddressHelper("map_Estado", address.get(1));
                    break;
                case 2:
                    insertRandomAddressHelper("map_delegaci", address.get(2));
                    break;
                case 3:
                    insertRandomAddressHelper("map_localidad", address.get(3));
                    break;

            }
        }
        pressOnButton("map_MapOKButton");
    }

    //////// assaf //////////////
    public void insertRandomAddressHelper(String itemNameParam, String address) throws Throwable {
        waitUntillItemDisplay(itemNameParam, 10);
        typeInputTextIntoField(itemNameParam, address);
        threadSleep(2);
        pressOnENTER();
    }

    @And("^insert date \"([^\"]*)\" into \"([^\"]*)\"$")
    public void insertDateInto(String date, String btn) throws Throwable {
        DateInput(date,btn);
    }

    //////// assaf //////////////
    public void DateInput(String date, String btn_name) throws Throwable {
        String btnName = GetMapping(btn_name);
        String DateHeader = GetMapping("Common_DateHeader");
        String DatePerv = GetMapping("Common_DatePerv");
        String calenderDay = GetMapping("Common_calenderDay");
        String[] DateArr = date.split("/");
        String day;
        String month;
        String year;
        if (DateArr.length != 0) {
            day = DateArr[1];
            month = String.valueOf(Integer.parseInt(DateArr[0]) - 1);
            year = DateArr[2];
            driver.findElement(By.xpath(btnName)).click();
            if (btn_name == "Common_Hasta") {
                DateHeader = DateHeader.replace("1", "2");
                DatePerv = DatePerv.replace("1", "2");
                calenderDay = calenderDay.replace("1", "2");
            }
            driver.findElement(By.xpath(DateHeader)).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(DateHeader)));
            WebElement dateWidget = driver.findElement(By.xpath(DateHeader));
            if (!dateWidget.getText().equals(year)) {
                do {
                    driver.findElement(By.xpath(DatePerv)).click();
                } while (!dateWidget.getText().equals(year));
            }

            String calenderMonth = GetMapping("Common_calenderMonth");
            calenderMonth = calenderMonth.replace("x", month);
            driver.findElement(By.xpath(calenderMonth)).click();

            calenderDay = calenderDay.replace("z", day);
            driver.findElement(By.xpath(calenderDay)).click();
        }
    }

    @And("^heatNews \"([^\"]*)\"$")
    public void heatnews(String path) throws Throwable {

    }

    @And("^somthing$")
    public void somthing() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    //////// assaf //////////////
    @And("^read csv file from \"([^\"]*)\" and create cases for \"([^\"]*)\" and \"([^\"]*)\"$")
    public void readCsvFileFromAndCreateCasesForAnd(String path, String url, String userName) throws Throwable {
        List<ArrayList<String>> addressLists;
        addressLists = commonMethods.MycsvReader(path);
        int count = 0;
        for (int i = 1; i < addressLists.size(); i++) {
            try {
                pressOnButton("topbar_addButton");
                threadSleep(1);
                pressOnButton("topbar_newCase");
                threadSleep(1);
                typeInputTextIntoField("case_nameOfTheCase", addressLists.get(i).get(7));
                typeInputTextIntoField("case_startInformation", "this is line number " + i);
                if (!addressLists.get(i).get(5).equals("")) {
                    typeInputTextIntoField("case_tipoDeDelito", addressLists.get(i).get(5));
                    threadSleep(1);
                    pressOnENTER();
                }
                pressOnButton("case_lugarYFecha");
                pressOnButton("map_define");
                pressOnButton("map_OpenCoordinates");
                threadSleep(1);
                typeInputTextIntoField("map_lat", addressLists.get(i).get(3));
                threadSleep(1);
                typeInputTextIntoField("map_long", addressLists.get(i).get(2));
                threadSleep(1);
                pressOnButton("map_searchCoordinate");
                threadSleep(3);
                pressOnButton("map_MapOKButton");
                if (!addressLists.get(i).get(4).equals("")) {
                    DateInput(addressLists.get(i).get(4), "Common_Desde");
                }
                pressOnButton("Common_saveButton");
                threadSleep(5);
                if (count > 0) {
                    logout();
                    threadSleep(1);
                    cleanCookies();
                    loginInToWithUserAndPassword(url, userName, "Pass123");
                    count = 0;
                }
                count++;
            } catch (NoSuchElementException e) {
                logout();
                threadSleep(1);
                cleanCookies();
                loginInToWithUserAndPassword(url, userName, "Pass123");
                count = 0;
            }
        }
    }

    ////assaf/////////
    @And("^read csv file from \"([^\"]*)\" and create news for \"([^\"]*)\" and \"([^\"]*)\"$")
    public void readCsvFileFromAndCreateNewsForAnd(String path, String url, String userName) throws Throwable {
        List<ArrayList<String>> addressLists;
        addressLists = commonMethods.MycsvReader(path);
        int count = 0;
        for (int i = 1; i < addressLists.size(); i++) {
            try {
                pressOnButton("topbar_addButton");
                threadSleep(1);
                pressOnButton("topbar_addNews");
                threadSleep(1);
                pressOnButton("NewsRules_addLabel");
                pressOnENTER();
                typeInputTextIntoField("NewsRules_themeInput", addressLists.get(i).get(8));
                pressOnButton("NewsRules_input", 0);
                threadSleep(1);
                pressOnENTER();
                pressOnButton("NewsRules_input", 1);
                threadSleep(1);
                pressOnENTER();
                pressOnButton("NewsRules_input", 2);
                threadSleep(1);
                pressOnENTER();
                pressOnButton("NewsRules_input", 3);
                threadSleep(1);
                pressOnENTER();
                pressOnButton("NewsRules_input", 4);
                threadSleep(1);
                pressOnENTER();
                typeInputTextIntoField("NewsRules_textArea", "general");
                pressOnButton("NewsRules_lugarYFecha");
                pressOnButton("map_define");
                pressOnButton("map_OpenCoordinates");
                threadSleep(1);
                typeInputTextIntoField("map_lat", addressLists.get(i).get(1));
                threadSleep(1);
                typeInputTextIntoField("map_long", addressLists.get(i).get(0));
                threadSleep(1);
                pressOnButton("map_searchCoordinate");
                threadSleep(3);
                pressOnButton("map_MapOKButton");
                if (!addressLists.get(i).get(4).equals("")) {
                    DateInput(addressLists.get(i).get(4), "Common_Desde");
                }
                pressOnButton("NewsRules_publishButton");
                threadSleep(5);
                if (count > 0) {
                    logout();
                    threadSleep(1);
                    cleanCookies();
                    loginInToWithUserAndPassword(url, userName, "Pass123");
                    count = 0;
                }
                count++;

            } catch (NoSuchElementException e) {
            logout();
            threadSleep(1);
            cleanCookies();
            loginInToWithUserAndPassword(url, userName, "Pass123");
            count = 0;
          }
        }
    }



    public class UserData {
        public String cases;
        public String orders;
        public String RIPs;
    }

    public UserData[] userData = new UserData[3];

    public class MapLocation {
        public String country;
        public String state;
        public String delegation;
        public String location;
    }

    public MapLocation[] mapLocation = new MapLocation[10];

//    static WebDriver RemoteDriver;
//
//    static {
//        try {
//            String REMOTE_GRID_IP = System.getenv("REMOTE_GRID_IP");
//            if (REMOTE_GRID_IP == null) {
//                REMOTE_GRID_IP = "10.32.5.155";
//
//            }
//            capability.setBrowserName("chrome");
//            capability.setPlatform(Platform.WINDOWS);
//           RemoteDriver = new RemoteWebDriver(new URL("http://" + REMOTE_GRID_IP + ":4444/wd/hub"), capability);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//    }


   // WebDriverWait WaitRemoteDriver = new WebDriverWait(RemoteDriver, 10);
    private int entittToLegal;

    public CucumberToSeleniumMethods() throws MalformedURLException, AWTException {
    }

    @Before
    public void before(Scenario scenario) {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        LoggingPreferences loggingprefs = new LoggingPreferences();
        loggingprefs.enable(LogType.BROWSER, Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);
        driver = new ChromeDriver(capabilities);
        driver.manage().deleteAllCookies();
        this.scenario = scenario;
        wait = new WebDriverWait(driver, 10);

    }

    @After()
    public void tearDown(Scenario scenario) throws Throwable {
        System.out.println("Scenario " + scenario.getName() + " Finished ");

        if (scenario.isFailed()) {
            writeErrorToReport("The test failed console logs added to the report :\n");

        }
      closeBrowser();
    }

    public void getConsoleLogs() {

        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry logEntry : logEntries.getAll()) {
            scenario.write("BrowserConsole: " + logEntry.toString());
        }
    }

    @And("test")
    public void testJS() throws Throwable {
        String item = GetMapping("login_userNameField");
        Actions action = new Actions(driver);
        action.sendKeys(Keys.F12).perform();
        JavascriptExecutor js;
        if (driver instanceof JavascriptExecutor) {
            js = (JavascriptExecutor) driver;

            WebElement element = driver.findElement(By.xpath(item));
            //   js.executeScript("arguments[0].style.border='3px solid red'", element);
            js.executeScript("debugModeOn()", element);
        }
    }

    @Given("^check for error messages using the image \"(.*)\"$")
    public void findErrorThread(String errorImagePath) {
        File file = new File(errorImagePath);
        String imagePath = file.getAbsolutePath();
        Timer time = new Timer(); // Instantiate Timer Object
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    clickOnButtonByTheImageThread(imagePath);
                } catch (Throwable throwable) {

                }
            }
        }, 0, 3000); // Create Repetitively task for every 3 secs
    }

    public void writeErrorToReport(String ErrorMessage) throws IOException {
        Helpi.makeDir(scenario.getName());
        String CurrentDateDay = dateFormatDay.format(date);
        String CurrentDateHour = dateFormatHour.format(date);
        String ScenarioName = scenario.getName();
        String fileLoaction = "target\\err\\" + CurrentDateDay + "\\" + ScenarioName + "\\" + CurrentDateHour + ".png";
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        System.out.println("save the image");
        FileUtils.copyFile(scrFile, new File(fileLoaction));
        System.out.println("save the image" + fileLoaction);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        scenario.write(" Time stamp is :" + timeStamp);
        scenario.embed(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png");
        scenario.write(ErrorMessage + " and images save in \n" + fileLoaction);
        getConsoleLogs();

    }

    /**
     * @param itemNameParam Contains the element name and/or relevant repository name by the template: repoName_elemName
     * @return The element's Mapping expression.
     * @throws Throwable
     */
    public String GetMapping(String itemNameParam) throws Throwable {
        String item = "";
        String repoName = "";
        //[0] - the name of the relevant repository [1] - the key name of the element.
        String[] strArr = itemNameParam.split("_");

        if (strArr.length == 1) {
            if (!lastRepoName.equals("")) {
                repoName = lastRepoName;
                item = itemNameParam;
            } else
                throw new Exception("Item name must include repo name --> repoName_itemName");
        } else if (strArr.length == 2) {
            repoName = strArr[0];
            item = strArr[1];
        }

        String itemName = commonMethods.parseItemFromObjectRepository(repoName, item);

        lastRepoName = strArr[0].toLowerCase();
        return itemName;
    }


    @Given("^Object repository for all GUI items is \"(.*)\"$")
    public void defineObjectRepository(String objectRepositoryName) {
        objectRepository = objectRepositoryName;
    }

    @And("^navigate to \"(.*)\" on browser$")
    public void navigate_to_URL_on_browser(String pathUrl) throws Throwable {
        cleanCookies();
        scenario.write("clean cookies");
        String IP = System.getenv("SANITY_IP");

        if (IP == null) {
            scenario.write("login to :" + pathUrl);
            driver.get(pathUrl);
            driver.manage().window().maximize();
        } else {
            IP = "http://" + IP + "/ui/";
            //IP = "http://" + IP + ":8080";
            driver.get(IP);
            driver.manage().window().maximize();
            scenario.write("login to :" + IP);
        }

    }


    @When("^type into \"(.*)\" the text \"(.*)\"$")
    public void typeInputTextIntoField(String itemNameParam, String inputText) throws Throwable {
        String map = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(map)));
        threadSleep(1);
        paintElement(map);
        driver.findElement(By.xpath(map)).sendKeys(inputText);
        scenario.write("insert data :" + inputText);
    }

    @When("^press on button \"(.*)\"$")
    public void pressOnButton(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        try {
            threadSleep(1);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
            threadSleep(1);
            paintElement(itemName);
            driver.findElement(By.xpath(itemName)).click();
            scenario.write("click on button :" + itemNameParam);
        } catch (Exception e) {
            writeErrorToReport("cant locate the button :" + itemName + " ObjectRepository name is" + itemNameParam);

        }

    }

    /**
     * Description: search User unique ID (WS ID PE.*) save it and search it on DB
     *
     * @param itemNameParam the path to the element
     * @return unique ID (WS ID PE.*)
     * @throws Throwable
     */
    @When("^get user id \"(.*)\"$")
    public void getUserId(String itemNameParam) throws Throwable {
        String UserId;
        String itemName = GetMapping(itemNameParam);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        UserId = driver.findElement(By.xpath(itemName)).getText();
        typeInputTextIntoField("topbar_mainSearchField", UserId);
        PID = UserId;
        scenario.write("user PID is :" + PID);


    }

    @When("^check if text from \"(.*) contain \"(.*)\"$")
    public void checkContainData(String itemNameParam, String containedData) throws Throwable {
        String uiText;
        String itemName = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        uiText = driver.findElement(By.xpath(itemName)).getText();
        //Assert.assertTrue(uiText.contains(containedData));
        System.out.println(uiText);

    }

    @When("^ text from \"(.*)\"$")
    public String getTextFromUi(String itemNameParam) throws Throwable {
        String uiText;
        String itemName = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        uiText = driver.findElement(By.xpath(itemName)).getText();
        scenario.write("The data from the UI is :" + uiText);
        System.out.println(uiText);
        return uiText;
    }

    @When("^Insert PID to \"(.*)\"$")
    public void insertPID(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        String tmp = elementID;

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        driver.findElement(By.xpath(itemName)).sendKeys(elementID);
        scenario.write("insert to ui PID :" + elementID);
    }


    @When("^scroll down from element \"(.*)\"$")
    public void ScrollDown(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);

        driver.findElement(By.xpath(itemName)).sendKeys(Keys.PAGE_DOWN);
    }

    @When("^edit user data on path \"(.*)\"$")
    public void EditUserData(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        driver.findElement(By.className(itemName)).click();

    }

    @When("^press on tab number \"(.*)\" of \"(.*)\"$")
    public void pressOnTabFromTabs(String tabNumber, String itemNameParam) throws Throwable {
        String XpathNewTab;
        String itemName = GetMapping(itemNameParam);
        XpathNewTab = itemName.replaceAll("num", tabNumber);

        driver.findElement(By.xpath(XpathNewTab)).click();
        scenario.write("move to tab number :" + tabNumber);
    }

    @When("^select item \"(.*)\" from dropdown list \"(.*)\"$")
    public void selectButton(int index, String dropdownListNameParam) throws Throwable {
        String dropdownName = GetMapping(dropdownListNameParam);
        //String dropdownName = commonMethods.parseItemFromObjectRepository(objectRepository,dropdownListNameParam);
        Select dropdown = new Select(driver.findElement(By.xpath(dropdownName)));
        dropdown.selectByIndex(index);
    }

    @And("^press on tab button on element \"([^\"]*)\"$")
    public void pressOnTabButtonOnElement(String arg0) throws Throwable {

        robot.keyPress(KeyEvent.VK_TAB);
        Thread.sleep(1000);
        robot.keyRelease(KeyEvent.VK_TAB);

    }

    @When("^press on ENTER$")
    public void pressOnENTER() throws Throwable {

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        scenario.write("press on enter button");

    }

    @When("^press on DownArrow")
    public void pressOnDownArrow() throws Throwable {

        robot.keyPress(KeyEvent.VK_DOWN);
        Thread.sleep(1000);
        robot.keyRelease(KeyEvent.VK_DOWN);
        scenario.write("press on DownArrow button");
    }

    @When("^press on Page Down")
    public void pressOnPageDown() throws Throwable {
        robot.keyPress(KeyEvent.VK_PAGE_DOWN);
        Thread.sleep(1000);
        robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
    }

    @When("^Hold for \"(.*)\" seconds$")
    public void threadSleep(int seconds) throws Throwable {
        Thread.sleep(seconds * 1000);
    }

    @When("^select \"(.*)\" item from \"(.*)\" dropdown menu$")
    public void moveToElement(String itemNameParam, String dropdown) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        String dropdownmap = GetMapping(dropdown);
        WebElement element = driver.findElement(By.xpath(itemName));
        String search = itemName + dropdownmap;
        Point dropdownLocation = driver.findElement(By.xpath(search)).getLocation();
        Point FieldLocation = element.getLocation();
        robot.mouseMove(0, 0);
        robot.mouseMove(dropdownLocation.x + FieldLocation.x, dropdownLocation.y + FieldLocation.y + (element.getSize().getHeight() * 5 / 2));
    }

    @Then("wait for \"(.*)\" is displaying on screen with timeout of \"(.*)\"$")
    public void waitUntillItemDisplay(String expectedItemParam, int timeOut) throws Throwable {
        String itemName = GetMapping(expectedItemParam);
        stopWatch.reset();
        stopWatch.start();

        WebDriverWait wait = new WebDriverWait(driver, timeOut);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        stopWatch.stop();
        scenario.write("the time to locate the item :" + expectedItemParam + " is :" + stopWatch);
    }

    @Then("wait for \"(.*)\" is not displaying on screen with timeout of \"(.*)\"$")
    public void waitUntillItemNotDisplay(String expectedItemParam, int timeOut) throws Throwable {
        WebDriverWait waitToLogo = new WebDriverWait(driver, timeOut);
        String itemName = GetMapping(expectedItemParam);
        waitToLogo.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        stopWatch.reset();
        stopWatch.start();
        scenario.write("wait for " + expectedItemParam + " to disappear");
        waitToLogo.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(itemName)));
        stopWatch.stop();
        //if (Integer.parseInt(stopWatch))
        if (Long.valueOf(stopWatch.getTime()) > 10) {
            writeErrorToReport("the time to file to be created is bigger then 10 sec its :" + stopWatch);
        }
        scenario.write("the time item :" + expectedItemParam + " is not display  :" + stopWatch);

    }

    @Then("^close the browser$")
    public void closeBrowser() throws Throwable {
        // driver.close();
        driver.quit();
        scenario.write("close the browser");
    }

    @Then("^the \"(.*)\" from the list is \"(.*)\"$")
    public void the_n_result_in_the_List_equals_to_expected_output(int resultNumber, String resultValue) throws Throwable {
        String bodyText = driver.findElement(By.xpath("(//*[@class='r'])[" + resultNumber + "]")).getText();
        Assert.assertTrue("Text not found!", bodyText.contains(resultValue));
    }

    @Then("list of results \"(.*)\" not containing the item \"(.*)\"$")
    public void checkThatItemNotInList(String listNameParam, String searchValue) throws Throwable {
        String listName = GetMapping(listNameParam);
        //String listName = commonMethods.parseItemFromObjectRepository(objectRepository, listNameParam);
        try {
            Assert.assertFalse("Text was fond although it shouldn't be!", driver.findElement(By.xpath(listName)).findElement(By.xpath("//*[text()='" + searchValue + "']")).isDisplayed());
        } catch (NoSuchElementException e) {
            writeErrorToReport("No element exist on the list with the value: " + searchValue);
            System.out.println("No element exist on the list with the value: " + searchValue);
        }
    }

    @Then("check if field \"(.*)\" containing the string \"(.*)\"$")
    public void checkIfFieldContainString(String itemNameParam, String value) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        //String listName = commonMethods.parseItemFromObjectRepository(objectRepository, listNameParam);
        try {
            String tmp = driver.findElement(By.xpath(itemName)).getText();
            System.out.println(tmp);
            Assert.assertTrue("The field not containing the expected value", value.contains(driver.findElement(By.xpath(itemName)).getText()));
        } catch (NoSuchElementException e) {
            writeErrorToReport("No element exist");
            System.out.println("No element exist");
        }
    }

    @Then("check if total Entities \"(.*)\" is equal to \"(.*)\"$")
    public void assertTotalEntities(String itemNameParam, String value) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        try {
            String tmp = driver.findElement(By.xpath(itemName)).getText();
            System.out.println(tmp);
            String[] splited = tmp.split("\\s+");
            String totalEntities = splited[0];
            Assert.assertEquals("The Actual Total of Entities :" + totalEntities + " is not equal to the expected Total:" + value, totalEntities, value);

        } catch (NoSuchElementException e) {
            writeErrorToReport("No element exist");
            System.out.println("No element exist");
        }
    }

    @Then("list of results \"(.*)\" containing the item \"(.*)\"$")
    public void checkThatItemExistInList(String listNameParam, String searchValue) throws Throwable {
        String listName = GetMapping(listNameParam);
        //String listName = commonMethods.parseItemFromObjectRepository(objectRepository, listNameParam);
        try {
            Assert.assertTrue(driver.findElement(By.xpath(listName)).findElement(By.xpath("//*[text()='" + searchValue + "']")).isDisplayed());
        } catch (NoSuchElementException e) {

            Assert.assertTrue("No element exist on the list with the value: " + searchValue, false);
            writeErrorToReport("No element exist on the list with the value: " + searchValue);
        }

    }

    @Then("The expected  \"(.*)\" is Equal to the actual \"(.*)\"$")
    public void AssertIsExpectedEqualToActual(String listNameParam, String Expected) throws Throwable {

        String Actual;
        String itemName = GetMapping(listNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        Actual = driver.findElement(By.xpath(itemName)).getText();
        System.out.println(Actual);


        Assert.assertEquals("the Actual result is: " + Actual + " and we Expected to: " + Expected, Expected, Actual);

    }

    @Then("Insert data from \"(.*)\" to \"(.*)\"$")
    public void InsertDataFromJson(String filePath, String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        ///////Read the data from the json///////////////
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File file = new File(filePath);
        Case jsonCaseFromFile = objectMapper.readValue(file, Case.class);
        Person jsonPersonFromFile = objectMapper.readValue(file, Person.class);
        ////////////////////////////////////////////////////
        String map = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(map)));
        switch (itemNameParam) {
            case "case_caseReportsDescription":
                driver.findElement(By.xpath(map)).sendKeys(jsonCaseFromFile.report.reportDescription);
                break;
            case "case_caseReportsName":
                driver.findElement(By.xpath(map)).sendKeys(jsonCaseFromFile.report.reportName);
                break;
            case "case_startInformation":
                driver.findElement(By.xpath(map)).sendKeys(jsonCaseFromFile.caseInfo.information);
                break;
            case "case_nameOfTheCase":
                driver.findElement(By.xpath(map)).sendKeys(jsonCaseFromFile.caseInfo.name);
                break;
            case "case_ObservationsTextArea":
                driver.findElement(By.xpath(map)).sendKeys(jsonCaseFromFile.observation.observe);
                break;
            case "person_FirstNameField":
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.personInfo.name);
                break;
            case "person_LastNameField":
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.personInfo.lastName);
                break;
            case "map_address":
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.address.address);
                break;
            case "Weapon_weaponSeries": {
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.arma.serie);
                break;
            }
            case "Weapon_weaponType": {
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.arma.tipo);
                break;
            }
            case "Weapon_weaponMark": {
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.arma.marca);
                break;
            }
            case "Activo_Nombre": {
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.activo.nombre);
                break;
            }
            case "Activo_Tipo": {
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.activo.tipo);
                break;
            }
            case "map_pais": {
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.address.pais);
                break;
            }
            case "Telephone_telephoneNumber": {
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.telefono.numero);
                break;
            }
            case "Telephone_teleType": {
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.telefono.type);
                break;
            }

            case "vehicle_Registration": {
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.vehiculo.matricula);
                break;
            }
            case "vehicle_Tipo": {
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.vehiculo.tipo);
                break;
            }
            case "organization_Registration": {
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.organization.company);
                break;
            }
            case "organization_Tipo": {
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.organization.tipo);
                break;
            }
            case "common_tagName": {
                driver.findElement(By.xpath(map)).sendKeys(jsonPersonFromFile.organization.tag);
                break;
            }
        }
    }

    @Then("Read data from  \"(.*)\" and check the \"(.*)\"$")
    public void AseertDataFromJson(String filePath, String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        String expected;
        ///////Read the data from the json///////////////
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File file = new File(filePath);
        Person jsonPersonFromFile = objectMapper.readValue(file, Person.class);
        ////////////////////////////////////////////////////
        String map = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(map)));

        String Actual = driver.findElement(By.xpath(map)).getText();
        switch (itemNameParam) {
            case "map_address":
                expected = jsonPersonFromFile.address.address;
                Assert.assertEquals("the Actual result is: " + Actual + " and we Expected to: " + expected, expected, Actual);
                break;
            case "map_pais": {
                expected = jsonPersonFromFile.address.pais;
                Assert.assertEquals("the Actual result is: " + Actual + " and we Expected to: " + expected, expected, Actual);
                break;
            }
            case "map_Estado":
                expected = jsonPersonFromFile.address.estado;
                Assert.assertEquals("the Actual result is: " + Actual + " and we Expected to: " + expected, expected, Actual);
                break;
            case "map_Delegacion": {
                expected = jsonPersonFromFile.address.delegaci;
                Assert.assertEquals("the Actual result is: " + Actual + " and we Expected to: " + expected, expected, Actual);
                break;
            }
            case "map_colonia": {
                expected = jsonPersonFromFile.address.colonia;
                Assert.assertEquals("the Actual result is: " + Actual + " and we Expected to: " + expected, expected, Actual);
                break;
            }
        }
    }

    @Then("check if item is visible \"(.*)\" with timeout of \"(.*)\"$")
    public void checkIfItemIsVisible(String expectedItemParam, int timeOut) throws Throwable {
        String itemName = GetMapping(expectedItemParam);
        try {
            Thread.sleep(2000);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
            scenario.write("Element " + itemName + " found");
        } catch (Exception e) {
            writeErrorToReport("cant locate the Element " + itemName + "ObjectRepository name is" + expectedItemParam + " check if exist");

        }
    }

    @Then("check if item \"(.*)\" with parameter \"(.*)\" is visible until timeout of \"(.*)\"$")
    public void checkIfItemWithParamIsVisible(String expectedItemParam, String param, int timeOut) throws Throwable {
        String itemName = GetMapping(expectedItemParam);
        itemName.replaceAll("param", param);
        try {
            Thread.sleep(2000);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
            scenario.write("Element " + itemName + " found");
        } catch (Exception e) {
            scenario.write("cant locate the Element " + itemName + "ObjectRepository name is" + expectedItemParam + " check if exist");

        }
    }

    @When("Hover on Element \"(.*)\" and click on \"(.*)\"$")
    public void hoverOnElement(String itemNameParam, String itemNameParam2) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        String itemName2 = GetMapping(itemNameParam2);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        WebElement element = driver.findElement(By.xpath(itemName));
        Actions action = new Actions(driver);

        action.moveToElement(element).perform();
        WebDriverWait wait2 = new WebDriverWait(driver, 10);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(itemName2)));
        WebElement subElement = driver.findElement(By.xpath(itemName2));

        action.moveToElement(subElement);
        subElement.click();

        //  action.perform();

    }

    @Then("Delete all files from folder")
    public void DeleteAllFilesFromFolder() {
        String folderPath = "C:/Users/" + System.getProperty("user.name") + "/Downloads/";
        File filesToDelete = new File(folderPath);
        try {
            FileUtils.cleanDirectory(filesToDelete);
        } catch (IOException e) {
            System.out.println("cant delete folder data check if the folder is exist");
        }

    }

    @Then("check if file exists \"(.*)\"$")
    public void checkIfFileExists(String fileName) throws IOException {
        String folderPath = "C:/Users/" + System.getProperty("user.name") + "/Downloads/";

        File f = new File(folderPath + fileName);
        if (f.exists() && !f.isDirectory()) {
            scenario.write("the file " + fileName + " created successfully");
            System.out.println(fileName + " is created successfully");
        } else {

            writeErrorToReport("the file " + fileName + "NOT created");
        }
    }

    @And("^press on ENTER button on element \"([^\"]*)\"$")
    public void pressOnEnterButtonOnElement(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        try {
            driver.findElement(By.xpath(itemName)).sendKeys(Keys.ENTER);
            scenario.write("the system find the searched element");
        } catch (Exception e) {
            scenario.write("the system Cant find the searched element");
            writeErrorToReport("the system Cant find the searched element");
        }
    }

    @And("^open file$")
    public void openFile() throws Throwable {
        driver.findElement(By.id("myUploadElement")).sendKeys("C:\\Users\\orela\\Desktop\\logo.jpg");

    }

    @Then("^check if button is clickable \"([^\"]*)\"$")
    public void checkIfButtonIsClickable(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(itemName)));
        element.click();
    }

    @Then("^set PID to \"([^\"]*)\"$")
    public void

    setPIDTo(String pid) throws Throwable {
        PID = "*";
        elementID = pid;
        System.out.println(PID);
    }

    @And("^get caseId from \"([^\"]*)\"$")
    public void getCaseIdFrom(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        casePID = driver.findElement(By.xpath(itemName)).getText();
        System.out.println(casePID);

    }

    @And("^get Element ID from \"([^\"]*)\"$")
    public void getElementIdFrom(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        String[] entitiyName = itemNameParam.split("_");
        String entityId = driver.findElement(By.xpath(itemName)).getText();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        System.out.println(entityId);
        String[] splited = entityId.split("\\s+");
        String cleanId = splited[splited.length - 1];
        cleanId = cleanId.substring(0, cleanId.length() - 1);

        switch (entitiyName[0]) {
            case "person":
                scenario.write("person id is :" + cleanId);

                MapID.put("person", cleanId);
                break;
            case "case":
                scenario.write("case id is :" + cleanId);
                System.out.println(cleanId);
                MapID.put("case", cleanId);
                break;
            case "vehicle":
                scenario.write("vehicle id is :" + cleanId);
                System.out.println(cleanId);
                MapID.put("vehicle", cleanId);
                break;
            case "Phone":
                scenario.write("Phone id is :" + cleanId);
                System.out.println(cleanId);

                MapID.put("phone", cleanId);
                break;
            case "Weapon":
                scenario.write("Weapon id is :" + cleanId);
                System.out.println(cleanId);
                MapID.put("weapon", cleanId);
                break;
            case "organization":
                scenario.write("organization id is :" + cleanId);
                System.out.println(cleanId);
                MapID.put("organization", cleanId);
                break;
            case "Activo":
                scenario.write("Activo id is :" + cleanId);
                System.out.println(cleanId);
                MapID.put("activo", cleanId);
                break;
            case "news":
                scenario.write("news id is :" + cleanId);
                System.out.println(cleanId);
                MapID.put("news", cleanId);
                break;

        }


    }


    @And("^search for case pid at \"([^\"]*)\"$")
    public void searchForCasePidAt(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        driver.findElement(By.xpath(itemName)).sendKeys(casePID);
//        Actions actions = new Actions(driver);
//        //actions.click();
//        actions.sendKeys(casePID);
//        actions.build().perform();
    }

    @And("search \"(.*)\" and search field \"(.*)\"$")
    public void searchDataAtSearchField(String searchData, String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        driver.findElement(By.xpath(itemName)).click();
        driver.findElement(By.xpath(itemName)).sendKeys(searchData);

    }

    @And("^search for Element pid at \"([^\"]*)\"$")
    public void searchForElementPidAt(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        driver.findElement(By.xpath(itemName)).sendKeys(elementID);

    }

    @And("^wait for \"([^\"]*)\" to be clickable with timeout of \"([^\"]*)\"$")
    public void waitForToBeClickableWithTimeoutOf(String itemNameParam, String timeout) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(itemName)));
    }

    @Then("^check number of evidence in legal \"([^\"]*)\"$")
    public void checkNumberOfEvidenceIn(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        WebElement body = driver.findElement(By.xpath(itemName));
        String bodyText = body.getText();

// count occurrences of the string
        int count = 0;

// search for the String within the text
        while (bodyText.contains("EVIDENCIA")) {

            // when match is found, increment the count
            count++;

            // continue searching from where you left off
            bodyText = bodyText.substring(bodyText.indexOf("EVIDENCIA") + "EVIDENCIA".length());
        }
        Assert.assertEquals("The expect Evidence is : " + entitiesEvidence + " but we got: " + count, count, entitiesEvidence);

    }

    @Then("^check number of evidence in entities \"([^\"]*)\"$")
    public void checkNumberOfEvidenceInEntities(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        WebElement body = driver.findElement(By.xpath(itemName));
        String bodyText = body.getText();

// count occurrences of the string
        int count = 0;

// search for the String within the text
        while (bodyText.contains("EVIDENCIA")) {

            // when match is found, increment the count
            count++;

            // continue searching from where you left off
            bodyText = bodyText.substring(bodyText.indexOf("EVIDENCIA") + "EVIDENCIA".length());
        }
        entitiesEvidence = count;
        System.out.println(count);

    }
    ////////////////////////////////Gidon/////////////////////////////////

    /**
     * This method will set any parameter string to the system's clipboard.
     */
    public static void setClipboardData(String string) {
        //StringSelection is a class that can be used for copy and paste operations.
        StringSelection stringSelection = new StringSelection(string);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    public static void uploadFile(String fileLocation) {
        try {
            //Setting clipboard with file location
            setClipboardData(fileLocation);
            //native key strokes for CTRL, V and ENTER keys
            Robot robot = new Robot();

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    @And("^upload image from \"([^\"]*)\"$")
    public void uploadImageFrom(String imageName) throws Throwable {
        File file = new File(imageName);
        String path = file.getAbsolutePath();
        uploadFile(path);
        Thread.sleep(2000);
    }

    public static void uploadFolder(String folderLocation) {
        try {
            //Setting clipboard with file location
            setClipboardData(folderLocation);
            //native key strokes for CTRL, V and ENTER keys
            Robot robot = new Robot();

            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    @And("^upload folder from \"([^\"]*)\"$")
    public void uploadFolderFrom(String folderName) throws Throwable {
        File file = new File(folderName);
        String path = file.getAbsolutePath();
        System.out.printf(path);
        uploadFolder(path);
        Thread.sleep(2000);
    }

    @When("^get Pid \"(.*)\"$")
    public void getPid(String itemNameParam) throws Throwable {
        String Pid;
        String itemName = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        Pid = driver.findElement(By.xpath(itemName)).getText();
        wsID = Pid;

    }

    @When("^Insert wsID to \"(.*)\"$")
    public void insertWsPID(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        String tmp = wsID;
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        driver.findElement(By.xpath(itemName)).sendKeys(wsID);
    }

    @Then("list of results \"(.*)\" containing wsId")
    public void checkThatwsIDExistInList(String listNameParam) throws Throwable {
        String listName = GetMapping(listNameParam);
        //String listName = commonMethods.parseItemFromObjectRepository(objectRepository, listNameParam);
        try {
            Assert.assertTrue(driver.findElement(By.xpath(listName)).findElement(By.xpath("//*[text()='" + wsID + "']")).isDisplayed());
        } catch (NoSuchElementException e) {
            Assert.assertTrue("No element exist on the list with the value: " + wsID, false);
        }
        class SomeTestIT {

        }
    }

    @And("^press on button name \"(.*)\" index \"(.*)\"$")
    public void pressOnButton(String itemNameParam, int index) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(itemName)));
            java.util.List<WebElement> elements = driver.findElements(By.xpath(itemName));
            elements.get(index).click();
        } catch (Exception e) {
            writeErrorToReport("cant locate the button :" + itemName + " ObjectRepository name is" + itemNameParam);
        }
    }

    @And("^get number of cases, orders and RIPs \"(.*)\" index \"(.*)\"$")
    public void getNumberOfCasesOredersRIPs(String itemNameParam, int index) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        userData[index] = new UserData();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(itemName)));
        java.util.List<WebElement> elements = driver.findElements(By.xpath(itemName));
        userData[index].cases = elements.get(0).getText();
        userData[index].orders = elements.get(1).getText();
        userData[index].RIPs = elements.get(2).getText();
    }

    @And("^check number of cases, orders and RIPs \"(.*)\" index \"(.*)\"$")
    public void checkNumberOfCasesOrdersRIPs(String itemNameParam, int index) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(itemName)));
        java.util.List<WebElement> elements = driver.findElements(By.xpath(itemName));
        if (!userData[index].cases.equals(elements.get(0).getText())) {
            writeErrorToReport("The case number is not equal.The subordinate case number from manager dashboard is :" + userData[index].cases + " and the subordinate dashboard case is: " + elements.get(0).getText());
            Assert.assertEquals("The case number is not equal.The subordinate case number from manager dashboard is :" + userData[index].cases + " and the subordinate dashboard case is: " + elements.get(0).getText(), userData[index].cases, elements.get(0).getText());
        }
        if (!userData[index].orders.equals(elements.get(1).getText())) {

            writeErrorToReport("The order number is not equal.The subordinate order number from manager dashboard is :" + userData[index].orders + " and the subordinate dashboard order is: " + elements.get(1).getText());
            Assert.assertEquals("The order number is not equal.The subordinate order number from manager dashboard is :" + userData[index].orders + " and the subordinate dashboard order is: " + elements.get(1).getText(), userData[index].orders, elements.get(1).getText());
        }

        if (!userData[index].RIPs.equals(elements.get(2).getText())) {

            writeErrorToReport("The RIP number is not equal.The subordinate RIP number from manager dashboard is :" + userData[index].RIPs + " and the subordinate dashboard RIP is: " + elements.get(2).getText());
            Assert.assertEquals("The RIP number is not equal.The subordinate RIP number from manager dashboard is :" + userData[index].RIPs + " and the subordinate dashboard RIP is: " + elements.get(2).getText(), userData[index].RIPs, elements.get(2).getText());

        }
    }

    @And("^clear field \"(.*)\"$")
    public void clearField(String itemNameParam) throws Throwable {
        String map = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(map)));
        driver.findElement(By.xpath(map)).clear();
    }

    @And("^check if text of \"(.*)\" is equal to \"(.*)\"$")
    public void checkFieldText(String itemNameParam, String text) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(itemName)));
        WebElement element = driver.findElement(By.xpath(itemName));
        if (!text.equals(element.getText())) {
            throw new Exception();
        }
    }

    @And("^press on DELETE$")
    public void pressOnDELETE() throws Throwable {
        robot.keyPress(KeyEvent.VK_DELETE);
        robot.keyRelease(KeyEvent.VK_DELETE);
    }

    @And("^remove all news \"(.*)\" \"(.*)\" \"(.*)\"$")
    public void removeAllNews(String newsItem, String newsMenu, String removeFromNews) throws Throwable {
        String itemName = GetMapping(newsItem);
        while (true) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(itemName)));
            } catch (TimeoutException e) {
                // no more news items then return with no exception
                return;
            }
            hoverOnElement(newsItem, newsMenu);
            Thread.sleep(1000);
            pressOnButton(removeFromNews);
            Thread.sleep(7000);
        }
    }

    @And("^press on left arrow$")
    public void pressOnLeftArrow() throws Throwable {
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);
    }

    @Then("wait for \"(.*)\" is displaying with timeout of \"(.*)\"$")
    public void waitForItemDisplay(String expectedItemParam, int timeOut) throws Throwable {
        String itemName = GetMapping(expectedItemParam);
        stopWatch.reset();
        stopWatch.start();

        WebDriverWait wt = new WebDriverWait(driver, timeOut);
        wt.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        stopWatch.stop();
        scenario.write("the time to locate the item :" + expectedItemParam + " is :" + stopWatch);
    }

    @And("check pixel value at location \"(.*)\" \"(.*)\" minimum \"(.*)\" maximum \"(.*)\"$")
    public void checkPixel(int x, int y, int min, int max) throws Throwable {
        Rectangle area = new Rectangle(0, 0, 1600, 1000);
        BufferedImage image = robot.createScreenCapture(area);
        int color = image.getRGB(x, y);
        int blue = color & 0xff;
        int green = (color & 0xff00) >> 8;
        int red = (color & 0xff0000) >> 16;

        Assert.assertTrue("value of red is " + red + " which is smaller than " + min, red > min);
        Assert.assertTrue("value of red is " + red + " which is greater than " + max, red < max);
        Assert.assertTrue("value of green is " + green + " which is smaller than " + min, green > min);
        Assert.assertTrue("value of green is " + green + " which is greater than " + max, green < max);
        Assert.assertTrue("value of blue is " + blue + " which is smaller than " + min, blue > min);
        Assert.assertTrue("value of blue is " + blue + " which is greater than " + max, blue < max);
    }

    @And("^press on location \"(.*)\" \"(.*)\" and move to \"(.*)\" \"(.*)\"$")
    public void pressAndMove(int x, int y, int moveX, int moveY) throws Throwable {
        try {
            robot.mouseMove(x, y);
            threadSleep(1);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            threadSleep(1);
            robot.mouseMove(moveX, moveY);
            threadSleep(1);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        } catch (Exception e) {
        }
    }

    @And("^click on location \"(.*)\" \"(.*)\"$")
    public void clickLocation(int x, int y) throws Throwable {
        try {
            robot.mouseMove(x, y);
            threadSleep(1);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            threadSleep(1);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        } catch (Exception e) {
        }
    }

    @And("^right click on location \"(.*)\" \"(.*)\"$")
    public void rightClickLocation(int x, int y) throws Throwable {
        try {
            robot.mouseMove(x, y);
            threadSleep(1);
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            threadSleep(1);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        } catch (Exception e) {
        }
    }

    @And("^check if \"(.*)\" value is between \"(.*)\" to \"(.*)\"$")
    public void checkFieldValue(String itemNameParam, String minimum, String maximum) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        String Actual = driver.findElement(By.xpath(itemName)).getText();
        float min = Float.parseFloat(minimum);
        float max = Float.parseFloat(maximum);
        String arr1[] = Actual.split("km");
//        String arr2[] = arr1[0].split("D:");
//        float val = Float.parseFloat(arr2[1]);
        String tempStr = arr1[0].replaceAll("[^\\d.]", "");
        float val = Float.parseFloat(tempStr);
        Assert.assertTrue("value is " + val + " which is smaller than " + min, val > min);
        Assert.assertTrue("value is " + val + " which is greater than " + max, val < max);
    }

    @And("^type to \"(.*)\" index \"(.*)\" the text \"(.*)\"$")
    public void typeTextIntoField(String itemNameParam, int index, String inputText) throws Throwable {
        String map = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(map)));
        threadSleep(1);
        java.util.List<WebElement> elements = driver.findElements(By.xpath(map));
        elements.get(index).sendKeys(inputText);
        scenario.write("insert data :" + inputText);
    }

    @Then("The expected \"(.*)\" start with the actual \"(.*)\"$")
    public void AssertIsStartWithTheActual(String listNameParam, String Expected) throws Throwable {

        String itemName = GetMapping(listNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        String Actual = driver.findElement(By.xpath(itemName)).getText();
        String[] actual = Actual.split("\n");
        System.out.println(actual[0]);
        Assert.assertEquals("the Actual result is: " + actual[0] + " and we Expected to: " + Expected, Expected, actual[0]);
    }


    @And("Zoom In \"(.*)\" Zoom Out \"(.*)\" zoom count \"(.*)\" \"(.*)\" times$")
    public void ZoomInOut(String zoomIn, String zoomOut, int count, int times) throws Throwable {
        readLocationFile();
        userNameFromFile = "wstu36";
        if (userNameFromFile == null)
            userNameFromFile = "wstu36";
        int no = Integer.parseInt(userNameFromFile.substring(4)) % 10;
        int n = (no == 0) ? 9 : no - 1;
        insertLocation(n);

        for (int i = 0; i < times; i++) {
            for (int j = 0; j < count; j++) {
                pressOnButton(zoomIn);
                Thread.sleep(1000);
            }
            for (int j = 0; j < count; j++) {
                pressOnButton(zoomOut);
                Thread.sleep(1000);
            }
        }
    }

    private void typeTextIntoField(String itemParam, String text, int sleep) throws Throwable {
        typeInputTextIntoField(itemParam, text);
        Thread.sleep(1000);
        pressOnENTER();
        Thread.sleep(sleep);
    }

    private void insertLocation(int n) throws Throwable {
        typeTextIntoField("map_pais", mapLocation[n].country, 1000);
        typeTextIntoField("map_Estado", mapLocation[n].state, 1000);
        typeTextIntoField("map_delegaci", mapLocation[n].delegation, 3000);
        typeTextIntoField("map_localidad", mapLocation[n].location, 3000);
    }

    private BufferedReader openLocationFile(String fileName) {
        File file = new File(System.getProperty("user.home") + "\\" + fileName);
        String FILENAME = file.getAbsolutePath();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return br;
    }

    private void readLocationFile() {
        BufferedReader br = openLocationFile("mapLocations.txt");
        for (int i = 0; i < 10; i++) {
            mapLocation[i] = new MapLocation();
            try {
                mapLocation[i].country = br.readLine();
                mapLocation[i].state = br.readLine();
                mapLocation[i].delegation = br.readLine();
                mapLocation[i].location = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @When("^Type wsID into \"(.*)\"$")
    public void insertWsID(String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
        WebElement element = driver.findElement(By.xpath(itemName));
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.click();
        actions.sendKeys(wsID);
        actions.build().perform();
    }

    @When("^type the text \"(.*)\" into \"(.*)\"$")
    public void typeTextIntoField(String inputText, String itemNameParam) throws Throwable {
        String map = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(map)));
        WebElement element = driver.findElement(By.xpath(map));
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.click();
        actions.sendKeys(inputText);
        actions.build().perform();
    }

    @And("^remove all searches \"(.*)\" \"(.*)\" \"(.*)\"$")
    public void removeAllSearches(String searchItem, String clearSearch, String yesButton) throws Throwable {
        String itemName = GetMapping(searchItem);
        while (true) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(itemName)));
            } catch (TimeoutException e) {
                // no more search items then return with no exception
                return;
            }
            pressOnButton(searchItem);
            Thread.sleep(1000);
            pressOnButton(clearSearch);
            Thread.sleep(1000);
            pressOnButton(yesButton);
            Thread.sleep(1000);
        }
    }


    ////////////////////////////////Gidon End/////////////////////////////////

    @Then("^check if the value \"([^\"]*)\" equal to the value in the field \"([^\"]*)\"$")
    public void checkIfTheValueEqualToTheValueInTheField(String value, String itemNameParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        //String listName = commonMethods.parseItemFromObjectRepository(objectRepository, listNameParam);
        try {
            String actualMapValue = driver.findElement(By.xpath(itemName)).getAttribute("value");
            assertThat("The actual map coordinate value is :" + actualMapValue, value, containsString(actualMapValue));
            Assert.assertTrue("The field not containing the expected value", value.contains(actualMapValue));
        } catch (NoSuchElementException e) {
            System.out.println("No element exist");

        }
    }


    @Then("^check if save massage is appear \"([^\"]*)\" if so save it \"([^\"]*)\"$")
    public void checkIfSaveMassageIsAppearIfSoSaveIt(String windowParam, String okButton) throws Throwable {


        try {
            String windowParamDriver = GetMapping(windowParam);
            String okButtonSave = GetMapping(okButton);
            driver.findElement(By.xpath(windowParamDriver)).isDisplayed();
            Thread.sleep(3000);
            driver.findElement(By.xpath(okButtonSave)).click();

        } catch (Exception e) {
            System.out.println("the form is saved ");
        }
    }

    public void elasticAPI() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "wscns")
                .put("network.host", "10.32.5.28")
//                .put("client.transport.sniff", true)
                .build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new
                        InetSocketTransportAddress(InetAddress.getByName(System.getenv("SANITY_IP")), 9300));

        TermQueryBuilder user5Query = QueryBuilders.termQuery("created.userId", "user5");

        SearchResponse searchResponse = client.prepareSearch("case_index").setTypes("case").setQuery(user5Query).get();
        int failedShards = searchResponse.getFailedShards();
        long totalHitsCount = searchResponse.getHits().totalHits();
        SearchHit[] hits;
        hits = searchResponse.getHits().getHits();
        String singleFoundDocument = hits[0].getSourceAsString();

    }

    public int postRequest() {
        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

        try {

            HttpPost request = new HttpPost("http://yoururl");
            StringEntity params = new StringEntity("details={\"name\":\"myname\",\"age\":\"20\"} ");
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            //handle response here...

        } catch (Exception ex) {

            //handle exception here

        } finally {
            //Deprecated
            //httpClient.getConnectionManager().shutdown();
        }
        return 0;
    }

    @And("^assert the user data name \"([^\"]*)\" last name \"([^\"]*)\" gender \"([^\"]*)\" direction \"([^\"]*)\" state \"([^\"]*)\" nationality \"([^\"]*)\" at grid \"([^\"]*)\" with person count \"([^\"]*)\"$")
    public void assertTheUserDataNameLastNameGenderDirectionStateNationalityAtGrid(String name, String lastName, String gender, String direction, String state, String nationality, String itemNameParam, String personCountParam) throws Throwable {
        String itemName = GetMapping(itemNameParam);
        String presonCountMap = GetMapping(personCountParam);
        String maleSpan = "search_male";
        maleSpan = GetMapping(maleSpan);
        String femaleSpan = "search_female";
        femaleSpan = GetMapping(femaleSpan);
        String[] personDataArray = new String[0];
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
            personDataArray = driver.findElement(By.xpath(itemName)).getText().split("\n");
            Assert.assertEquals("the data is not equal", name + " " + lastName, personDataArray[1]);
            Assert.assertEquals("the data is not equal", "Género: " + gender, personDataArray[2]);
            Assert.assertEquals("the data is not equal", "Ubicación: " + state + ", " + direction, personDataArray[4]);
            Assert.assertEquals("the data is not equal", "Nacionalidad: " + nationality, personDataArray[5]);
            if (name.equals("#W52R3TaERa")) {
                driver.findElement(By.xpath(presonCountMap)).click();
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(maleSpan)));
                    scenario.write("found person gender: " + driver.findElement(By.xpath(maleSpan)).getText() + " with name " + name + " :\n");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(maleSpan)));
                    scenario.write("found person gender: " + driver.findElement(By.xpath(femaleSpan)).getText() + " with name " + name + " :\n");
                } catch (Exception e) {
                    scenario.write("cant locate the result grid :\n" + itemName);
                    scenario.write("check if the element is in the DB :\n" + itemName);
                    scenario.isFailed();
                }


            }
        } catch (Exception e) {
            scenario.write("cant locate the result grid :\n" + itemName);
            scenario.write("check if the element is in the DB :\n" + itemName);
            scenario.isFailed();
        }


    }

    @And("^open another chrome window and navigate to \"([^\"]*)\" on browser$")
    public void openAnotherChromeWindowAndNavigateToOnBrowser(String url) throws Throwable {
        String userName = GetMapping("login_userNameField");
        String password = GetMapping("login_passwordField");
        String loginButton = GetMapping("login_startSessionButton");

        Set<String> windows = driver.getWindowHandles();
        String user5ChatWindow = driver.getWindowHandle();
        ((JavascriptExecutor) driver).executeScript("window.open();");
        Set<String> user6ChatWindow = driver.getWindowHandles();
        user6ChatWindow.removeAll(windows);
        String customerSiteHandle = ((String) user6ChatWindow.toArray()[0]);
        driver.switchTo().window(customerSiteHandle);

        ///////////////////////////LOGIN with user6///////////////////////////////
        driver.get(url);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(userName)));
            driver.findElement(By.xpath(userName)).sendKeys("wstu4");
            driver.findElement(By.xpath(password)).sendKeys("Pass123");
            driver.findElement(By.xpath(loginButton)).click();
        } catch (Exception e) {
            ////////////////////Log Out//////////////////////////
            String logOutSpan = GetMapping("common_logOutSpan");
            String logOutButton = GetMapping("common_logOutButton");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(logOutSpan)));
            driver.findElement(By.xpath(logOutSpan)).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(logOutButton)));
            driver.findElement(By.xpath(logOutButton)).click();
            ////////////////////Log Out//////////////////////////
            ////////////////////Log In//////////////////////////
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(userName)));
            driver.findElement(By.xpath(userName)).sendKeys("wstu5");
            driver.findElement(By.xpath(password)).sendKeys("Pass123");
            driver.findElement(By.xpath(loginButton)).click();
            ////////////////////Log In//////////////////////////
        }

        //////////////////////////Go back to user 5
        driver.switchTo().window(user5ChatWindow);
        //  ((JavascriptExecutor) driver).executeScript("window.open(arguments[0])", url);
    }


    @When("^type into \"([^\"]*)\" the text \"([^\"]*)\" on second window$")
    public void typeIntoTheTextOnSecondWindow(String itemNameParam, String inputText) throws Throwable {

    }

    @Then("^click on button by the image \"([^\"]*)\"$")
    public void clickOnButtonByTheImage(String imagePath) throws Throwable {
        Screen s = new Screen();
        s.find(imagePath);
        s.click(imagePath);
        System.out.println("button clicked");
    }

    public void clickOnButtonByTheImageThread(String imagePath) throws Throwable {
        Screen s = new Screen();
        File file = new File(imagePath);
        String closeErrorButton = file.getAbsolutePath();
        try {
            if (s.exists(new Pattern(imagePath).exact()) != null) {
                writeErrorToReport("ERROR Message Has shown On the Screen see screenshot");
                s.click(closeErrorButton);
                Thread.sleep(3000);
                robot.keyPress(KeyEvent.VK_ESCAPE);
                robot.keyRelease(KeyEvent.VK_ESCAPE);

            }
        } catch (ElementNotFoundException e) {
            throw new AssertionError("no error message", e);
        }
    }

    @Then("^check system version$")
    public void checkSystemVersion() throws Throwable {
        Screen s = new Screen();
        File file = new File("src/test/resources/ImageRepository/version/hoverVer.PNG");
        String pathToHover = file.getAbsolutePath();
        s.find(pathToHover);
        s.hover(pathToHover);
        try {
            File fileVersion = new File("src/test/resources/ImageRepository/version/versionImage.png");
            String pathToVersion = fileVersion.getAbsolutePath();
            //  s.wait(pathToVersion).exact();
            s.wait(new Pattern(pathToVersion).exact());
            scenario.write("system version find and o.k");
        } catch (Exception e) {
            scenario.write("system version is not o.k");
            scenario.isFailed();
        }
        Thread.sleep(5000);
    }

    @Then("^check system version from \"([^\"]*)\"$")
    public void checkSystemVersionFrom(String itemNameParam) throws Throwable {
        String versionNumberXpath = GetMapping(itemNameParam);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(versionNumberXpath)));

        WebElement element = driver.findElement(By.xpath(versionNumberXpath));
        Actions action = new Actions(driver);
        action.moveToElement(element).perform();

        String versionHover = GetMapping("common_versionNumber");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(versionHover)));
        //  String versionNumber = driver.findElement(By.xpath(versionHover));
        // System.out.println(versionNumber);
    }

    @Then("^test \"([^\"]*)\"$")
    public void test(String userName) throws Throwable {
        for (int i = 0; i < uniqueEntitiesName.length; i++) {
            System.out.println(uniqueEntitiesName[i]);
        }

    }

    @Then("^change frame to chat$")
    public void changeFrameToChat() throws Throwable {
        driver.switchTo().defaultContent();
        driver.switchTo().frame(0);
    }

    @Then("^change frame to html$")
    public void changeFrameToHtml() throws Throwable {
        driver.switchTo().defaultContent();
    }

    @And("^open firefox browser$")
    public void openFirefoxBrowser() throws Throwable {

    }

//    @And("^open remote browser to chat$")
//    public void openRemoteBrowserToChat() throws Throwable {
//
//        RemoteDriver.manage().window().maximize();
//        RemoteDriver.get("https://10.32.5.73/ui/");
//        String userName = GetMapping("login_userNameField");
//        String password = GetMapping("login_passwordField");
//        String loginButton = GetMapping("login_startSessionButton");
////        WaitRemoteDriver.until(ExpectedConditions.presenceOfElementLocated(By.xpath(userName)));
//        RemoteDriver.findElement(By.xpath(userName)).sendKeys("wstu12");
//        RemoteDriver.findElement(By.xpath(password)).sendKeys("Pass123");
//        RemoteDriver.findElement(By.xpath(loginButton)).click();
//        scenario.write("login in to remote servert to open chat with : user12 in  https://10.32.5.73/ui/");
//
//
//    }

    @And("^on remote read chat message \"([^\"]*)\"$")
    public void readChatMessage(String message) throws Throwable {

    }

//    @And("^on remote press on button \"([^\"]*)\"$")
//    public void onRemoteClickOnButton(String itemNameParam) throws Throwable {
//        String itemName = GetMapping(itemNameParam);
//        try {
//            WaitRemoteDriver.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemName)));
//            RemoteDriver.findElement(By.xpath(itemName)).click();
//        } catch (Exception e) {
//
//
//            Helpi.makeDir(scenario.getName());
//            String CurrentDateDay = dateFormatDay.format(date);
//            String CurrentDateHour = dateFormatHour.format(date);
//            String ScenarioName = scenario.getName();
//            String fileLoaction = "target\\err\\" + CurrentDateDay + "\\" + ScenarioName + "\\" + CurrentDateHour + ".png";
//            File scrFile = ((TakesScreenshot) RemoteDriver).getScreenshotAs(OutputType.FILE);
//            System.out.println("save the image");
//            FileUtils.copyFile(scrFile, new File(fileLoaction));
//            System.out.println("save the image" + fileLoaction);
//            scenario.embed(((TakesScreenshot) RemoteDriver).getScreenshotAs(OutputType.BYTES), "image/png");
//            scenario.write("cant locate the button :" + itemName + "The test fail and images save in \n" + fileLoaction);
//        }
//    }


//    @And("^on remote type into \"([^\"]*)\" the text \"([^\"]*)\"$")
//    public void onRemoteTypeIntoTheText(String itemNameParam, String inputText) throws Throwable {
//        String map = GetMapping(itemNameParam);
//        WaitRemoteDriver.until(ExpectedConditions.presenceOfElementLocated(By.xpath(map)));
//        RemoteDriver.findElement(By.xpath(map)).sendKeys(inputText);
//        scenario.write("insert the data :" + inputText + " on remote ");
//    }
//
//    @And("^on remote type private chat name in \"([^\"]*)\"$")
//    public void privateChat(String itemNameParam) throws Throwable {
//        String map = GetMapping(itemNameParam);
//
////        WaitRemoteDriver.until(ExpectedConditions.presenceOfElementLocated(By.xpath(map)));
//        RemoteDriver.findElement(By.xpath(map)).sendKeys(chatName);
//        scenario.write("insert the data :" + chatName + " on remote ");
//    }
//
//    @Then("^on remote change frame to chat$")
//    public void onRemoteChangeFrameToChat() throws Throwable {
//        try {
//            RemoteDriver.switchTo().frame(0);
//            scenario.write("change I frame");
//        } catch (Exception e) {
//            RemoteDriver.switchTo().defaultContent();
//            RemoteDriver.switchTo().frame(0);
//        }
//
//    }


    @Then("^check private message from remote$")
    public void checkPrivateMessageFromRemote() throws Throwable {
        String privateChat = "//*[text()='" + chatName + "']";
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(privateChat)));
        driver.findElement(By.xpath(privateChat)).click();

    }

    /////////////////////////////////- Load selenium Functions -/////////////////////////////////
    @When("^type to entities \"(.*)\" unique name into field \"(.*)\"$")
    public void insertUniqueNameToEntities(String inputText, String itemNameParam) throws Throwable {
        String map = GetMapping(itemNameParam);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(map)));

        switch (inputText) {
            case "person":
                driver.findElement(By.xpath(map)).sendKeys(uniqueEntitiesName[0]);
                break;
            case "telephone":
                driver.findElement(By.xpath(map)).sendKeys(uniqueEntitiesName[1]);
                break;
            case "vehicle":
                driver.findElement(By.xpath(map)).sendKeys(uniqueEntitiesName[2]);
                break;
            case "property":
                driver.findElement(By.xpath(map)).sendKeys(uniqueEntitiesName[3]);
                break;
            case "organization":
                driver.findElement(By.xpath(map)).sendKeys(uniqueEntitiesName[4]);
                break;
            case "weapon":
                driver.findElement(By.xpath(map)).sendKeys(uniqueEntitiesName[5]);
                break;
            case "case":
                driver.findElement(By.xpath(map)).sendKeys(uniqueEntitiesName[6]);
                break;


        }
    }

    @And("^choose randomly entities to case$")
    public void chooseRandomlyEntitiesToCase() throws Throwable {
        int idx = new Random().nextInt(uniqueEntitiesName.length - 1);
        String entity;
        String linkEntity = GetMapping("common_LinkEntity");
        switch (idx) {
            case 0:
                entity = GetMapping("addentityDropdown_addEntityPersonButton");
                driver.findElement(By.xpath(entity)).click();
                driver.findElement(By.xpath(linkEntity)).sendKeys(uniqueEntitiesName[0]);
                System.out.println("choose randomly person " + idx);
                break;
            case 1:
                entity = GetMapping("addentityDropdown_addEntityTelephoneButton");
                driver.findElement(By.xpath(entity)).click();
                driver.findElement(By.xpath(linkEntity)).sendKeys(uniqueEntitiesName[1]);
                System.out.println("choose randomly Telephone " + idx);
                break;
            case 2:
                entity = GetMapping("addentityDropdown_addEntityVehicleButton");
                driver.findElement(By.xpath(entity)).click();
                driver.findElement(By.xpath(linkEntity)).sendKeys(uniqueEntitiesName[2]);
                System.out.println("choose randomly Vehicle " + idx);
                break;
            case 3:
                entity = GetMapping("addentityDropdown_addEntityActiveButton");
                driver.findElement(By.xpath(entity)).click();
                driver.findElement(By.xpath(linkEntity)).sendKeys(uniqueEntitiesName[3]);
                System.out.println("choose randomly Property " + idx);
                break;
            case 4:
                entity = GetMapping("addentityDropdown_addEntityOrgButton");
                driver.findElement(By.xpath(entity)).click();
                driver.findElement(By.xpath(linkEntity)).sendKeys(uniqueEntitiesName[4]);
                System.out.println("choose randomly Org " + idx);
                break;
            case 5:
                entity = GetMapping("addentityDropdown_addEntityWeaponButton");
                driver.findElement(By.xpath(entity)).click();
                driver.findElement(By.xpath(linkEntity)).sendKeys(uniqueEntitiesName[5]);
                System.out.println("choose randomly Weapon " + idx);
                break;
        }
    }

    @And("^choose randomly entities$")
    public void chooseRandomlyEntities() throws Throwable {
        int idx = new Random().nextInt(uniqueEntitiesName.length - 1);
        String entity;
        String linkEntity = GetMapping("common_LinkEntity");
        switch (idx) {
            case 0:
                entity = GetMapping("addentityDropdown_addEntityPersonButton");
                driver.findElement(By.xpath(entity)).click();
                System.out.println("choose randomly person " + idx);
                break;
            case 1:
                entity = GetMapping("addentityDropdown_addEntityTelephoneButton");
                driver.findElement(By.xpath(entity)).click();
                System.out.println("choose randomly Telephone " + idx);
                break;
            case 2:
                entity = GetMapping("addentityDropdown_addEntityVehicleButton");
                driver.findElement(By.xpath(entity)).click();
                System.out.println("choose randomly Vehicle " + idx);
                break;
            case 3:
                entity = GetMapping("addentityDropdown_addEntityActiveButton");
                driver.findElement(By.xpath(entity)).click();
                System.out.println("choose randomly Property " + idx);
                break;
            case 4:
                entity = GetMapping("addentityDropdown_addEntityOrgButton");
                driver.findElement(By.xpath(entity)).click();
                System.out.println("choose randomly Org " + idx);
                break;
            case 5:
                entity = GetMapping("addentityDropdown_addEntityWeaponButton");
                driver.findElement(By.xpath(entity)).click();
                System.out.println("choose randomly Weapon " + idx);
                break;
        }
    }

    /*
    create random entity
     */
    public void createPerson(String entityName) throws Throwable {
        typeInputTextIntoField("person_FirstNameField", entityName);
        threadSleep(2);
//        pressOnButton("person_profileImage");
//        pressOnButton("person_chooseImage");
//        threadSleep(2);
//        uploadImageFrom("src\\main\\resources\\photos\\profileImage.jpg");
        ///assaf////
        pressOnButton("map_AddDirection");
        insertRandomAddress();
        threadSleep(2);
        ///assaf///
        pressOnButton("common_saveButton");
        threadSleep(2);
    }

    public void createTelephone(String entityName) throws Throwable {
        typeInputTextIntoField("Telephone_telephoneNumber", entityName);
        threadSleep(2);
        pressOnButton("common_saveButton");
        threadSleep(2);
    }

    public void createVehicle(String entityName) throws Throwable {
        typeInputTextIntoField("vehicle_Registration", entityName);
        typeInputTextIntoField("vehicle_Tipo", "Autobus");
        pressOnENTER();
        threadSleep(2);
        pressOnButton("common_saveButton");
        threadSleep(2);
    }

    public void createProperty(String entityName) throws Throwable {
        typeInputTextIntoField("Activo_Nombre", entityName);
        typeInputTextIntoField("Activo_Tipo", "Casa");
        threadSleep(2);
        pressOnENTER();
        threadSleep(2);
        ////assaf////
        threadSleep(2);
        insertRandomAddress();
        ////assaf////
        pressOnButton("common_saveButton");
        threadSleep(2);
    }

    public void createOrganization(String entityName) throws Throwable {
        typeInputTextIntoField("organization_Registration", entityName);
        typeInputTextIntoField("organization_Tipo", "Banco");
        threadSleep(2);
        pressOnENTER();
        threadSleep(2);
        ///assaf////
        pressOnButton("map_AddDirection");
        insertRandomAddress();
        threadSleep(2);
        ///assaf///
        pressOnButton("common_saveButton");
        threadSleep(2);
    }

    public void createWeapon(String entityName) throws Throwable {
        typeInputTextIntoField("Weapon_weaponSeries", entityName);
        typeInputTextIntoField("Weapon_weaponType", "Carabina");

        threadSleep(1);
        pressOnENTER();
        threadSleep(1);
        typeInputTextIntoField("Weapon_weaponMark", "Adi");
        threadSleep(1);
        pressOnENTER();
        threadSleep(10);
        pressOnButton("common_saveButton");

    }

    @When("^Hover on Element \"([^\"]*)\" and add \"([^\"]*)\" random entity$")
    public void hoverOnElementAndAddRandomEntity(String itemNameParam, int numberOfEntitiesToCreate) throws Throwable {
        int idx = 0;
        int personCounter = 0;
        int TelephoneCounter = 0;
        int VehicleCounter = 0;
        int PropertyCounter = 0;
        int OrgCounter = 0;
        int WeaponCounter = 0;
        ArrayList<String> personList = new ArrayList<String>();
        ArrayList<String> TelephoneList = new ArrayList<String>();
        ArrayList<String> VehicleList = new ArrayList<String>();
        ArrayList<String> PropertyList = new ArrayList<String>();
        ArrayList<String> OrgList = new ArrayList<String>();
        ArrayList<String> WeaponList = new ArrayList<String>();
        WebElement subElement;
        Entities entityCreator = new Entities();
        String entity;

        for (int i = 0; i < numberOfEntitiesToCreate; i++) {
            idx = new Random().nextInt(uniqueEntitiesName.length - 1);
            pressOnButton("topbar_addButton");
            String itemName = GetMapping(itemNameParam);
            WebElement element = driver.findElement(By.xpath(itemName));
            Actions action = new Actions(driver);
            action.moveToElement(element).perform();
            switch (idx) {
                case 0:
                    entity = GetMapping("addentityDropdown_addEntityPersonButton");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(entity)));
                    subElement = driver.findElement(By.xpath(entity));
                    action.moveToElement(subElement);
                    subElement.click();
                    System.out.println("choose randomly person " + idx);
                    if (personCounter > listOLists.get(0).size()) {
                        listOLists.get(0).add(randomString.nextString());
                    }
                    createPerson(listOLists.get(0).get(personCounter));
                    personList.add(listOLists.get(0).get(personCounter));
                    personCounter++;
                    break;
                case 1:
                    entity = GetMapping("addentityDropdown_addEntityTelephoneButton");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(entity)));
                    subElement = driver.findElement(By.xpath(entity));
                    action.moveToElement(subElement);
                    subElement.click();
                    System.out.println("choose randomly Telephone " + idx);
                    if (TelephoneCounter > listOLists.get(1).size()) {
                        listOLists.get(1).add(randomString.nextString());
                    }

                    createTelephone(listOLists.get(1).get(TelephoneCounter));
                    TelephoneList.add(listOLists.get(1).get(TelephoneCounter));
                    TelephoneCounter++;
                    break;
                case 2:
                    entity = GetMapping("addentityDropdown_addEntityVehicleButton");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(entity)));
                    subElement = driver.findElement(By.xpath(entity));
                    action.moveToElement(subElement);
                    subElement.click();
                    System.out.println("choose randomly Vehicle " + idx);
                    if (VehicleCounter > listOLists.get(2).size()) {
                        listOLists.get(2).add(randomString.nextString());
                    }

                    createVehicle(listOLists.get(2).get(VehicleCounter));
                    VehicleList.add(listOLists.get(2).get(VehicleCounter));
                    VehicleCounter++;
                    break;
                case 3:
                    entity = GetMapping("addentityDropdown_addEntityActiveButton");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(entity)));
                    subElement = driver.findElement(By.xpath(entity));
                    action.moveToElement(subElement);
                    subElement.click();
                    System.out.println("choose randomly Property " + idx);
                    if (PropertyCounter > listOLists.get(3).size()) {
                        listOLists.get(3).add(randomString.nextString());
                    }
                    createProperty(listOLists.get(3).get(PropertyCounter));
                    PropertyList.add(listOLists.get(3).get(PropertyCounter));
                    PropertyCounter++;
                    break;
                case 4:
                    entity = GetMapping("addentityDropdown_addEntityOrgButton");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(entity)));
                    subElement = driver.findElement(By.xpath(entity));
                    action.moveToElement(subElement);
                    subElement.click();
                    System.out.println("choose randomly Org " + idx);
                    if (OrgCounter > listOLists.get(4).size()) {
                        listOLists.get(4).add(randomString.nextString());
                    }
                    createOrganization(listOLists.get(4).get(OrgCounter));
                    OrgList.add(listOLists.get(4).get(OrgCounter));
                    OrgCounter++;
                    break;
                case 5:
                    entity = GetMapping("addentityDropdown_addEntityWeaponButton");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(entity)));
                    subElement = driver.findElement(By.xpath(entity));
                    action.moveToElement(subElement);
                    subElement.click();

                    System.out.println("choose randomly Weapon " + idx);
                    if (WeaponCounter > listOLists.get(5).size()) {
                        listOLists.get(5).add(randomString.nextString());
                    }
                    createWeapon(listOLists.get(5).get(WeaponCounter));
                    WeaponList.add(listOLists.get(5).get(WeaponCounter));
                    WeaponCounter++;
                    break;
            }
        }

        listOfEntities.add(0, personList);

        listOfEntities.add(1, TelephoneList);

        listOfEntities.add(2, VehicleList);

        listOfEntities.add(3, PropertyList);

        listOfEntities.add(4, OrgList);

        listOfEntities.add(5, WeaponList);

    }


    @And("^choose entities$")
    public void chooseEntities() throws Throwable {
        int totalEntities = 0;
        String firstResultItem = "Common_LinkFirstResultSearch";
        String entity;
        String linkEntity = GetMapping("common_LinkEntity");
        String acceptLink = "case_caseAcceptLink";
        String LinkEntitiesButton = "case_caseLinkEntitiesButton";

        for (int i = 0; i < listOfEntities.size(); i++) {
            for (int j = 0; j < listOfEntities.get(i).size(); j++) {
                if (i == 0) {
                    pressOnButton(LinkEntitiesButton);
                    entity = GetMapping("addentityDropdown_addEntityPersonButton");

                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(entity)));
                    driver.findElement(By.xpath(entity)).click();
                    driver.findElement(By.xpath(linkEntity)).sendKeys(listOfEntities.get(0).get(j));

                    pressOnENTER();
                    try {
                        pressOnButton(firstResultItem);
                        pressOnButton(acceptLink);
                        System.out.println("choose randomly person ");
                        totalEntities++;
                    } catch (Exception e) {
                        scenario.write("cant find entity person:" + listOfEntities.get(0).get(j));
                        pressOnButton("common_closeObservation");
                    }

                }
                if (i == 1) {
                    pressOnButton(LinkEntitiesButton);
                    entity = GetMapping("addentityDropdown_addEntityTelephoneButton");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(entity)));

                    driver.findElement(By.xpath(entity)).click();
                    driver.findElement(By.xpath(linkEntity)).sendKeys(listOfEntities.get(1).get(j));

                    pressOnENTER();
                    try {
                        pressOnButton(firstResultItem);
                        pressOnButton(acceptLink);
                        System.out.println("choose randomly Telephone ");
                        totalEntities++;
                    } catch (Exception e) {
                        scenario.write("cant find entity Telephone:" + listOfEntities.get(1).get(j));
                        pressOnButton("common_closeObservation");
                    }

                }

                if (i == 2) {
                    pressOnButton(LinkEntitiesButton);
                    entity = GetMapping("addentityDropdown_addEntityVehicleButton");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(entity)));

                    driver.findElement(By.xpath(entity)).click();
                    driver.findElement(By.xpath(linkEntity)).sendKeys(listOfEntities.get(2).get(j));


                    pressOnENTER();
                    try {
                        pressOnButton(firstResultItem);
                        pressOnButton(acceptLink);
                        System.out.println("choose randomly Vehicle ");
                        totalEntities++;
                    } catch (Exception e) {
                        scenario.write("cant find entity Vehicle:" + listOfEntities.get(2).get(j));
                        pressOnButton("common_closeObservation");
                    }

                }
                if (i == 3) {
                    pressOnButton(LinkEntitiesButton);
                    entity = GetMapping("addentityDropdown_addEntityActiveButton");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(entity)));

                    driver.findElement(By.xpath(entity)).click();
                    driver.findElement(By.xpath(linkEntity)).sendKeys(listOfEntities.get(3).get(j));

                    pressOnENTER();
                    try {
                        pressOnButton(firstResultItem);
                        pressOnButton(acceptLink);
                        System.out.println("choose randomly Property ");
                        totalEntities++;
                    } catch (Exception e) {
                        scenario.write("cant find entity Property:" + listOfEntities.get(3).get(j));
                        pressOnButton("common_closeObservation");
                    }

                }
                if (i == 4) {
                    pressOnButton(LinkEntitiesButton);
                    entity = GetMapping("addentityDropdown_addEntityOrgButton");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(entity)));

                    driver.findElement(By.xpath(entity)).click();
                    driver.findElement(By.xpath(linkEntity)).sendKeys(listOfEntities.get(4).get(j));

                    pressOnENTER();
                    try {
                        pressOnButton(firstResultItem);
                        pressOnButton(acceptLink);
                        System.out.println("choose randomly Org ");
                        totalEntities++;
                    } catch (Exception e) {
                        scenario.write("cant find entity Org:" + listOfEntities.get(4).get(j));
                        pressOnButton("common_closeObservation");
                    }

                }

                if (i == 5) {
                    pressOnButton(LinkEntitiesButton);
                    entity = GetMapping("addentityDropdown_addEntityWeaponButton");
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(entity)));

                    driver.findElement(By.xpath(entity)).click();
                    driver.findElement(By.xpath(linkEntity)).sendKeys(listOfEntities.get(5).get(j));

                    pressOnENTER();
                    try {
                        pressOnButton(firstResultItem);
                        pressOnButton(acceptLink);
                        System.out.println("choose randomly Weapon ");
                        totalEntities++;
                    } catch (Exception e) {
                        scenario.write("cant find entity Weapon:" + listOfEntities.get(5).get(j));
                        pressOnButton("common_closeObservation");
                    }

                }
            }
        }

        linkEvidenceToEntity(totalEntities);
        entittToLegal = totalEntities;

    }

    public void linkEvidenceToEntity(int numberOfEntity) throws Throwable {
        for (int i = 1; i <= numberOfEntity; i++) {
            threadSleep(2);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//DIV[@draggable='true'])[" + i + "]//SPAN/span/span")));
            driver.findElement(By.xpath("(//DIV[@draggable='true'])[" + i + "]//SPAN/span/span")).click();
            threadSleep(1);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//SPAN[text()='Marcar como evidencia ']")));
            driver.findElement(By.xpath("//SPAN[text()='Marcar como evidencia ']")).click();
        }
    }

    @Then("^in load test check number of evidence in legal \"([^\"]*)\"$")
    public void inLoadTestCheckNumberOfEvidenceInLegal(String itemNameParam) throws Throwable {
        threadSleep(2);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='root']/div/div[3]/div/div/div/div[2]/div/div[2]/div/div/div[2]/div[2]/div/div")));
        String itemName = GetMapping(itemNameParam);
        WebElement body = driver.findElement(By.xpath(itemName));
        String bodyText = body.getText();
        int counter = 0;

        while (bodyText.contains("EVIDENCIA")) {

            // when match is found, increment the count
            counter++;

            // continue searching from where you left off
            bodyText = bodyText.substring(bodyText.indexOf("EVIDENCIA") + "EVIDENCIA".length());
        }
        Assert.assertEquals("we expect Evidence is number to be : " + entittToLegal + "but we got: " + counter, counter, entittToLegal);

    }

    @When("^type username from file to \"([^\"]*)\"$")
    public void typeUsernameFromFileTo(String itemNameParam) throws Throwable {

        Help_Functions funci = new Help_Functions();
        userNameFromFile = funci.ReadFile();
        typeInputTextIntoField(itemNameParam, userNameFromFile);
    }

    @And("^accept chrome alert$")
    public void acceptChromeAlert() {
        try {
            scenario.write("check if multi file message appears");
            driver.switchTo().alert();

            driver.switchTo().alert().accept();
            scenario.write("message appears and accepted");

        } catch (NoAlertPresentException Ex) {
            scenario.write("chrome alert  message not appear");
        }
    }

    @And("^press on tab button$")
    public void pressOnTabButton() throws Throwable {
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
    }

    @And("^Assert element by text \"([^\"]*)\"$")
    public void assertElementByText(String elementText) throws Throwable {
        if (driver.findElement(By.xpath("//*[text()='" + elementText + "']")).isDisplayed()) {
            scenario.write("The element with the text: " + elementText + " found");
        } else {
            writeErrorToReport("The element with the text: " + elementText + "NOT found");
        }
    }

    @Then("^logout$")
    public void logout() throws Throwable {

        String logoutButton = GetMapping("common_logOutButton");
        String logoutSpan = GetMapping("common_logOutSpan");
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(logoutSpan)));
            WebElement logoutSpanElement = driver.findElement(By.xpath(logoutSpan));
            logoutSpanElement.click();

            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(logoutButton)));
            WebElement logoutButtonElement = driver.findElement(By.xpath(logoutButton));
            logoutButtonElement.click();
            scenario.write("log out the system");
            threadSleep(2);
            cleanCookies();
        } catch (Exception e) {
            writeErrorToReport("cant logout, element not found");
        }
    }

    @Given("^login in to \"([^\"]*)\" with user \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void loginInToWithUserAndPassword(String pathUrl, String username, String password) throws Throwable {
        navigate_to_URL_on_browser(pathUrl);
        typeInputTextIntoField("login_userNameField", username);
        typeInputTextIntoField("login_passwordField", password);
        pressOnButton("login_startSessionButton");
        Assert.assertEquals("The titles are not equals", "Wisdom Stone", driver.getTitle());



    }

    public void paintElement(String itemName) {
        // draw a border around the found element
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid blue'", driver.findElement(By.xpath(itemName)));
        }
    }
}