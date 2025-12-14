package com.telerikacademy.web.movielibrary.service.async;

import com.telerikacademy.web.movielibrary.client.omdb.OmdbClient;
import com.telerikacademy.web.movielibrary.config.OmdbProperties;
import com.telerikacademy.web.movielibrary.dto.omdb.OmdbResponse;
import com.telerikacademy.web.movielibrary.model.Movie;
import com.telerikacademy.web.movielibrary.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieRatingAsyncServiceImpl {

    private static final Logger log =
            LoggerFactory.getLogger(MovieRatingAsyncServiceImpl.class);

    private final MovieRepository movieRepository;
    private final OmdbClient omdbClient;
    private final OmdbProperties omdbProperties;

    public MovieRatingAsyncServiceImpl(
            MovieRepository movieRepository,
            OmdbClient omdbClient,
            OmdbProperties omdbProperties) {

        this.movieRepository = movieRepository;
        this.omdbClient = omdbClient;
        this.omdbProperties = omdbProperties;
    }

    @Async
    @Transactional
    public void enrichRating(Long movieId) {

        Movie movie = movieRepository.findById(movieId).orElse(null);
        if ( movie == null ) {
            return;
        }

        try {
            OmdbResponse response = omdbClient.getMovieByTitle(
                    movie.getTitle(),
                    omdbProperties.getApiKey()
            );

            if ( response != null && response.isSuccessful() ) {
                String ratingStr = response.getImdbRating();

                if ( ratingStr != null && !"N/A".equalsIgnoreCase(ratingStr) ) {
                    movie.setRating(Double.parseDouble(ratingStr));
                    movieRepository.save(movie);
                }
            }

        } catch (Exception ex) {
            log.warn("Failed to enrich rating for movieId={}", movieId, ex);
        }
    }
}