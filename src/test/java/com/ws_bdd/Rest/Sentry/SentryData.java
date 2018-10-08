package com.ws_bdd.Rest.Sentry;


public class SentryData {
    String culprit ;
    String title;
    String id;
    String level;
    public SentryData(String culprit,String title,String id,String level) {
        this.culprit = culprit;
        this.title = title;
        this.id = id;
        this.level = level;

    }
}
