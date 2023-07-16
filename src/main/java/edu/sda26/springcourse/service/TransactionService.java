package edu.sda26.springcourse.service;

import edu.sda26.springcourse.dto.TransactionDto;
import edu.sda26.springcourse.model.Account;
import edu.sda26.springcourse.model.Transaction;
import edu.sda26.springcourse.model.enums.TransactionType;
import edu.sda26.springcourse.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public List<TransactionDto> findTransactionsByAccountId(Long accountId) {
        List<Transaction> transactions = transactionRepository.findAllByAccountId(accountId);
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        for(Transaction transaction: transactions) {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setId(transaction.getId());
            transactionDto.setAmount(transaction.getAmount());
            transactionDto.setType(transaction.getType());
            transactionDto.setTransactionDate(transaction.getTransactionDate());
            transactionDtoList.add(transactionDto);
        }
        return transactionDtoList;
    }

    public TransactionDto findTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElse(null);
        if(transaction == null) {
            return null;
        }
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionDate(transaction.getTransactionDate());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setType(transaction.getType());
        return transactionDto;
    }

    public TransactionDto save(Long accountId, TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setType(transactionDto.getType());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionDate(LocalDate.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        TransactionDto savedTransactionDto = new TransactionDto();
        savedTransactionDto.setType(savedTransaction.getType());
        savedTransactionDto.setAmount(savedTransaction.getAmount());
        savedTransactionDto.setTransactionDate(savedTransaction.getTransactionDate());
        savedTransactionDto.setId(savedTransaction.getId());
        return savedTransactionDto;
    }

    public TransactionDto refundTransaction(Long transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElse(null);

        if(transaction == null)
            return null;

        transaction.setType(TransactionType.REFUND.name());
        Transaction savedTransaction = transactionRepository.save(transaction);

        TransactionDto refundedTransactionDto = new TransactionDto();
        refundedTransactionDto.setType(savedTransaction.getType());
        refundedTransactionDto.setAmount(savedTransaction.getAmount());
        refundedTransactionDto.setTransactionDate(savedTransaction
                .getTransactionDate());
        refundedTransactionDto.setId(savedTransaction.getId());

        return refundedTransactionDto;
    }

    public List<TransactionDto> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        for(Transaction transaction: transactions) {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setId(transaction.getId());
            transactionDto.setAmount(transaction.getAmount());
            transactionDto.setType(transaction.getType());
            transactionDto.setTransactionDate(transaction.getTransactionDate());
            transactionDtoList.add(transactionDto);
        }
        return transactionDtoList;
    }

    public Transaction save(Transaction transaction) {
       return transactionRepository.save(transaction);
    }

    public void save(Transaction transaction, Account account) {
        if (transaction.getType().equalsIgnoreCase("Deposit")) {
            transactionRepository.save(transaction);
            calculateAccountBalance(transaction, account, "Deposit");
        } else {
             if (transaction.getAmount() > account.getBalance()) {
                 throw new IllegalStateException("Transaction amount exceeded Account Balance");
             }
             transactionRepository.save(transaction);
             calculateAccountBalance(transaction, account, "Withdraw");
        }
    }


    private void calculateAccountBalance(Transaction transaction, Account account, String type) {
        double total;

        if (type.equalsIgnoreCase("Deposit"))
            total = account.getBalance() + transaction.getAmount();
        else
            total = account.getBalance() - transaction.getAmount();

        account.setBalance(total);
        accountService.save(account.getCustomerId(), account);
    }
}
