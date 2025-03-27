package com.cinenexus.backend.service;

import com.cinenexus.backend.model.media.Genre;
import com.cinenexus.backend.repository.GenreRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GenreService {

  private final RestTemplate restTemplate;
  private final GenreRepository genreRepository;

  @Value("${tmdb.api.key}")
  private String apiKey;

  @Value("${tmdb.api.url}")
  private String apiUrl;

  public GenreService(RestTemplate restTemplate, GenreRepository genreRepository) {
    this.restTemplate = restTemplate;
    this.genreRepository = genreRepository;
  }


  @Transactional
  public void fetchAndStoreGenres(String type) {
      String url = apiUrl + "/genre/" + type + "/list?api_key=" + apiKey;
      Map<String, Object> response = restTemplate.getForObject(url, Map.class);

      if (response == null || !response.containsKey("genres")) {
          throw new RuntimeException("No genres found from TMDB.");
      }

      List<Map<String, Object>> genres = (List<Map<String, Object>>) response.get("genres");

      for (Map<String, Object> genreData : genres) {
          Long tmdbId = ((Number) genreData.get("id")).longValue();
          String name = (String) genreData.get("name");

          Optional<Genre> existingGenre = genreRepository.findByTmdbId(tmdbId);
          if (existingGenre.isEmpty()) {
              Genre genre = new Genre();
              genre.setName(name);
              genre.setTmdbId(tmdbId);
              genreRepository.save(genre);
          }
      }
  }

    public Page<Genre> getAllGenres(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return genreRepository.findAll(pageable);
    }


}
