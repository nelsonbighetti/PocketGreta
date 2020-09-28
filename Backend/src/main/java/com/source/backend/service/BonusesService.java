package com.source.backend.service;


import com.source.backend.model.Account;
import com.source.backend.model.AccountAndCode;
import com.source.backend.model.Code;
import com.source.backend.repository.AccountAndCodeRepository;
import com.source.backend.repository.CodeRepository;
import com.source.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BonusesService {
    private final UserRepository userRepository;
    private final AccountAndCodeRepository accountAndCodeRepository;
    private final CodeRepository codeRepository;

    public String setBonusesForAccount(String code) throws Exception {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<Account> accountOpt = userRepository.findByUsername(username);
        Account account  = accountOpt
                .orElseThrow(() -> new UsernameNotFoundException("No Account " +
                        "Found with username : " + username));
        Code qCode = codeRepository.findCodeByQrcode(code) .orElseThrow(() -> new IllegalArgumentException("No code found with code "+ code));
        if (accountAndCodeRepository.existsByAccountAndCode(account, qCode )){
            throw new Exception("AccountAlreadySetThisCode");
        }
        account.setBonuses(account.getBonuses() + qCode.getBonuses());

        userRepository.save(account);
        AccountAndCode accountAndCode = AccountAndCode.builder().account(account).code(qCode).build();
        accountAndCodeRepository.save(accountAndCode);
        return "New code was set for account";
    }
}
