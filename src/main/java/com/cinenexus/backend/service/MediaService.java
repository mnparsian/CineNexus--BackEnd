package com.cinenexus.backend.service;

import com.cinenexus.backend.model.media.*;
import com.cinenexus.backend.model.misc.Language;
import com.cinenexus.backend.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MediaService {
  private final MediaRepository mediaRepository;
  private final GenreRepository genreRepository;
  private final MediaGenreRepository mediaGenreRepository;
  private final RestTemplate restTemplate;
  private final LanguageRepository languageRepository;
  private final MediaStatusRepository mediaStatusRepository;
  private final SeasonRepository seasonRepository;
  private final ProductionCompanyRepository productionCompanyRepository;

  @Value("${tmdb.api.url}")
  private String TMDB_BASE_URL;

  @Value("${tmdb.api.key}")
  private String TMDB_API_KEY;

  public MediaService(
      MediaRepository mediaRepository,
      GenreRepository genreRepository,
      MediaGenreRepository mediaGenreRepository,
      RestTemplate restTemplate,
      LanguageRepository languageRepository,
      MediaStatusRepository mediaStatusRepository,
      SeasonRepository seasonRepository,
      ProductionCompanyRepository productionCompanyRepository) {
    this.mediaRepository = mediaRepository;
    this.genreRepository = genreRepository;
    this.mediaGenreRepository = mediaGenreRepository;
    this.restTemplate = restTemplate;
    this.languageRepository = languageRepository;
    this.mediaStatusRepository = mediaStatusRepository;
    this.seasonRepository = seasonRepository;
    this.productionCompanyRepository = productionCompanyRepository;
  }

  public Page<Media> fetchPopularMovies(int page, int size) {
    String url = TMDB_BASE_URL + "/movie/popular?api_key=" + TMDB_API_KEY + "&page=" + (page + 1);
    return fetchMediaCredits(url, page, size, false);
  }

  public Page<Media> fetchPopularTVShows(int page, int size) {
    String url = TMDB_BASE_URL + "/tv/popular?api_key=" + TMDB_API_KEY + "&page=" + (page + 1);
    return fetchMediaCredits(url, page, size, true);
  }

  public Page<Media> fetchNowPlayingMovies(int page, int size) {
    String url =
        TMDB_BASE_URL + "/movie/now_playing?api_key=" + TMDB_API_KEY + "&page=" + (page + 1);
    return fetchMediaCredits(url, page, size, false);
  }

  public Page<Media> fetchNowAiringTVShows(int page, int size) {
    String url = TMDB_BASE_URL + "/tv/on_the_air?api_key=" + TMDB_API_KEY + "&page=" + (page + 1);
    return fetchMediaCredits(url, page, size, true);
  }

  public Media fetchAndStoreMovie(Long tmdbId) {
    String url = TMDB_BASE_URL + "/movie/" + tmdbId + "?api_key=" + TMDB_API_KEY;
    Map<String, Object> response = restTemplate.getForObject(url, Map.class);
    return saveMedia(response, false);
  }

  public Media fetchAndStoreTVShow(Long tmdbId) {
    String url = TMDB_BASE_URL + "/tv/" + tmdbId + "?api_key=" + TMDB_API_KEY;
    Map<String, Object> response = restTemplate.getForObject(url, Map.class);
    return saveMedia(response, true);
  }

  public Page<Media> fetchTopRatedMovies(int page, int size) {
    String url = TMDB_BASE_URL + "/movie/top_rated?api_key=" + TMDB_API_KEY + "&page=" + (page + 1);
    return fetchMediaCredits(url, page, size, false);
  }

  public Page<Media> fetchTopRatedTVShows(int page, int size) {
    String url = TMDB_BASE_URL + "/tv/top_rated?api_key=" + TMDB_API_KEY + "&page=" + (page + 1);
    return fetchMediaCredits(url, page, size, true);
  }

  public Page<Media> fetchMediaCredits(String url, int page, int size, boolean isTVShow) {
    Map<String, Object> response = restTemplate.getForObject(url, Map.class);
    List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

    for (Map<String, Object> result : results) {
      saveMedia(result, isTVShow);
    }

    return mediaRepository.findAll(PageRequest.of(page, size));
  }

  public Page<Media> searchMedia(String query, int page, int size) {
    String url =
        TMDB_BASE_URL
            + "/search/multi?api_key="
            + TMDB_API_KEY
            + "&query="
            + query
            + "&page="
            + (page + 1);
    return fetchMediaCredits(url, page, size, false);
  }

  private Media saveMedia(Map<String, Object> response, boolean isTVShow) {
    Long tmdbId = ((Number) response.get("id")).longValue();
    if (mediaRepository.existsByTmdbId(tmdbId)) {
      throw new RuntimeException("This media already exists in the database.");
    }

    Media media = new Media();
    media.setTmdbId(tmdbId);

    if (isTVShow) {

      media.setTitle((String) response.get("name"));
      media.setOriginalTitle((String) response.get("original_name"));
      media.setReleaseDate(
          response.get("first_air_date") != null
              ? LocalDate.parse((String) response.get("first_air_date"))
              : null);
    } else {

      media.setTitle((String) response.get("title"));
      media.setOriginalTitle((String) response.get("original_title"));
      media.setReleaseDate(
          response.get("release_date") != null
              ? LocalDate.parse((String) response.get("release_date"))
              : null);
    }

    media.setDescription((String) response.get("overview"));
    media.setPosterUrl("https://image.tmdb.org/t/p/w500" + response.get("poster_path"));
    media.setBackdropUrl("https://image.tmdb.org/t/p/w780" + response.get("backdrop_path"));

    String languageCode = (String) response.get("original_language");
    Language language =
        languageRepository
            .findByCode(languageCode)
            .orElseGet(
                () -> {
                  Language newLang = new Language();
                  newLang.setCode(languageCode);
                  newLang.setName(languageCode.toUpperCase());
                  return languageRepository.save(newLang);
                });
    media.setLanguage(language);

    media.setStatus(
        mediaStatusRepository
            .findByName("Released")
            .orElseThrow(() -> new RuntimeException("Status not found")));

    List<Integer> genreIds =
        response.get("genre_ids") != null
            ? (List<Integer>) response.get("genre_ids")
            : new ArrayList<>();
    List<Genre> genres = genreRepository.findAllByTmdbIdIn(genreIds);
    List<MediaGenre> mediaGenres =
        genres.stream().map(genre -> new MediaGenre(media, genre)).collect(Collectors.toList());
    media.setMediaGenres(mediaGenres);


    Media savedMedia = mediaRepository.save(media);


    System.out.println("Seasons from API: " + response.get("seasons"));
    System.out.println("Production Companies from API: " + response.get("production_companies"));


    if (isTVShow) {
      List<Map<String, Object>> seasons =
          response.get("seasons") != null
              ? (List<Map<String, Object>>) response.get("seasons")
              : new ArrayList<>();

      List<Season> seasonList =
          seasons.stream()
              .map(
                  season -> {
                    Season newSeason =
                        new Season(
                            savedMedia,
                            (Integer) season.get("season_number"),
                            season.get("episode_count") != null
                                ? (Integer) season.get("episode_count")
                                : 0,
                            new ArrayList<>());

                    if (season.get("air_date") != null) {
                      newSeason.setAirDate((String) season.get("air_date"));
                    }
                    return newSeason;
                  })
              .collect(Collectors.toList());

      savedMedia.setSeasons(seasonList);
    } else {

      Integer runtime =
          response.get("runtime") != null ? ((Number) response.get("runtime")).intValue() : 0;
      savedMedia.setTagline(
          response.get("tagline") != null
              ? (String) response.get("tagline")
              : "No tagline available");
      savedMedia.setHomepage(
          response.get("homepage") != null ? (String) response.get("homepage") : "N/A");
      Long budget =
          response.get("budget") != null ? ((Number) response.get("budget")).longValue() : 0L;
      Long revenue =
          response.get("revenue") != null ? ((Number) response.get("revenue")).longValue() : 0L;
    }

    System.out.println("Saving media with TMDB ID: " + tmdbId);
    System.out.println("Title: " + media.getTitle());
    System.out.println("Original Title: " + media.getOriginalTitle());
    System.out.println("Description: " + media.getDescription());
    System.out.println("Tagline: " + media.getTagline());
    System.out.println("Poster URL: " + media.getPosterUrl());
    System.out.println("Backdrop URL: " + media.getBackdropUrl());


    List<Map<String, Object>> companies =
        response.get("production_companies") != null
            ? (List<Map<String, Object>>) response.get("production_companies")
            : new ArrayList<>();

    List<ProductionCompany> productionCompanies =
        companies.stream()
            .map(
                company ->
                    new ProductionCompany(
                        null,
                        (Long ) company.get("id"),
                        (String) company.get("name"),
                        (String) company.get("logo_path"),
                        (String) company.get("origin_country"),
                        savedMedia))
            .collect(Collectors.toList());

    savedMedia.setProductionCompanies(productionCompanies);


    Media finalMedia = mediaRepository.save(savedMedia);
    System.out.println("Saved Media: " + finalMedia);
    return finalMedia;
  }

  public void fetchAndSaveTVDetails() {
    List<Media> tvShows = mediaRepository.findByIsTVShowTrue();

    for (Media media : tvShows) {
      try {
        String url = "https://api.themoviedb.org/3/tv/" + media.getTmdbId() + "?api_key=" + TMDB_API_KEY + "&language=en-US";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
          ObjectMapper objectMapper = new ObjectMapper();
          JsonNode jsonNode = objectMapper.readTree(response.getBody());


          List<Season> seasons = new ArrayList<>();
          for (JsonNode seasonNode : jsonNode.get("seasons")) {
            Season season = new Season();
            season.setName(seasonNode.get("name").asText());
            season.setSeasonNumber(seasonNode.get("season_number").asInt());
            season.setTotalEpisodes(seasonNode.get("episode_count").asInt());
            season.setMedia(media);
            seasons.add(season);
          }


          List<ProductionCompany> companies = new ArrayList<>();
          for (JsonNode companyNode : jsonNode.get("production_companies")) {
            ProductionCompany company = new ProductionCompany();
            company.setName(companyNode.get("name").asText());
            company.setOriginCountry(companyNode.get("origin_country").asText());
            company.setMedia(media);
            companies.add(company);
          }


          media.setSeasons(seasons);
          media.setProductionCompanies(companies);
          mediaRepository.save(media);

          System.out.println("جزئیات سریال '{}' ذخیره شد."+ media.getTitle());
        }
      } catch (Exception e) {
        System.out.println("خطا در دریافت جزئیات سریال {}: {}" + media.getTmdbId() + e.getMessage());
      }
    }
  }
}
