package com.common.solstar.domain.board.model.repository;

import com.common.solstar.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findByFunding_IdAndIsDeleteFalse(int fundingId);
}
