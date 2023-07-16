package edu.sda26.springcourse.model;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Account {
    // 2. Account entity that has balance and list of transactions and its assigned to a customer
    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Long id;

    private Double balance;

    private Long customerId;

    private Boolean status;
}
