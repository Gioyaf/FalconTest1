package com.ws_bdd;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.*;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)

@CucumberOptions(
        strict = false,
        features = {"src/test/"},
        tags = "@sanity",
        format = {"pretty"
                , "json:target/cucumber.json"}

)
public class cukesRunnerTest {

}
