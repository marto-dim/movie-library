package com.telerikacademy.web.movielibrary.model;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String director;

    @Column(name = "release_year")
    private Integer releaseYear;

    private Double rating;
}
