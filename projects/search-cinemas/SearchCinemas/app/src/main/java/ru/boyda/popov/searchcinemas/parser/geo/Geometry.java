package ru.boyda.popov.searchcinemas.parser.geo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry implements Serializable {
    @JsonProperty("location")
    private Location location;

    public Location getLocation() {
        return location;
    }
}
