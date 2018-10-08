package com.ws_bdd.Rest.Cnac;

import com.fasterxml.jackson.databind.JsonNode;


public class cnacJsonParser {

    public cnacData getErrorsMessage(JsonNode jsonNode) {
        String wsID = jsonNode.get(0).get("wsId").textValue();
        cnacData data = new cnacData(jsonNode.get(0).get("wsId").textValue());
        data.setCnacID(wsID);
        return data;

    }

    public String getCancId(JsonNode jsonNode) {
        String wsID;
        return wsID = jsonNode.get("wsId").textValue();


    }
}
