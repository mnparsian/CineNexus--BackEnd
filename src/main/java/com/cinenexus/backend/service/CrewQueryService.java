package com.cinenexus.backend.service;


import com.cinenexus.backend.dto.media.MediaResponseDTO;
import com.cinenexus.backend.model.media.*;
import com.cinenexus.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrewQueryService {

    private final PersonRepository personRepository;
    private final MediaCrewRepository mediaCrewRepository;
    private final ProductionCompanyRepository productionCompanyRepository;
    private final CrewRoleRepository crewRoleRepository;
    private final MediaResponseDTO mediaResponseDTO;

    public CrewQueryService(PersonRepository personRepository,
                            MediaCrewRepository mediaCrewRepository,
                            ProductionCompanyRepository productionCompanyRepository,
                            CrewRoleRepository crewRoleRepository,
                            MediaResponseDTO mediaResponseDTO) {
        this.personRepository = personRepository;
        this.mediaCrewRepository = mediaCrewRepository;
        this.productionCompanyRepository = productionCompanyRepository;
        this.crewRoleRepository = crewRoleRepository;
        this.mediaResponseDTO = mediaResponseDTO;
    }

    // Person methods
    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }

    public Optional<Person> getPersonByTmdbId(Long tmdbId) {
        return personRepository.findByTmdbId(tmdbId);
    }

    public List<Person> searchPersonByName(String name) {
        return personRepository.searchByName(name);
    }

    // MediaCrew methods
    public List<MediaCrew> getCrewByMediaId(Long mediaId) {
        return mediaCrewRepository.findAllByMediaId(mediaId);
    }

    public List<MediaCrew> getCrewByMediaIdAndRole(Long mediaId, String roleName) {
        return mediaCrewRepository.findByMediaIdAndCrewRoleName(mediaId, roleName);
    }

    public List<MediaCrew> getMediaByPersonId(Long personId) {
        return mediaCrewRepository.findAllByPersonId(personId);
    }

    // ProductionCompany methods
    public List<ProductionCompany> getCompaniesByMediaId(Long mediaId) {
        return productionCompanyRepository.findAllByMediaId(mediaId);
    }

    // CrewRole methods
    public List<CrewRole> getAllCrewRoles() {
        return crewRoleRepository.findAll();
    }
    public List<MediaResponseDTO> getAllMediaByPersonId(Long personId) {
        List<Media> listMedia = mediaCrewRepository.findMediaByPersonId(personId);
        return listMedia.stream().map(mediaResponseDTO::fromEntity).toList();
    }
}

