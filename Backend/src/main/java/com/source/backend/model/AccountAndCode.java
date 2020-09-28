package com.source.backend.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Builder;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Data
@Entity
@Getter
@Builder
public class AccountAndCode {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne
    private Code code;

    @ManyToOne
    private Account account;


}
