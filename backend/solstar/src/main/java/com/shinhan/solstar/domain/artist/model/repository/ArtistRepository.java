package com.shinhan.solstar.domain.artist.model.repository;

import com.shinhan.solstar.domain.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    Optional<Artist> findById(int id);
}
