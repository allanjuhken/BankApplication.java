package edu.sda26.springcourse.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Transaction {

    // 3. Transaction entity that has amount, transaction type and transaction date
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private String type;

    private Long accountId;

    //Instant or LocalDate
    private LocalDate transactionDate;

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", accountId=" + accountId +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
