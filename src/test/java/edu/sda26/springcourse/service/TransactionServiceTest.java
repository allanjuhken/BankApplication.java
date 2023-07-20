package edu.sda26.springcourse.service;

import edu.sda26.springcourse.model.Account;
import edu.sda26.springcourse.model.BalanceDtoResponse;
import edu.sda26.springcourse.model.Transaction;
import edu.sda26.springcourse.model.enums.TransactionType;
import edu.sda26.springcourse.repository.AccountRepository;
import edu.sda26.springcourse.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    //for save transaction, one test for exception and one for save method
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    AccountService accountService;

    @Test

    public void testSaveTransaction_thenResponseSuccessfully(){
        //given
        Transaction transaction = new Transaction(2L,200.00,
                TransactionType.DEPOSIT.name(),2L, LocalDate.now());
        Account account = new Account(2L,500.00,2L,true);
        Account updatedAccount = new Account(2L,300.00,2L,true);
        TransactionService transactionService = new TransactionService(transactionRepository,accountService);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(accountService.save(2L,account)).thenReturn(updatedAccount);

        //when
        BalanceDtoResponse balanceResponse = transactionService.save(transaction,account);

        //Then
        Assertions.assertEquals(balanceResponse.getTransaction().getAmount(),200.00);
        Assertions.assertEquals(balanceResponse.getTransaction().getType(),TransactionType.DEPOSIT.name());
        Assertions.assertEquals(balanceResponse.getTransaction().getAccountId(),2L);
        Assertions.assertNotNull(balanceResponse.getTransaction().getTransactionDate());
        Assertions.assertEquals(balanceResponse.getAccount().getBalance(), 300.00);
    }

    @Test
    public void testSaveTransaction_exception(){
        //given
        Transaction transaction = new Transaction(2L ,2000.0,
                TransactionType.WITHDRAW.name(), 2L, LocalDate.now());
        Account account = new Account(2L,1000.0,2L,true);
        TransactionService transactionService = new TransactionService(transactionRepository,accountService);

        //when
        assertThrows(IllegalStateException.class,() -> transactionService.save(transaction, account));
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(accountService);
    }


}
