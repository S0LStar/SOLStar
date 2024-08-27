package com.shinhan.solstar.domain.likeList.model.repository;

import com.shinhan.solstar.domain.artist.entity.Artist;
import com.shinhan.solstar.domain.likeList.entity.LikeList;
import com.shinhan.solstar.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeListRepository extends JpaRepository<LikeList, Integer> {
    List<LikeList> findByUser_Id(int userId);
    Optional<LikeList> findByUserAndArtist(User user, Artist artist);
    boolean existsByUserAndArtist(User user, Artist artist);
}