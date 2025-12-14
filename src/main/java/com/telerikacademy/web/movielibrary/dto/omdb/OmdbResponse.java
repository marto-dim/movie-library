package com.telerikacademy.web.movielibrary.dto.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OmdbResponse {

    @JsonProperty("imdbRating")
    private String imdbRating;

    @JsonProperty("Response")
    private String response;

    public String getImdbRating() {
        return imdbRating;
    }

    public String getResponse() {
        return response;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public boolean isSuccessful() {
        return "True".equalsIgnoreCase(response);
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

