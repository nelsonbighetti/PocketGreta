package com.source.backend.repository;

import com.source.backend.model.Account;
import com.source.backend.model.AccountCode;
import com.source.backend.model.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountCodeRepository extends JpaRepository<AccountCode, Long> {

    public Optional<AccountCode> findByCode(Code code);
    public boolean existsByCode(Code code);
}
