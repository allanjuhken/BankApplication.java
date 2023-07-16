package edu.sda26.springcourse;

import edu.sda26.springcourse.model.Account;
import edu.sda26.springcourse.model.Customer;
import edu.sda26.springcourse.model.MyUser;
import edu.sda26.springcourse.model.Transaction;
import edu.sda26.springcourse.model.enums.TransactionType;
import edu.sda26.springcourse.repository.AccountRepository;
import edu.sda26.springcourse.repository.CustomerRepository;
import edu.sda26.springcourse.repository.MyUserRepository;
import edu.sda26.springcourse.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

@Profile("dev")
@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
public class SpringCourseApplication implements CommandLineRunner {


	@Value("(${sda26.description})")
	String description;

	@Value("(${sda26.name})")
	String name;

	@Value("(${sda26.message})")
	String message;

	/************************** Injection using Field************************************/
	@Autowired
	private AccountRepository accountRepository;

	/************************** Injection using constructor*******************************/
	private final CustomerRepository customerRepository;
	private final MyUserRepository myUserRepository;

	public SpringCourseApplication(CustomerRepository customerRepository,
								   MyUserRepository myUserRepository) {
		this.customerRepository = customerRepository;
		this.myUserRepository = myUserRepository;
	}

	/************************** Injection using Setter************************************/
	private TransactionRepository transactionRepository;

	@Autowired
	public void setTransactionRepository(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}
	/*************************************************************************************/


	public static void main(String[] args) {
		SpringApplication.run(SpringCourseApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("Active profile is:" + message);


		System.out.println("My SDA 26 description value is: " + description);
		System.out.println("My SDA 26 Name value is: " +  name);

		Customer customer1 = createCustomer(20, "John", "+372444333755", "John@Doe.com", true);
		Customer customer2 = createCustomer(25, "Jane", "+37222222222", "Jane@Doe.com", true);
		Customer customer3 = createCustomer(30, "John", "+372411111111", "John2@Doe.com", false);
		Customer customer4 = createCustomer(32, "John", "+372344000000", "John3@Doe.com", true);

		Account account1 = createAccount(200.0 ,customer1.getId());
		Account account2 = createAccount(100.0 ,customer2.getId());

		Transaction savedTransaction = createTransaction(account1, 200.0, TransactionType.DEPOSIT);
		Transaction savedTransaction2 = createTransaction(account1, 250.0, TransactionType.DEPOSIT);
		Transaction savedTransaction3 = createTransaction(account2, 250.0, TransactionType.DEPOSIT);

		System.out.println("Customer 1 is created: " + customer1);
		System.out.println("Customer 2 is created: " + customer2);
		System.out.println("Account is created: " + account1);
		System.out.println("Transaction is created: " + savedTransaction);

		// lets
		// 1. find all customers
		// 2. lets find customer by id = 2
		// 3. lets delete customer id = 2

		System.out.println("List of all customers: " + customerRepository.findAll());
		System.out.println("Customer with id 2: " + customerRepository.findById(2L).orElse(null));
		System.out.println("Delete customer with id 2: ");
//		customerRepository.deleteById(2L);
		System.out.println("Customer with id 2 deleted Successfully");
		System.out.println("List of all customers: " + customerRepository.findAll());

		// Lets do the same this time for account
		// 1. find all accounts
		// 2. lets find account by id = 2
		// 3. lets delete account id = 2
		System.out.println("List of all accounts: " + accountRepository.findAll());
		System.out.println("Account with id 2: " + accountRepository.findById(2L).orElse(null));
		System.out.println("Delete account with id 2: ");
//		accountRepository.deleteById(2L);
		System.out.println("Account with id 2 deleted Successfully");
		System.out.println("List of all accounts: " + accountRepository.findAll());


		MyUser admin1 = new MyUser(3L,"admin1", "admin1@gmail.com","123", "ROLE_ADMIN");

		myUserRepository.save(admin1);

	}

	private Transaction createTransaction(Account account1, Double amount,
										  TransactionType type) {
		Transaction transaction = new Transaction();
		transaction.setTransactionDate(LocalDate.now());
		transaction.setAmount(amount);
		transaction
				.setType(type.toString());
		transaction
				.setAccountId(account1.getId());

		return transactionRepository.save(transaction);
	}

	private Account createAccount(Double balance, Long customerId) {
		Account account = Account.builder()
				.balance(balance)
				.customerId(customerId)
				.status(true)
				.build();

//		Account account = new Account();
//		account.setBalance(balance);
//		account.setCustomerId(customerId);

		return accountRepository.save(account);
	}

	private Customer createCustomer(int age, String name, String phone, String email, Boolean isActive) {
		Customer c = Customer.builder()
				.age(age)
				.active(isActive)
				.email(email)
				.name(name)
				.phone(phone)
				.build();

		return customerRepository.save(c);
	}
}
