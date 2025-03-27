package com.cinenexus.backend.repository;

import com.cinenexus.backend.model.media.Media;
import com.cinenexus.backend.model.media.MediaCrew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaCrewRepository extends JpaRepository<MediaCrew, Long> {
    @Query("SELECT mc FROM MediaCrew mc " +
            "JOIN FETCH mc.crewRole cr " +
            "JOIN FETCH mc.person p " +
            "JOIN FETCH mc.media m " +
            "WHERE m.id = :mediaId AND LOWER(cr.name) = LOWER(:roleName)")
    List<MediaCrew> findByMediaIdAndCrewRoleName(@Param("mediaId") Long mediaId,
                                                 @Param("roleName") String roleName);

    @Query("SELECT mc FROM MediaCrew mc " +
            "JOIN FETCH mc.media m " +
            "JOIN FETCH mc.person p " +
            "JOIN FETCH mc.crewRole cr " +
            "WHERE m.id = :mediaId")
    List<MediaCrew> findAllByMediaId(@Param("mediaId") Long mediaId);

    @Query("SELECT mc FROM MediaCrew mc " +
            "JOIN FETCH mc.media m " +
            "JOIN FETCH mc.person p " +
            "JOIN FETCH mc.crewRole cr " +
            "WHERE p.id = :personId")
    List<MediaCrew> findAllByPersonId(@Param("personId") Long personId);


    @Query("SELECT mc.media FROM MediaCrew mc WHERE mc.person.id = :personId")
    List<Media> findMediaByPersonId(@Param("personId") Long personId);

}
