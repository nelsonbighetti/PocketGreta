package com.source.backend.endpoint;


import com.source.backend.Dto.AuthenticationResponse;
import com.source.backend.Dto.LoginRequest;
import com.source.backend.Dto.RefreshTokenRequest;
import com.source.backend.Dto.RegisterRequest;
import com.source.backend.model.RefreshToken;
import com.source.backend.repository.RefreshTokenRepository;
import com.source.backend.service.AuthorizationService;
import com.source.backend.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;
@RestController
@RequestMapping("/rest/auth")
@AllArgsConstructor
public class AuthorizationEndpoint {

    private final AuthorizationService authService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Successful",
                OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthenticationResponse authenticationResponse = authService.login(loginRequest);
            return new ResponseEntity<>(authenticationResponse,OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<AuthenticationResponse> refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken());
            RefreshToken refreshToken = refreshTokenOpt.orElseThrow(() -> new UsernameNotFoundException("No RefreshToken " +
                    "Found with Token : " + refreshTokenRequest.getRefreshToken()));
            if(refreshTokenRequest.getUsername().equals(refreshToken.getAccount().getUsername())) {
                AuthenticationResponse authenticationResponse = authService.refreshToken(refreshTokenRequest);
                return new ResponseEntity<>(authenticationResponse, OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }

}
