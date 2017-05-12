package ru.boyda.popov.searchcinemas.parser.geo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoResponse {
    @JsonProperty("results")
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

}
