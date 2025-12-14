package com.telerikacademy.web.movielibrary.client.omdb;

import com.telerikacademy.web.movielibrary.dto.omdb.OmdbResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "omdbClient",
        url = "${omdb.base-url}"
)
public interface OmdbClient {

    @GetMapping("/")
    OmdbResponse getMovieByTitle(
            @RequestParam("t") String title,
            @RequestParam("apikey") String apiKey
    );
}
