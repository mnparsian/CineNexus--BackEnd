package com.cinenexus.backend.service;

import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.media.MediaGenre;
import com.cinenexus.backend.repository.MediaGenreRepository;
import com.cinenexus.backend.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MediaQueryService {

    private final MediaRepository mediaRepository;
    private final MediaGenreRepository mediaGenreRepository;

    public Page<Media> filterMedia(
            Optional<String> title,
            Optional<String> category,
            Optional<String> mediaType,
            Optional<List<MediaGenre>> mediaGenres,
            Optional<Double> minVoteAverage,
            Optional<Double> maxVoteAverage,
            Optional<LocalDate> releasedAfter,
            Optional<LocalDate> releasedBefore,
            Pageable pageable
    ) {
        Specification<Media> spec = Specification.where(null);

        if (title.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("title")), "%" + title.get().toLowerCase() + "%"));
        }

        if (category.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("category"), category.get()));
        }

        if (mediaType.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("mediaType").get("name"), mediaType.get()));
        }
        if(mediaGenres.isPresent()){
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("mediaGenres"), mediaGenres.get()));
        }

        if (minVoteAverage.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.ge(root.get("voteAverage"), minVoteAverage.get()));
        }

        if (maxVoteAverage.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.le(root.get("voteAverage"), maxVoteAverage.get()));
        }

        if (releasedAfter.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("releaseDate"), releasedAfter.get()));
        }

        if (releasedBefore.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("releaseDate"), releasedBefore.get()));
        }

        return mediaRepository.findAll(spec, pageable);
    }

    public List<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    public Media getMediaById(Long id) {
        return mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found with ID: " + id));
    }

    public Page<Media> getMediaByCategory(String category, int page ,int size) {
        return mediaRepository.findByCategory(category,PageRequest.of(page,size));
    }

    public Page<Media> searchMediaByTitle(String title, int page, int size) {
        return mediaRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(page, size));
    }

    public Page<Media> getMediaByType(String typeName, int page, int size) {
        return mediaRepository.findByMediaType_Name(typeName, PageRequest.of(page, size));
    }

    public Page<Media> getMediaByPopularity(int page, int size) {
        return mediaRepository.findAllByOrderByPopularityDesc(PageRequest.of(page, size));
    }
    public Page<Media> getMediaByGenre(Long genreId,int page,int size) {
        return mediaGenreRepository.findMediaByGenreId(genreId,PageRequest.of(page,size));
    }

}

