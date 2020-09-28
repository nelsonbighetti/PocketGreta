package com.source.backend.service;


import com.source.backend.Dto.HistoryDto;
import com.source.backend.mapper.AccountToAccountAndCodeDtoMapper;
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

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
@AllArgsConstructor
public class BonusesService {
    private final UserRepository userRepository;
    private final AccountAndCodeRepository accountAndCodeRepository;
    private final CodeRepository codeRepository;
    private AccountToAccountAndCodeDtoMapper historyMapper;
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
        AccountAndCode accountAndCode = AccountAndCode.builder().account(account).code(qCode).bonuses(qCode.getBonuses()).date(Instant.now()).build();
        accountAndCodeRepository.save(accountAndCode);
        return "New code was set for account";
    }

    public Set<HistoryDto> bonusesHistory() throws Exception {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Account account  = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No Account " +
                        "Found with username : " + username));
        return accountAndCodeRepository.findAllByAccountOrderByDateAsc(account)
                .stream()
                .map(e -> historyMapper.mapToDto(e)).collect(toSet());
    }
}
