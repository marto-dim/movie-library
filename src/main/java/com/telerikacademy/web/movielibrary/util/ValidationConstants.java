package com.telerikacademy.web.movielibrary.util;

public class ValidationConstants {
    private ValidationConstants() {
    }

    public static final int MIN_MOVIE_YEAR = 1888;

    public static final String TITLE_BLANK_MESSAGE =
            "Title must not be blank";

    public static final String RELEASE_YEAR_MIN_MESSAGE =
            "Release year must be >= " + MIN_MOVIE_YEAR;
}
