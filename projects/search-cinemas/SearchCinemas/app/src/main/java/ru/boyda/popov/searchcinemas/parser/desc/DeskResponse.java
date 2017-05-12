package ru.boyda.popov.searchcinemas.parser.desc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeskResponse {

    @JsonProperty("result")
    private CinemaDetails result;

    @JsonProperty("status")
    private String status;

    public CinemaDetails getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }
}
