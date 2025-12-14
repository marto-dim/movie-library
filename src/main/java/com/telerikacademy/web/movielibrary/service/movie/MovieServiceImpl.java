package com.telerikacademy.web.movielibrary.service.movie;

import com.telerikacademy.web.movielibrary.model.Movie;
import com.telerikacademy.web.movielibrary.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.telerikacademy.web.movielibrary.util.StringErrorConstants.MOVIE_ID_NOT_FOUND;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie create(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Movie getById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow( () -> new EntityNotFoundException( String.format(MOVIE_ID_NOT_FOUND, id)) );
    }

    @Override
    public Movie update(Long id, Movie updatedMovie) {
        Movie existing = getById(id);

        if ( updatedMovie.getTitle() != null ) {
            existing.setTitle(updatedMovie.getTitle());
        }

        if ( updatedMovie.getDirector() != null ) {
            existing.setDirector(updatedMovie.getDirector());
        }

        if ( updatedMovie.getReleaseYear() != null ) {
            existing.setReleaseYear(updatedMovie.getReleaseYear());
        }

        return movieRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Movie existing = getById(id);
        movieRepository.delete(existing);
    }
}

