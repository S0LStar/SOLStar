package com.shinhan.solstar.domain.agency.model.repository;

import com.shinhan.solstar.domain.agency.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {

    Optional<Agency> findByEmail(String name);

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    Optional<Agency> findByRefreshToken(String refreshToken);

}
