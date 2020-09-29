package com.source.backend.service;


import com.source.backend.Dto.HistoryDto;
import com.source.backend.mapper.AccountToAccountAndCodeDtoMapper;
import com.source.backend.model.Account;
import com.source.backend.model.AccountAndCode;
import com.source.backend.model.AccountCode;
import com.source.backend.model.Code;
import com.source.backend.repository.AccountAndCodeRepository;
import com.source.backend.repository.AccountCodeRepository;
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
    private final AccountToAccountAndCodeDtoMapper historyMapper;
    private final AccountCodeRepository accountCodeRepository;

    public String setBonusesForAccount(String code) throws Exception {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Optional<Account> accountOpt = userRepository.findByUsername(username);
        Account account  = accountOpt
                .orElseThrow(() -> new UsernameNotFoundException("No Account " +
                        "Found with username : " + username));
        Optional<Code> codeOptional = codeRepository.findCodeByQrcode(code);
        if (codeOptional.isPresent()) {
            Code qCode = codeRepository.findCodeByQrcode(code).get();
            if(accountAndCodeRepository.existsByAccountAndCode(account,qCode))
            {
                throw new Exception("AccountAlreadySetThisCode");
            }

            if(accountCodeRepository.existsByCode(qCode)) {
                int bonuses = -qCode.getBonuses();
                AccountCode accountCode = accountCodeRepository.findByCode(qCode)
                        .orElseThrow(() -> new Exception());
                Account ownerOfCode = accountCode.getAccount();
                account.setBonuses(account.getBonuses() + bonuses);
                ownerOfCode.setBonuses(ownerOfCode.getBonuses() - bonuses);
                accountAndCodeRepository.save(AccountAndCode.builder().account(account).bonuses(bonuses).date(Instant.now()).code(qCode).build());
                accountAndCodeRepository.save(AccountAndCode.builder().account(ownerOfCode).bonuses(-bonuses).date(Instant.now()).code(qCode).build());
                return "TransferWasSuccessful";
            }

            account.setBonuses(account.getBonuses() + qCode.getBonuses());

            userRepository.save(account);
            AccountAndCode accountAndCode = AccountAndCode.builder().account(account).code(qCode).bonuses(qCode.getBonuses()).date(Instant.now()).build();
            accountAndCodeRepository.save(accountAndCode);
        }
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
    
    public void addCode(String code, int bonuses) throws Exception {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Account account  = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No Account " +
                        "Found with username : " + username));
        if (account.getBonuses()<bonuses){
            throw new Exception("Don't have enough bonuses");
        }
        Code newCode = Code.builder().qrcode(code).bonuses(-bonuses).build();
        codeRepository.save(newCode);
        AccountCode accountCode = AccountCode.builder().account(account).code(newCode).date(Instant.now()).build();
        accountCodeRepository.save(accountCode);
    }
}
