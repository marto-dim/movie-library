package com.telerikacademy.web.movielibrary.mapper.movie;

import com.telerikacademy.web.movielibrary.dto.movie.MovieCreateDto;
import com.telerikacademy.web.movielibrary.dto.movie.MovieResponseDto;
import com.telerikacademy.web.movielibrary.dto.movie.MovieUpdateDto;
import com.telerikacademy.web.movielibrary.model.Movie;

public class MovieMapper {

    public static Movie toEntity(MovieCreateDto dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setDirector(dto.getDirector());
        movie.setReleaseYear(dto.getReleaseYear());
        return movie;
    }

    public static Movie toEntity(MovieUpdateDto dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setDirector(dto.getDirector());
        movie.setReleaseYear(dto.getReleaseYear());
        return movie;
    }

    public static MovieResponseDto toResponse(Movie movie) {
        MovieResponseDto dto = new MovieResponseDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDirector(movie.getDirector());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setRating(movie.getRating());
        return dto;
    }
}

