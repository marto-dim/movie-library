package com.telerikacademy.web.movielibrary.repository;

import com.telerikacademy.web.movielibrary.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}

