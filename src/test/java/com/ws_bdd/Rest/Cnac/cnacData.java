package com.ws_bdd.Rest.Cnac;

public class cnacData {
    String cnacID;
    int responseCode;

    public String getCnacID() {
        return cnacID;
    }

    public void setCnacID(String cnacID) {
        this.cnacID = cnacID;
    }

    public int getResponcCode() {
        return responseCode;
    }

    public void setResponcCode(int responcCode) {
        this.responseCode = responcCode;
    }

    public cnacData(String cnacID) {

        this.cnacID = cnacID;

    }
}
