package com.source.backend.model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Builder;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;


@Data
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountAndCode {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne
    private Code code;

    @ManyToOne
    private Account account;
    
    private Instant date;

    private int bonuses;

}
