package edu.sda26.springcourse.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransactionDto {
    private Double amount;
    private String type;
    private LocalDate transactionDate;
    private Long id;
}
