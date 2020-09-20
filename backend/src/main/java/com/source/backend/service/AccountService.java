package com.source.backend.service;

import com.source.backend.Dto.AccountInfoDto;
import com.source.backend.model.Account;
import com.source.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {

    private UserRepository userRepository;

    public AccountInfoDto getInfo(String username){
        Optional<Account> accountOpt = userRepository.findByUsername(username);
        Account account = accountOpt
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + username));

        return AccountInfoDto.builder()
                .username(account.getUsername())
                .email(account.getEmail())
                .bonuses(account.getBonuses())
                .build();
    }
}
