package com.cinenexus.backend.service;

import com.cinenexus.backend.model.media.*;
import com.cinenexus.backend.model.misc.Language;
import com.cinenexus.backend.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TMDBService {

    private final RestTemplate restTemplate;
    private final MediaRepository mediaRepository;
    private final GenreRepository genreRepository;
    private final ProductionCompanyRepository productionCompanyRepository;
    private final PersonRepository personRepository;
    private final CrewRoleRepository crewRoleRepository;
    private final SeasonRepository seasonRepository;
    private final ObjectMapper objectMapper;
    private final MediaTypeRepository mediaTypeRepository;
    private final LanguageRepository languageRepository;
    private final MediaStatusRepository mediaStatusRepository;
    private final EpisodeRepository episodeRepository;
    private Media lastSavedMedia;

    @Value("${tmdb.api.url}")
    private String TMDB_BASE_URL;

    @Value("${tmdb.api.key}")
    private String TMDB_API_KEY;

    @Transactional
    public Media fetchAndSaveMedia(Long tmdbId, MediaType mediaType) {
        String url = TMDB_BASE_URL + "/" + mediaType.getName() + "/" + tmdbId
                + "?api_key=" + TMDB_API_KEY
                + "&append_to_response=credits,genres,production_companies,seasons";

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            if (root == null) return null;;

            Optional<Media> existingMedia = mediaRepository.findByTmdbId(tmdbId);
            if (existingMedia.isPresent()) return existingMedia.get();

            Media media = new Media();
            media.setTmdbId(tmdbId);
            media.setTitle(root.get("title") != null ? root.get("title").asText() : root.get("name").asText());
            media.setOriginalTitle(root.get("original_title") != null
                    ? root.get("original_title").asText()
                    : root.get("original_name").asText());
            media.setOverview(root.get("overview").asText());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String releaseDateStr = root.has("release_date") ? root.get("release_date").asText() : root.get("first_air_date").asText();

            LocalDate releaseDate = null;
            if (releaseDateStr != null && !releaseDateStr.isEmpty()) {
                try {
                    releaseDate = LocalDate.parse(releaseDateStr, formatter);
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }
            }
            String languageCode = root.get("original_language").asText();
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
            media.setReleaseDate(releaseDate);
            media.setPopularity(root.get("popularity").asDouble());
            media.setVoteAverage(root.get("vote_average").asDouble());
            media.setVoteCount(root.get("vote_count").asInt());
            media.setPosterUrl(root.get("poster_path").asText());
            media.setBackdropUrl(root.get("backdrop_path").asText());
            String statusName = root.get("status").asText();
            MediaStatus status = mediaStatusRepository.findByName(statusName)
                    .orElseGet(() -> mediaStatusRepository.save(new MediaStatus(statusName)));

            media.setStatus(status);



            MediaType savedMediaType = mediaTypeRepository
                    .findByName(mediaType.getName())
                    .orElseGet(() -> mediaTypeRepository.save(mediaType));
            media.setMediaType(savedMediaType);

            Media saveMediaBeforeGenre = mediaRepository.save(media);


            Set<MediaGenre> mediaGenres = new HashSet<>();
            if (root.has("genres")) {
                for (JsonNode genreNode : root.get("genres")) {
                    Genre genre = genreRepository.findByTmdbId(genreNode.get("id").asLong())
                            .orElseGet(() -> genreRepository.save(new Genre(
                                    genreNode.get("id").asLong(), genreNode.get("name").asText())));
                    mediaGenres.add(new MediaGenre(saveMediaBeforeGenre, genre));
                }
            }
            saveMediaBeforeGenre.setMediaGenres(new ArrayList<>(mediaGenres));
            Media SavedMediaBeforeCompany = mediaRepository.save(saveMediaBeforeGenre);

            Set<ProductionCompany> productionCompanies = new HashSet<>();
            if (root.has("production_companies")) {
                for (JsonNode companyNode : root.get("production_companies")) {
                    ProductionCompany company = productionCompanyRepository.findByTmdbId(companyNode.get("id").asLong())
                            .orElseGet(() -> productionCompanyRepository.save(new ProductionCompany(
                                    companyNode.get("id").asLong(),
                                    companyNode.get("name").asText(),
                                    companyNode.has("logo_path") ? companyNode.get("logo_path").asText() : null,
                                    companyNode.has("origin_country") ? companyNode.get("origin_country").asText() : null,
                                    SavedMediaBeforeCompany)));
                    productionCompanies.add(company);
                }
            }
            SavedMediaBeforeCompany.setProductionCompanies(new ArrayList<>(productionCompanies));

            Media savedMediaBeforeCrew = mediaRepository.save(SavedMediaBeforeCompany);


            Set<MediaCrew> mediaCrews = new HashSet<>();
            if (root.has("credits") && root.get("credits").has("cast")) {
                for (JsonNode castNode : root.get("credits").get("cast")) {
                    Person person = personRepository.findByTmdbId(castNode.get("id").asLong())
                            .orElseGet(() -> personRepository.save(new Person(
                                    castNode.get("id").asLong(),
                                    castNode.get("name").asText(),
                                    null,
                                    castNode.has("profile_path") ? "https://image.tmdb.org/t/p/w500" + castNode.get("profile_path").asText() : null,
                                    null
                            )));

                    String roleName = castNode.has("known_for_department")
                            ? castNode.get("known_for_department").asText()
                            : "Actor";

                    CrewRole role = crewRoleRepository.findByName(roleName)
                            .orElseGet(() -> crewRoleRepository.save(new CrewRole(roleName)));

                    String characterName = role.getName().equals("Actor") ? castNode.get("character").asText() : null;
                    mediaCrews.add(new MediaCrew(person, savedMediaBeforeCrew, role, characterName));
                }
            }
            savedMediaBeforeCrew.setMediaCrew(new ArrayList<>(mediaCrews));

            Media savedMediaBeforeSeason = mediaRepository.save(savedMediaBeforeCrew);


            if ("tv".equals(mediaType.getName()) && root.has("seasons")) {
                Set<Season> seasons = new HashSet<>();
                for (JsonNode seasonNode : root.get("seasons")) {
                    Season season = seasonRepository.findBySeasonNumberAndMedia_TmdbId(
                                    seasonNode.get("season_number").asInt(), tmdbId)
                            .orElseGet(() -> seasonRepository.save(new Season(
                                    savedMediaBeforeSeason,
                                    seasonNode.get("season_number").asInt(),
                                    seasonNode.get("episode_count").asInt(),
                                    seasonNode.get("air_date").asText(),
                                    seasonNode.get("poster_path").asText()
                            )));
                    seasons.add(season);
                }
                savedMediaBeforeSeason.setSeasons(new ArrayList<>(seasons));
            }

            for (Season season : seasonRepository.findByMedia(media)) {
                String episodesUrl = String.format("%s/tv/%d/season/%d?api_key=%s&language=en-US",
                        TMDB_BASE_URL, media.getTmdbId(), season.getSeasonNumber(), TMDB_API_KEY);

                JsonNode episodesResponse = restTemplate.getForObject(episodesUrl, JsonNode.class);

                if (episodesResponse != null && episodesResponse.has("episodes")) {
                    for (JsonNode episodeNode : episodesResponse.get("episodes")) {
                        Episode episode = new Episode();
                        episode.setSeason(season);
                        episode.setEpisodeNumber(episodeNode.get("episode_number").asInt());
                        episode.setTitle(episodeNode.has("name") ? episodeNode.get("name").asText() : "Unknown");
                        episode.setReleaseDate(episodeNode.has("air_date") ? episodeNode.get("air_date").asText() : null);
                        episode.setOverview(episodeNode.has("overview") ? episodeNode.get("overview").asText() : "No description");

                        episode.setTmdbId(episodeNode.get("id").asLong());

                        episodeRepository.save(episode);
                    }
                }
            }



            lastSavedMedia = mediaRepository.save(savedMediaBeforeSeason);
            System.out.println("✅ Media saved: " + savedMediaBeforeSeason.getTitle());
            return lastSavedMedia;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastSavedMedia;

    }


    private static final long TWELVE_HOURS = 12 * 60 * 60 * 1000;

    @Scheduled(fixedRate = TWELVE_HOURS)
    public void updateMediaDatabase() {
        System.out.println("🔄 Updating media database...");
    }


//    public void fetchAndSaveAllMedia() {
//        List<String> categories = List.of(
//                "movie/popular", "movie/now_playing", "movie/top_rated", "movie/upcoming",
//                "tv/popular", "tv/on_the_air", "tv/top_rated", "tv/airing_today"
//        );
//
//        for (String category : categories) {
//            try {
//                String url = String.format("%s/%s?api_key=%s&language=en-US&page=1", TMDB_BASE_URL, category, TMDB_API_KEY);
//                JsonNode response = restTemplate.getForObject(url, JsonNode.class);
//
//                if (response != null && response.has("results")) {
//                    for (JsonNode item : response.get("results")) {
//                        Long tmdbId = item.get("id").asLong();
//                        String mediaType = category.startsWith("movie") ? "movie" : "tv";
//
//                        Media media = fetchAndSaveMedia(tmdbId, new MediaType(mediaType));
//                        if (media != null) {
//                            media.setCategory(category); // ست کردن دسته‌بندی
//                            mediaRepository.save(media);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                System.err.println("⚠️ Error fetching " + category + ": " + e.getMessage());
//            }
//        }
//    }

    public void fetchAndSaveAllMedia() {
        List<String> categories = List.of(
                "movie/upcoming", "tv/top_rated","tv/on_the_air","tv/popular", "tv/top_rated", "tv/airing_today",
                "movie/popular", "movie/now_playing", "movie/top_rated"
        );

        Set<String> processedCategories = new HashSet<>();

        for (String category : categories) {
            if (processedCategories.contains(category)) {
                System.out.println("✅ Skipping category (already processed): " + category);
                continue;
            }

            int page = 1;
            boolean hasMorePages = true;

            while (hasMorePages) {
                try {
                    String url = String.format("%s/%s?api_key=%s&language=en-US&page=%d",
                            TMDB_BASE_URL, category, TMDB_API_KEY, page);
                    JsonNode response = restTemplate.getForObject(url, JsonNode.class);

                    if (response != null && response.has("results")) {
                        for (JsonNode item : response.get("results")) {
                            Long tmdbId = item.get("id").asLong();
                            String mediaType = category.startsWith("movie") ? "movie" : "tv";

                            // چک کنیم آیا این رسانه قبلاً در دیتابیس هست
                            if (mediaRepository.existsByTmdbId(tmdbId)) {
                                System.out.println("⚠️ Skipping media (already exists): " + tmdbId);
                                continue;
                            }

                            Media media = fetchAndSaveMedia(tmdbId, new MediaType(mediaType));
                            if (media != null) {
                                media.setCategory(category);
                                mediaRepository.save(media);
                            }
                        }
                    }


                    int totalPages = response.has("total_pages") && response.get("total_pages").isInt()
                            ? response.get("total_pages").asInt()
                            : 1;

                    hasMorePages = page < totalPages;
                    page++;

                } catch (Exception e) {
                    System.err.println("⚠️ Error fetching " + category + ": " + e.getMessage());
                    hasMorePages = false;
                }
            }

            processedCategories.add(category);
        }
    }


}
