package com.shinhan.solstar.domain.user.model.repository;

import com.shinhan.solstar.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findById(int id);
}
