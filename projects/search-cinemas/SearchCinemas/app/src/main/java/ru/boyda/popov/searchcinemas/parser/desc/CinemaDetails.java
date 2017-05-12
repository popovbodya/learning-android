package ru.boyda.popov.searchcinemas.parser.desc;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import ru.boyda.popov.searchcinemas.parser.geo.Geometry;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CinemaDetails implements Serializable {

    @JsonProperty("formatted_address")
    private String formattedAddress;

    @JsonProperty("geometry")
    private Geometry geometry;

    @JsonProperty("name")
    private String name;

    @JsonProperty("rating")
    private String rating;

    public String getName() {
        return name;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public String getRating() {
        return rating;
    }
}
