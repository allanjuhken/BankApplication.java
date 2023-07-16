package edu.sda26.springcourse.service;

import edu.sda26.springcourse.dto.AccountDto;
import edu.sda26.springcourse.exception.AccountNotFoundException;
import edu.sda26.springcourse.model.Account;
import edu.sda26.springcourse.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountDto> accountDtos = new ArrayList<>();

        for (Account account : accounts) {
            accountDtos.add(toAccountDto(account));
        }
        return accountDtos;
    }

    public Account findAccountById(Long id) throws AccountNotFoundException {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account Not found", 2));
    }

    public List<Account> findAccountWithPositiveBalance() {
        // todo replace with better code in next sessions
        // 1. using repository method
        // 2. using Query annotation

        // using repository method
        return accountRepository.findAllByBalanceGreaterThan(0.0);

//        return accountRepository.findAll()
//                .stream().filter(account -> account.getBalance() > 0)
//                .collect(Collectors.toList());
    }

    public Account findAccountByCustomerId(Long customerId) throws AccountNotFoundException {
        // todo replace with better code in next sessions
        // 1. using repository method
        // 2. using Query annotation

        // Using repository method
         Account account = accountRepository.findByCustomerId(customerId);
         if (account==null)
             throw new AccountNotFoundException("Account Not found", 2);

         return account;
//        return accountRepository.findAll()
//                .stream().filter(account -> account.getCustomerId().equals(customerId))
//                .findAny()
//                .orElse(null);
    }

    public List<Account> getAllAccountsWithBalanceBetweenMinAndMax
            (Double min, Double max) {
        // todo replace with better code in next sessions
        // 1. using repository method
        // 2. using Query annotation

        return accountRepository
                .findAllByBalanceBetween(min, max);

        // return accountRepository
        // .findAllByBalanceGreaterThanAndBalanceLessThan()

//        return accountRepository.findAll().stream()
//                .filter(account ->
//                        account.getBalance() > min
//                                && account.getBalance() < max)
//                .toList();
    }

    public AccountDto save(Long customerId, AccountDto accountDto) {
        Account account = toAccount(customerId, accountDto);

        Account savedAccount = accountRepository.save(account);

        return toAccountDto(savedAccount);
    }

    public Account save(Long customerId, Account account) {
        account.setCustomerId(customerId);
        return accountRepository.save(account);
    }


    private Account toAccount(Long customerId, AccountDto accountDto) {
        Account account = new Account();
        account.setBalance(accountDto.getBalance());
        account.setCustomerId(customerId);
        return account;
    }

    public void removeAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    public AccountDto update(Long customerId, Long accountId,
                             AccountDto accountDto) {
        Account account = toAccount(customerId, accountId, accountDto);
        Account updatedAccount = accountRepository.save(account);
        return toAccountDto(updatedAccount);
    }

    private Account toAccount(Long customerId,
                              Long accountId,
                              AccountDto accountDto) {
        Account account = new Account();
        account.setBalance(accountDto.getBalance());
        account.setCustomerId(customerId);
        account.setId(accountId);
        return account;
    }

    private AccountDto toAccountDto(Account savedAccount) {
        AccountDto savedAccountDto = new AccountDto();
        savedAccountDto.setBalance(savedAccount.getBalance());
        savedAccountDto.setCustomerId(savedAccount.getCustomerId());
        savedAccountDto.setId(savedAccount.getId());
        savedAccountDto.setStatus(savedAccount.getStatus());
        return savedAccountDto;
    }

    public AccountDto updateAccountStatus(Long id) {
        Account account = accountRepository.findById(id).orElse(null);

        if (account == null)
            return null;

        account.setStatus(true);
        return toAccountDto(accountRepository.save(account));
    }


}
