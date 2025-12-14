package com.telerikacademy.web.movielibrary.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telerikacademy.web.movielibrary.dto.movie.MovieCreateDto;
import com.telerikacademy.web.movielibrary.model.Movie;
import com.telerikacademy.web.movielibrary.service.movie.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieService movieService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createMovie_shouldReturn201() throws Exception {
        MovieCreateDto dto = new MovieCreateDto();
        dto.setTitle("Inception");
        dto.setDirector("Nolan");
        dto.setReleaseYear(2010);

        when(movieService.create(any(Movie.class)))
                .thenReturn(createMovie(1L));

        mockMvc.perform(post("/api/movies")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.rating").value(8.8));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createMovie_blankTitle_shouldReturn400() throws Exception {
        MovieCreateDto dto = new MovieCreateDto();
        dto.setTitle("");
        dto.setReleaseYear(2010);

        mockMvc.perform(post("/api/movies")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages").isArray());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllMovies_shouldReturn200() throws Exception {
        when(movieService.getAll())
                .thenReturn(List.of(createMovie(1L), createMovie(2L)));

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getMovieById_shouldReturnMovie() throws Exception {
        when(movieService.getById(1L))
                .thenReturn(createMovie(1L));

        mockMvc.perform(get("/api/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateMovie_shouldReturnUpdatedMovie() throws Exception {
        Movie updated = createMovie(1L);
        updated.setTitle("Updated");

        when(movieService.update(eq(1L), any(Movie.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/movies/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "title": "Updated"
                }
            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteMovie_shouldReturn204() throws Exception {
        doNothing().when(movieService).delete(1L);

        mockMvc.perform(delete("/api/movies/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteMovie_asUser_shouldBeForbidden() throws Exception {
        mockMvc.perform(delete("/api/movies/1"))
                .andExpect(status().isForbidden());
    }

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