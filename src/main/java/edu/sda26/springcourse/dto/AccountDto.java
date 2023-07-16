package edu.sda26.springcourse.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {
    private Long id;
    private Double balance;
    private Long customerId;
    private Boolean status;
}
