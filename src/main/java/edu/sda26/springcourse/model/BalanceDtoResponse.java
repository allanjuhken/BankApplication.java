package edu.sda26.springcourse.model;

import jakarta.persistence.Entity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Builder
public class BalanceDtoResponse {

    Transaction transaction;
    Account account;

}
