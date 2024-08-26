package com.shinhan.solstar.domain.likeList.model.repository;

import com.shinhan.solstar.domain.artist.entity.Artist;
import com.shinhan.solstar.domain.likeList.entity.LikeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeListRepository extends JpaRepository<LikeList, Integer> {
    List<Artist> findByUser_Id(int userId);
}
