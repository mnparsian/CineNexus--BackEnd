package com.cinenexus.backend.controller;

import com.cinenexus.backend.model.media.*;
import com.cinenexus.backend.service.CrewQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crew")
public class CrewQueryController {

    private final CrewQueryService crewQueryService;

    public CrewQueryController(CrewQueryService crewQueryService) {
        this.crewQueryService = crewQueryService;
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable Long id) {
        return crewQueryService.getPersonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/person/tmdb/{tmdbId}")
    public ResponseEntity<?> getPersonByTmdbId(@PathVariable Long tmdbId) {
        return crewQueryService.getPersonByTmdbId(tmdbId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Person>> searchPersonByName(@RequestParam String name) {
        return ResponseEntity.ok(crewQueryService.searchPersonByName(name));
    }

    @GetMapping("/media/{mediaId}")
    public ResponseEntity<List<?>> getCrewByMediaId(@PathVariable Long mediaId) {
        return ResponseEntity.ok(crewQueryService.getCrewByMediaId(mediaId));
    }

    @GetMapping("/media/{mediaId}/role/{roleName}")
    public ResponseEntity<List<?>> getCrewByMediaIdAndRole(@PathVariable Long mediaId, @PathVariable String roleName) {
        return ResponseEntity.ok(crewQueryService.getCrewByMediaIdAndRole(mediaId, roleName));
    }

    @GetMapping("/person/{personId}/media")
    public ResponseEntity<List<?>> getMediaByPersonId(@PathVariable Long personId) {
        return ResponseEntity.ok(crewQueryService.getMediaByPersonId(personId));
    }

    @GetMapping("/media/{mediaId}/companies")
    public ResponseEntity<List<?>> getCompaniesByMediaId(@PathVariable Long mediaId) {
        return ResponseEntity.ok(crewQueryService.getCompaniesByMediaId(mediaId));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<?>> getAllCrewRoles() {
        return ResponseEntity.ok(crewQueryService.getAllCrewRoles());
    }
}

