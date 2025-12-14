package com.telerikacademy.web.movielibrary.service;

import com.telerikacademy.web.movielibrary.client.omdb.OmdbClient;
import com.telerikacademy.web.movielibrary.config.OmdbProperties;
import com.telerikacademy.web.movielibrary.dto.omdb.OmdbResponse;
import com.telerikacademy.web.movielibrary.model.Movie;
import com.telerikacademy.web.movielibrary.repository.MovieRepository;
import com.telerikacademy.web.movielibrary.service.async.MovieRatingAsyncServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieRatingAsyncServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private OmdbClient omdbClient;

    @Mock
    private OmdbProperties omdbProperties;

    @InjectMocks
    private MovieRatingAsyncServiceImpl service;

    // ---------- MOVIE NOT FOUND ----------

    @Test
    void enrichRating_movieNotFound_shouldDoNothing() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        service.enrichRating(1L);

        verifyNoInteractions(omdbClient);
        verify(movieRepository, never()).save(any());
    }

    // ---------- SUCCESSFUL RATING ----------

    @Test
    void enrichRating_successfulResponse_shouldSaveRating() {
        Movie movie = createMovie(1L);
        OmdbResponse response = createResponse("8.7");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(omdbProperties.getApiKey()).thenReturn("test-key");
        when(omdbClient.getMovieByTitle("Inception", "test-key"))
                .thenReturn(response);

        service.enrichRating(1L);

        assertThat(movie.getRating()).isEqualTo(8.7);
        verify(movieRepository).save(movie);
    }

    // ---------- N/A RATING ----------

    @Test
    void enrichRating_naRating_shouldNotSave() {
        Movie movie = createMovie(1L);
        OmdbResponse response = createResponse("N/A");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(omdbProperties.getApiKey()).thenReturn("test-key");
        when(omdbClient.getMovieByTitle(any(), any()))
                .thenReturn(response);

        service.enrichRating(1L);

        verify(movieRepository, never()).save(any());
    }

    // ---------- UNSUCCESSFUL RESPONSE ----------

    @Test
    void enrichRating_unsuccessfulResponse_shouldNotSave() {
        Movie movie = createMovie(1L);
        OmdbResponse response = new OmdbResponse();
        response.setResponse("False");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(omdbProperties.getApiKey()).thenReturn("test-key");
        when(omdbClient.getMovieByTitle(any(), any()))
                .thenReturn(response);

        service.enrichRating(1L);

        verify(movieRepository, never()).save(any());
    }

    // ---------- EXCEPTION HANDLING ----------

    @Test
    void enrichRating_exceptionThrown_shouldBeSwallowed() {
        Movie movie = createMovie(1L);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(omdbProperties.getApiKey()).thenReturn("test-key");
        when(omdbClient.getMovieByTitle(any(), any()))
                .thenThrow(new RuntimeException("OMDb down"));

        // Should NOT throw
        service.enrichRating(1L);

        verify(movieRepository, never()).save(any());
    }

    // ---------- Helpers ----------

    private Movie createMovie(Long id) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle("Inception");
        return movie;
    }

    private OmdbResponse createResponse(String rating) {
        OmdbResponse response = new OmdbResponse();
        response.setResponse("True");
        response.setImdbRating(rating);
        return response;
    }
}

