package edu.sda26.springcourse.controller;

import edu.sda26.springcourse.dto.TransactionDto;
import edu.sda26.springcourse.service.TransactionService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(path = "/account/{id}/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccount(@PathVariable("id") Long accountId) {
        List<TransactionDto> transactionDtoList =
                transactionService.findTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactionDtoList);
    }


    @GetMapping(path = "/transaction/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable("id") Long transactionId) {
        TransactionDto transactionDto =
                transactionService.findTransactionById(transactionId);

        return ResponseEntity.ok(transactionDto);
    }

    @PostMapping(path = "/account/{id}/transaction")
    public ResponseEntity<TransactionDto> createTransaction(@PathVariable("id") Long accountId,
                                                            @RequestBody TransactionDto transactionDto) {
        TransactionDto savedTransactionDto = transactionService.save(
                accountId, transactionDto);
        return ResponseEntity.ok(savedTransactionDto);
    }

    @PatchMapping(path = "/transaction/{id}/refund")
    public ResponseEntity<TransactionDto> refundTransaction(
            @PathVariable("id") Long transactionId) {

        TransactionDto transaction = transactionService
                .refundTransaction(transactionId);
        return ResponseEntity.accepted().body(transaction);
    }



}
