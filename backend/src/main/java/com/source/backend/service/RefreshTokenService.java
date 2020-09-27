package com.source.backend.service;

import com.source.backend.model.Account;
import com.source.backend.model.RefreshToken;
import com.source.backend.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(Account account) {
        Optional<RefreshToken> refreshTokenOptional =refreshTokenRepository.findByAccount(account);
        RefreshToken refreshToken = refreshTokenOptional.orElse(new RefreshToken());
        if ( refreshToken.getAccount() != null){
            return refreshToken;
        }
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        refreshToken.setAccount(account);
        return refreshTokenRepository.save(refreshToken);
    }

    void validateRefreshToken(String token) throws Exception{
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new Exception("Invalid refresh Token"));
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
