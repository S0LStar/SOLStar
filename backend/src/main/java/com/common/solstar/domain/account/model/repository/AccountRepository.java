package com.common.solstar.domain.account.model.repository;

import com.common.solstar.domain.account.entity.Account;
import com.common.solstar.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByUser(User user);
}
