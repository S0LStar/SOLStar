package com.common.solstar.domain.funding.model.repository;

import com.common.solstar.domain.artist.entity.Artist;
import com.common.solstar.domain.funding.entity.Funding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FundingRepository extends JpaRepository<Funding, Integer> {
    Optional<Funding> findById(int id);
    List<Funding> findByArtist_Id(int artistId);
    List<Funding> findByArtistIn(List<Artist> artistList);
    List<Funding> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT f FROM Funding f WHERE f.isDelete = false ORDER BY f.totalJoin DESC")
    List<Funding> findPopularFundings();
}
