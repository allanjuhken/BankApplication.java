package edu.sda26.springcourse.service;

import edu.sda26.springcourse.exception.AccountNotFoundException;
import edu.sda26.springcourse.model.Account;
import edu.sda26.springcourse.repository.AccountRepository;
import edu.sda26.springcourse.service.AccountService;
import jakarta.persistence.Table;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @SneakyThrows
    @Test
    public void testGetAccountByCustomerId_thenResponseSuccessfully(){
        //given
        Account account = new Account(1L, 200.00,1L,true);
        when(accountRepository.findByCustomerId(anyLong())).thenReturn(account);
        AccountService accountService = new AccountService(accountRepository);

        //when
        Account account1 = accountService.findAccountByCustomerId(1L);

        //then
        assertEquals(1L,account1.getCustomerId());
        assertEquals(200.00, account1.getBalance());
        assertTrue(true);
    }

    //Lets make an example to test account not found on account find by customer id

    @Test
    public void testFindAccountByCustomerId_expectedException(){
        //given
        when(accountRepository.findByCustomerId(anyLong())).thenReturn(null);
        AccountService accountService = new AccountService(accountRepository);

        //when/then
        assertThrows(AccountNotFoundException.class,() -> accountService.findAccountByCustomerId(anyLong()));
        verify(accountRepository).findByCustomerId(anyLong());
    }




}
