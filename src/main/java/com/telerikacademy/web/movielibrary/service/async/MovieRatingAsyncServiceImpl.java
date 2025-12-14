package com.telerikacademy.web.movielibrary.service.async;

import com.telerikacademy.web.movielibrary.model.Movie;
import com.telerikacademy.web.movielibrary.repository.MovieRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieRatingAsyncServiceImpl {

    private final MovieRepository movieRepository;

    public MovieRatingAsyncServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Async
    @Transactional
    public void enrichRating(Long movieId) {
        try {
            // simulate slow external API
            Thread.sleep(10000);

            Movie movie = movieRepository.findById(movieId)
                    .orElseThrow();

            // fake rating for now
            movie.setRating(8.5);

            movieRepository.save(movie);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}