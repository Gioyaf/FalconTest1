package com.ws_bdd.CaseInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Case implements Serializable {

    public Case() {
        this.caseInfo = new CaseInfo();
        this.observation = new Observation();
        this.report = new Report();
    }

    public Report report;
    public CaseInfo caseInfo;
    public Observation observation;
}
