package ru.boyda.popov.searchcinemas.parser.geo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {

    @JsonProperty("place_id")
    private String placeId;

    public String getPlaceId() {
        return placeId;
    }

}
