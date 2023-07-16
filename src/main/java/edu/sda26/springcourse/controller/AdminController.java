package edu.sda26.springcourse.controller;

import edu.sda26.springcourse.dto.AccountDto;
import edu.sda26.springcourse.dto.CustomerDto;
import edu.sda26.springcourse.dto.TransactionDto;
import edu.sda26.springcourse.exception.AccountNotFoundException;
import edu.sda26.springcourse.model.Account;
import edu.sda26.springcourse.model.Customer;
import edu.sda26.springcourse.model.Transaction;
import edu.sda26.springcourse.service.AccountService;
import edu.sda26.springcourse.service.CustomerService;
import edu.sda26.springcourse.service.TransactionService;
//import jakarta.annotation.security.RolesAllowed;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {

    private final CustomerService customerService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public AdminController(CustomerService customerService,
                           AccountService accountService,
                           TransactionService transactionService) {
        this.customerService = customerService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/")
    public String getCustomers(final ModelMap modelMap) {
        List<CustomerDto> customerList = customerService.getAllCustomers();
        modelMap.addAttribute("customerDtoList", customerList);
        return "index";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/admin/accounts")
    public String getAccounts(final ModelMap modelMap) {
        List<AccountDto> accountList = accountService.getAllAccounts();
        modelMap.addAttribute("sda26AccountObject", accountList);
        return "account";
    }

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/admin/transactions")
    public String getTransactions(final ModelMap modelMap) {
        List<TransactionDto> transactionDtoList = transactionService.getAllTransactions();
        modelMap.addAttribute("transactionDtoList", transactionDtoList);
        return "transaction";
    }

    @GetMapping("/admin/customer/{id}/accounts")
    public String getAccountByCustomer(final ModelMap modelMap,
                                       @PathVariable("id") Long id)
            throws AccountNotFoundException {
        Account account =
                accountService.findAccountByCustomerId(id);
        modelMap.addAttribute("account", account);
        return "customer-account";
    }

    @GetMapping("/admin/customer/{id}/account")
    public String showCreateAccountPage(final ModelMap modelMap,
                                       @PathVariable("id") Long id) {

        Account account = null;
        try {
            account = accountService.findAccountByCustomerId(id);
        } catch (AccountNotFoundException ignored) {
        }

        if(account != null)
            return "internal-error";

        AccountDto accountDto =  new AccountDto();
        accountDto.setCustomerId(id);
        modelMap.addAttribute("accountDto", accountDto);
        return "create-account";
    }


    @GetMapping("/admin/customer/{id}/account/transactions")
    public String getTransactionsByCustomer(final ModelMap modelMap,
                                            @PathVariable("id") Long id)
            throws AccountNotFoundException {

        Account account =
                accountService.findAccountByCustomerId(id);

        List<TransactionDto> transactionDtoList =
               transactionService.findTransactionsByAccountId(account.getId());

        modelMap.addAttribute("transactionDtoList", transactionDtoList);

        return "transaction";
    }


    //Create customer
    @GetMapping("/admin/customer/create")
    public String showCreateCustomerForm(ModelMap modelMap) {
        Customer customer = new Customer();
        modelMap.addAttribute("customer", customer);
        return "create-customer";
    }

    @PostMapping("/admin/customer")
    public String createCustomer(@ModelAttribute("customer")
                                             Customer customer) {
        customer.setActive(true);
        customerService.save(customer);
        return "redirect:/";
    }


    // Create Account
    @GetMapping("/admin/account/create")
    public String showCreateAccountForm(ModelMap modelMap) {
        AccountDto accountDto =  new AccountDto();
        modelMap.addAttribute("accountDto", accountDto);
        return "create-account";
    }

    @PostMapping("/admin/accounts")
    public String createAccount(@ModelAttribute("accountDto")
                                         AccountDto accountDto) {
        accountService.save(accountDto.getCustomerId(), accountDto);
        return "redirect:/admin/accounts";
    }

    @GetMapping("/admin/customer/{id}/account/transaction")
    public String showCreateTransaction(ModelMap modelMap,
                                        @PathVariable("id") Long customerId) throws AccountNotFoundException {

        Transaction transaction = new Transaction();
        Account account = accountService.findAccountByCustomerId(customerId);
        transaction.setAccountId(account.getId());
        modelMap.addAttribute("transaction", transaction);
        return "create-transaction";
    }



    @PostMapping("/admin/transaction/create")
    public String createTransaction(@ModelAttribute("transaction")
                                               Transaction transaction)
            throws AccountNotFoundException {
        Account account = accountService.findAccountById(transaction.getAccountId());
        transactionService.save(transaction, account);

        return "redirect:/";
    }

    @GetMapping(value = "/admin/transaction/create")
    public String showCreateTransactionForm(ModelMap modelMap) {
        Transaction transaction = new Transaction();
        modelMap.addAttribute("transaction", transaction);
        return "create-transaction";
    }



}
