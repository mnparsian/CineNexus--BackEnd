package com.cinenexus.backend.service;

import com.cinenexus.backend.model.media.Media;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RmService {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<Media> findRecommendedMovies(
            Set<Long> favoriteGenres,
            Set<Long> favoriteDirectors,
            Set<Long> favoriteActors,
            Set<Long> favoriteCompanies,
            List<Long> watchedMovies,
            Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Media> query = cb.createQuery(Media.class);
        Root<Media> media = query.from(Media.class);

        List<Predicate> predicates = new ArrayList<>();


        if (!favoriteGenres.isEmpty()) {
            predicates.add(media.join("mediaGenres").join("genre").get("id").in(favoriteGenres));
        }


        if (!favoriteDirectors.isEmpty()) {
            predicates.add(media.join("mediaCrew").join("person").get("id").in(favoriteDirectors));
        }


        if (!favoriteActors.isEmpty()) {
            predicates.add(media.join("mediaCrew").join("person").get("id").in(favoriteActors));
        }

        if (!favoriteCompanies.isEmpty()) {
            predicates.add(media.join("productionCompanies").get("id").in(favoriteCompanies));
        }


        if (!watchedMovies.isEmpty()) {
            predicates.add(cb.not(media.get("id").in(watchedMovies)));
        }


        query.select(media)
                .where(cb.or(predicates.toArray(new Predicate[0])))
                .orderBy(cb.desc(media.get("voteAverage")), cb.desc(media.get("popularity")));

        TypedQuery<Media> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(typedQuery.getResultList(), pageable, typedQuery.getResultList().size());
    }
}

