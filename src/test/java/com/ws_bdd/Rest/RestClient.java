package com.ws_bdd.Rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ws_bdd.ConfigProp;
import com.ws_bdd.CucumberToSeleniumMethods;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;

public class RestClient {

    public JsonNode responceJson;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public String Authorization = ConfigProp.GetInstance().getProperty("sentryAuthorization");

    public String SentryPath = ConfigProp.GetInstance().getProperty("SentryPath");

    public String SentryHost = ConfigProp.GetInstance().getProperty("SentryHost");

    static CucumberToSeleniumMethods cucumberToSeleniumMethods;

    static {
        try {
            cucumberToSeleniumMethods = new CucumberToSeleniumMethods();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public RestClient() throws MalformedURLException, AWTException {
    }

    /**
     * @param path       = extra path to the target(baseURl) ex- http://10.32.5.8:9022/api/0/+ path +
     * @param query      = the query ot the rest request ex ?statsPeriod
     * @param queryValue the value of the query ex = 24h
     * @return the json response
     * @throws Exception
     */
    public JsonNode sendRestRequest(String path, String query, String queryValue) throws Exception {
        //build the client
        Client client = ClientBuilder
                .newBuilder()
                .register(JacksonFeature.class)
                .build();
        //build the request
        Response response = client.target(SentryPath)
                .path(path)
                .queryParam(query, queryValue)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + Authorization)
                .header("Host", SentryHost)
                .get();

        responceJson = checkRespoceCode(response);
        return responceJson;
    }

    public JsonNode restPostRequest(String environment) throws IOException {
        if (System.getenv("SANITY_IP")!=null)
        {
            environment = System.getenv("SANITY_IP");
        }
        String body = "{\n" +
                "  \"id\": \"CNAC123457\",\n" +
                "  \"source\": \"CNAC\",\n" +
                "  \"subject\": \"Publish by Automation UI \",\n" +
                "  \"description\": \"Test cnac \",\n" +
                "  \"sourceName\": \"Jose Martinez\",\n" +
                "  \"reliability\": \"c\",\n" +
                "  \"sourceCredibility\": \"3\",\n" +
                "  \"priority\": \"normal\",\n" +
                "  \"crimeType\": \"Amenaza de Bomba;Tr\\u00E1fico de Droga\",\n" +
                "  \"createdTime\": \"2017-06-10 23:12:00\",\n" +
                "  \"locations\": [\n" +
                "    {\n" +
                "      \"timeSpan\": {\n" +
                "        \"startTime\": \"2013-12-10 23:12:00\",\n" +
                "        \"endTime\": \"2015-02-15 07:22:00\"\n" +
                "      },\n" +
                "      \"address\": {\n" +
                "        \"country\": \"México\",\n" +
                "        \"state\": \"México\",\n" +
                "        \"colony\": \"Ixtapaluca\",\n" +
                "        \"street\": \"Ampliación san francisco\",\n" +
                "        \"settlement\": \"Jacarandas\",\n" +
                "        \"postalCode\": \"35190\",\n" +
                "        \"internalNumber\": \"\",\n" +
                "        \"externalNumber\": \"\",\n" +
                "        \"description\": \"Xcaret Park\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"timeSpan\": {\n" +
                "        \"startTime\": \"2013-04-10 23:12:00\",\n" +
                "        \"endTime\": \"2015-08-15 07:22:00\"\n" +
                "      },\n" +
                "      \"address\": {\n" +
                "        \"country\": \"M\\u00E9xico\",\n" +
                "        \"floor\": \"2\",\n" +
                "        \"colony\": \"colony_1\",\n" +
                "        \"street\": \"Calz Cetys\",\n" +
                "        \"district\": \"Estado de m\\u00E9xico\",\n" +
                "        \"settlement\": \"Atlacomulco\",\n" +
                "        \"postalCode\": \"22000\",\n" +
                "        \"internalNumber\": \"2A\",\n" +
                "        \"externalNumber\": \"10\",\n" +
                "        \"description\": \"Hotel Camino\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"orgTarget\": \"divisions_2\"\n" +
                "}";
        Client client = ClientBuilder
                .newBuilder()
                .register(JacksonFeature.class)
                .build();
        //build the request and send it to server
        Response response = client.target("http://"+environment+":12056")
                .path("/CNAC/Create/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "xxx")
                .post(Entity.json(body));

        responceJson = checkRespoceCode(response);
        return responceJson;
    }


    public JsonNode checkRespoceCode(Response response) throws IOException {

        int responseCode = response.getStatus();

        if (responseCode == 200) {
            String responseString = response.readEntity(String.class);
            JsonNode jsonNode = objectMapper.readTree(responseString);
            return jsonNode;
        } else {
            cucumberToSeleniumMethods.writeErrorToReport("Got error response Code from sentry responseCode is :" + responseCode);
        }
        return null;
    }

    public static void main(String[] args) throws IOException, AWTException {
        JsonNode responceJson;
        RestClient restClient = new RestClient();
        responceJson =   restClient.restPostRequest("http://10.32.5.23/ui/ ");
        System.out.println(responceJson.get("wsId"));
    }
}


