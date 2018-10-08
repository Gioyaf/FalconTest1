package com.ws_bdd.Rest.Sentry;

//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;


import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SentryJsonParser {

    ArrayList<String> sentyDataList = new ArrayList<String>();

    public ArrayList<String> getErrorsMessage(JsonNode jsonNode) {

        int jsonNodeSize = jsonNode.size();
        if (jsonNodeSize > 0) {
            for (int i = 0; i < jsonNodeSize; i++) {

                String issue = jsonNode.get(i).get("metadata").get("title").textValue();
                sentyDataList.add(issue);

            }
            return sentyDataList;
        }
        sentyDataList.add("No errors");
        return sentyDataList;

    }

    public List<SentryData> getErrorsMessageToClass(JsonNode jsonNode) {
        List<SentryData> sentryDataList = new ArrayList<SentryData>();
        int jsonNodeSize = jsonNode.size();
        if (jsonNodeSize > 0) {
            for (int i = 0; i < jsonNodeSize; i++) {
                SentryData sentryData = new SentryData(jsonNode.get(i).get("culprit").textValue(), jsonNode.get(i).get("title").textValue(), jsonNode.get(i).get("id").textValue(), jsonNode.get(i).get("level").textValue());

                sentryDataList.add(sentryData);

            }
            return sentryDataList;
        }
        SentryData sentryData = new SentryData("No errors", "No errors", "No errors", "No errors");
        sentryDataList.add(sentryData);
        return sentryDataList;

    }

    /**
     * get from sentry list of issue id
     *
     * @param jsonNode = return json from request
     * @return list of issue ID
     */

    public ArrayList<String> getIssueId(JsonNode jsonNode) {

        int jsonNodeSize = jsonNode.size();
        if (jsonNodeSize > 0) {
            for (int i = 0; i < jsonNodeSize; i++) {

                String issue = jsonNode.get(i).get("id").textValue();
                sentyDataList.add(issue);

            }
            return sentyDataList;
        }
        sentyDataList.add("No errors");
        return sentyDataList;

    }

    public void getJsonValue(JsonNode jsonNode) {
        Iterator<JsonNode> iterator = jsonNode.iterator();
        Integer size = 0;
        while (iterator.hasNext()) {
            JsonNode next = iterator.next();
            if (next.has("metadata")) {
                size++;
            }
        }
    }
}
