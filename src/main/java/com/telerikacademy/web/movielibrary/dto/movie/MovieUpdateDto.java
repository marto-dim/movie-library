package com.telerikacademy.web.movielibrary.dto.movie;

import jakarta.validation.constraints.Min;

import static com.telerikacademy.web.movielibrary.util.ValidationConstants.MIN_MOVIE_YEAR;
import static com.telerikacademy.web.movielibrary.util.ValidationConstants.RELEASE_YEAR_MIN_MESSAGE;

public class MovieUpdateDto {

    private String title;
    private String director;

    @Min(value = MIN_MOVIE_YEAR, message = RELEASE_YEAR_MIN_MESSAGE)
    private Integer releaseYear;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
}
