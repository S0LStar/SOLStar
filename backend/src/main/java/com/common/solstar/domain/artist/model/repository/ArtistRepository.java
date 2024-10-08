package com.common.solstar.domain.artist.model.repository;

import com.common.solstar.domain.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    Optional<Artist> findById(int id);
    List<Artist> findByNameContainingIgnoreCaseOrGroupContainingIgnoreCase(String name, String group);
    boolean existsByName(String name);
}
