package com.source.backend.repository;

import com.source.backend.model.Account;
import com.source.backend.model.AccountAndCode;
import com.source.backend.model.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountAndCodeRepository extends JpaRepository<AccountAndCode, Long> {

    public boolean existsByAccountAndCode(Account account, Code code);
}
