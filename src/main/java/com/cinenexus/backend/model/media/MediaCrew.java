package com.cinenexus.backend.model.media;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "media_crew")
public class MediaCrew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "media_id", nullable = false)
    @JsonBackReference
    private Media media;

    @ManyToOne
    @JoinColumn(name = "crew_role_id", nullable = false)
    private CrewRole crewRole;

    @Column
    private String characterName; // فقط برای بازیگران استفاده می‌شود

    public MediaCrew(Person person, Media media, CrewRole crewRole, String characterName) {
        this.person = person;
        this.media = media;
        this.crewRole = crewRole;
        this.characterName = characterName;
    }
}

