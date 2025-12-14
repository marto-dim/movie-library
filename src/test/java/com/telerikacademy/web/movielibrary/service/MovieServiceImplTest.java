package com.telerikacademy.web.movielibrary.service;

import com.telerikacademy.web.movielibrary.model.Movie;
import com.telerikacademy.web.movielibrary.repository.MovieRepository;
import com.telerikacademy.web.movielibrary.service.async.MovieRatingAsyncServiceImpl;
import com.telerikacademy.web.movielibrary.service.movie.MovieServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.telerikacademy.web.movielibrary.util.StringErrorConstants.MOVIE_ID_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieRatingAsyncServiceImpl movieRatingAsyncService;

    @InjectMocks
    private MovieServiceImpl movieService;

    // ---------- CREATE ----------

    @Test
    void create_shouldSaveMovieAndTriggerAsyncEnrichment() {
        Movie movie = createMovie(null);
        Movie saved = createMovie(1L);

        when(movieRepository.save(movie)).thenReturn(saved);

        Movie result = movieService.create(movie);

        assertThat(result.getId()).isEqualTo(1L);
        verify(movieRepository).save(movie);
        verify(movieRatingAsyncService).enrichRating(1L);
    }

    // ---------- GET ALL ----------

    @Test
    void getAll_shouldReturnAllMovies() {
        when(movieRepository.findAll())
                .thenReturn(List.of(createMovie(1L), createMovie(2L)));

        List<Movie> result = movieService.getAll();

        assertThat(result).hasSize(2);
        verify(movieRepository).findAll();
    }

    // ---------- GET BY ID ----------

    @Test
    void getById_existingMovie_shouldReturnMovie() {
        Movie movie = createMovie(1L);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        Movie result = movieService.getById(1L);

        assertThat(result.getTitle()).isEqualTo("Inception");
    }

    @Test
    void getById_missingMovie_shouldThrowException() {
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.getById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format(MOVIE_ID_NOT_FOUND, 99L));
    }

    // ---------- UPDATE ----------

    @Test
    void update_shouldUpdateProvidedFieldsOnly() {
        Movie existing = createMovie(1L);
        Movie update = new Movie();
        update.setTitle("Updated Title");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(movieRepository.save(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));

        Movie result = movieService.update(1L, update);

        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getDirector()).isEqualTo("Christopher Nolan");
        verify(movieRepository).save(existing);
    }

    @Test
    void update_missingMovie_shouldThrowException() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.update(1L, new Movie()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------- DELETE ----------

    @Test
    void delete_existingMovie_shouldDelete() {
        Movie movie = createMovie(1L);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        doNothing().when(movieRepository).delete(movie);

        movieService.delete(1L);

        verify(movieRepository).delete(movie);
    }

    @Test
    void delete_missingMovie_shouldThrowException() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.delete(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------- Helper ----------

    private Movie createMovie(Long id) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle("Inception");
        movie.setDirector("Christopher Nolan");
        movie.setReleaseYear(2010);
        movie.setRating(8.8);
        return movie;
    }
}
