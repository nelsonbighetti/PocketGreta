package com.source.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.Instant;

@Data
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountCode {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Account account;

    @OneToOne
    private Code code;


    private Instant date;

}
