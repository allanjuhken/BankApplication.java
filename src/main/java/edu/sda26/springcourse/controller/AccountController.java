package edu.sda26.springcourse.controller;

import edu.sda26.springcourse.dto.AccountDto;
import edu.sda26.springcourse.exception.AccountNotFoundException;
import edu.sda26.springcourse.exception.CustomerNotFoundException;
import edu.sda26.springcourse.model.Account;
import edu.sda26.springcourse.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/accounts")
    public List<AccountDto> getAllAccount() {
        return accountService.getAllAccounts();
    }


    @GetMapping(path = "/account")
    public ResponseEntity<List<Account>> getAccountsWithBalanceBetweenMinAndMax(@RequestParam("min") Double min,
                                                                                @RequestParam("max") Double max) {
        List<Account> accounts = accountService
                .getAllAccountsWithBalanceBetweenMinAndMax(min, max);
        if (accounts.size() < 1)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(accounts);
    }

    @GetMapping(path = "/account/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable("id") Long id) throws AccountNotFoundException {
        Account account = accountService.findAccountById(id);
        return ResponseEntity.ok(account);
    }

    @GetMapping(path = "/account-with-positive-balance")
    public List<Account> getAccountsWithPositiveBalance() {
        return accountService.findAccountWithPositiveBalance();
    }

   @PostMapping(path = "/customer/{id}/account")
   public ResponseEntity<AccountDto> createAccount(@PathVariable("id") Long customerId,
                                                @RequestBody AccountDto account) {

         AccountDto accountDto = accountService.save(customerId, account);
         return ResponseEntity.ok(accountDto);
   }



   @DeleteMapping(path = "/account/{id}")
   public ResponseEntity<String> removeAccount(@PathVariable("id") Long accountId) {
       accountService.removeAccount(accountId);
       return ResponseEntity.ok("Account with ID: " + accountId + " deleted successfully");
   }

   // In order to implement an api you need to follow these steps:
    // 1. Understand the requirement completely
    // 2. Decide what type of rest api you need: (Get? Post? Put? Delete? Patch?)
    // 3. Try to understand what is the api input( What you have? @PathVariable, @RequestParam, @RequestBody) and output and what is the http status for response
    // 4. Then implement the dto that you need
    // 5. Implement the Controller and inside the controller call service class method
    // 6. Implement the service class method and call repository method there
    // 7. If needed implement repository method ether by @Query annotation or already defined repository method

    @PatchMapping(path = "/account/{id}/activate")
    public ResponseEntity<AccountDto> updateAccountStatus(@PathVariable("id") Long id) {
        return ResponseEntity.accepted().body(accountService.updateAccountStatus(id));
    }


    @PutMapping(path = "/customer/{customerId}/account/{accountId}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable("customerId") Long customerId,
                                                    @PathVariable("accountId") Long accountId,
                                                    @RequestBody AccountDto accountDto) {
        return ResponseEntity.accepted().body(
                accountService.update(customerId, accountId, accountDto));
    }

}
