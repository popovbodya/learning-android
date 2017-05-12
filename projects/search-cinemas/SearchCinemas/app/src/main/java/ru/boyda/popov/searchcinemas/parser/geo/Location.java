package ru.boyda.popov.searchcinemas.parser.geo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location implements Serializable {
    @JsonProperty("lng")
    private String lng;

    @JsonProperty("lat")
    private String lat;

    public String getLng() {
        return lng;
    }

    public String getLat() {
        return lat;
    }
}
