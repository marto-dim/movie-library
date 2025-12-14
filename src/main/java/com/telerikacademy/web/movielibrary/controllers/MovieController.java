package com.telerikacademy.web.movielibrary.controllers;


import com.telerikacademy.web.movielibrary.dto.movie.MovieCreateDto;
import com.telerikacademy.web.movielibrary.dto.movie.MovieResponseDto;
import com.telerikacademy.web.movielibrary.dto.movie.MovieUpdateDto;
import com.telerikacademy.web.movielibrary.mapper.movie.MovieMapper;
import com.telerikacademy.web.movielibrary.model.Movie;
import com.telerikacademy.web.movielibrary.service.movie.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<MovieResponseDto> create(
            @Valid @RequestBody MovieCreateDto dto) {

        Movie movie = MovieMapper.toEntity(dto);
        Movie created = movieService.create(movie);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MovieMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<MovieResponseDto>> getAll() {
        List<MovieResponseDto> result = movieService.getAll()
                .stream()
                .map(MovieMapper::toResponse)
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> getById(@PathVariable Long id) {
        Movie movie = movieService.getById(id);
        return ResponseEntity.ok(MovieMapper.toResponse(movie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody MovieUpdateDto dto) {

        Movie updated = MovieMapper.toEntity(dto);
        Movie result = movieService.update(id, updated);

        return ResponseEntity.ok(MovieMapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        movieService.delete(id);
    }
}

