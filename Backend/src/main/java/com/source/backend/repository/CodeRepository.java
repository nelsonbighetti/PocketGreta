package com.source.backend.repository;

import com.source.backend.model.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> {

    public Optional<Code> findCodeByQrcode(String qrcode);
}
