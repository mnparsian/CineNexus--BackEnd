package com.cinenexus.backend.ai;

import com.cinenexus.backend.dto.media.MediaResponseDTO;
import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.repository.MediaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final MediaRepository mediaRepository;
private final MediaResponseDTO mediaResponseDTO;
    public RecommendationService(MediaRepository mediaRepository,MediaResponseDTO mediaResponseDTO) {
        this.mediaRepository = mediaRepository;
        this.mediaResponseDTO = mediaResponseDTO;
    }

    public Page<MediaResponseDTO> getRecommendedMedia(int page, int size, String sortBy, String category) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Media> mediaPage = mediaRepository.findByCategory(category, pageable);

        return mediaPage.map(mediaResponseDTO::fromEntity);
    }
}
