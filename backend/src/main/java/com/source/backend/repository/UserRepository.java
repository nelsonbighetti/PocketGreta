package com.source.backend.repository;

import com.source.backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Account, Long> {

    public Optional<Account> findByUsername(String username);
    @Query("SELECT username FROM Account a WHERE email=?1")
    public Optional<String> getUsernameByEmailOnly(String email);
}
