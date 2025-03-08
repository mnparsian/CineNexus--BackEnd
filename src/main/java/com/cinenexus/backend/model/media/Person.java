package com.cinenexus.backend.model.media;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tmdbId;

    @Column(nullable = false)
    private String name;

    private String bio;
    private String profileImage;
    private LocalDate birthDate;

    public Person(Long tmdbId, String name, String bio, String profileImage, LocalDate birthDate) {
        this.tmdbId = tmdbId;
        this.name = name;
        this.bio = bio;
        this.profileImage = profileImage;
        this.birthDate = birthDate;
    }
}
