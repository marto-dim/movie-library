package com.telerikacademy.web.movielibrary.service.movie;

import com.telerikacademy.web.movielibrary.model.Movie;

import java.util.List;

public interface MovieService {

    Movie create(Movie movie);

    List<Movie> getAll();

    Movie getById(Long id);

    Movie update(Long id, Movie movie);

    void delete(Long id);

}
