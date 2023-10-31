package ca.mcgill.ecse321.hotelsystem.service;

import ca.mcgill.ecse321.hotelsystem.Model.Account;
import ca.mcgill.ecse321.hotelsystem.exception.HRSException;
import ca.mcgill.ecse321.hotelsystem.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;


    /**
     * Test get all accounts in the hotel system
     */
    @Test
    public void testGetAllAccounts(){
        String password = "Password123";
        Date dob = Date.valueOf("1990-03-03");
        String address = "435 Snow Hill Road";
        Account a1 = new Account(password, address, dob);

        String password2 = "Safepassword1";
        Date dob2 = Date.valueOf("1995-03-03");
        String address2 = "34 Rainbow Road";
        Account a2 = new Account(password, address, dob);

        List<Account> accounts = new ArrayList<>();
        accounts.add(a1);
        accounts.add(a2);

        when(accountRepository.findAll()).thenReturn(accounts);
        List<Account> output = accountService.getAllAccounts();

        assertEquals(2, output.size());
        Iterator<Account> accountsIterator = accounts.iterator();
        assertEquals(a1, accountsIterator.next());
        assertEquals(a2, accountsIterator.next());
    }

    /**
     * Test getting all accounts when none exist
     */
    @Test
    public void testGetAllEmptyAccounts(){
        List<Account> accounts = new ArrayList<>();
        when(accountRepository.findAll()).thenReturn(accounts);

        HRSException e = assertThrows(HRSException.class, () -> accountService.getAllAccounts());
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There are no accounts in the system.");
    }

    /**
     * Test creating a valid account
     */
    @Test
    public void testCreateValidAccount(){
        String password = "Password123";
        Date dob = Date.valueOf("1990-03-03");
        String address = "435 Snow Hill Road";

        Account response = new Account(password, address, dob);
        when(accountRepository.save(response)).thenReturn(response);

        Account output = accountService.createAccount(response);

        assertNotNull(output);
        assertEquals(response, output);
        verify(accountRepository, times(1)).save(response);
    }

    /**
     * Test creating an invalid account with an empty field
     */
    @Test
    public void testCreateInvalidEmptyAccount(){
        Account a = new Account();
        HRSException e = assertThrows(HRSException.class, () -> accountService.createAccount(a));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Empty field in the account");
    }

    /**
     * Test creating an invalid account with an invalid password
     */
    @Test
    public void testCreateInvalidPasswordAccount(){
        String password = "Password";
        Date dob = Date.valueOf("1990-03-03");
        String address = "435 Snow Hill Road";
        Account a = new Account(password, address, dob);

        HRSException e = assertThrows(HRSException.class, () -> accountService.createAccount(a));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid Password");
    }

    /**
     * Test getting an account with a valid account number
     */
    @Test
    public void testGetAccountByAccountNumber(){
        String password = "Password123";
        Date dob = Date.valueOf("1990-03-03");
        String address = "435 Snow Hill Road";
        Account a = new Account(password, address, dob);

        when(accountRepository.findAccountByAccountNumber(a.getAccountNumber())).thenReturn(a);

        Account output = accountService.getAccountByAccountNumber(a.getAccountNumber());
        assertEquals(output, a);
    }

    /**
     * Test getting an account that does not exist
     */
    @Test
    public void testGetAccountByInvalidNumber(){
        int accountNumber = 1;
        when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(null);

        HRSException e = assertThrows(HRSException.class, () -> accountService.getAccountByAccountNumber(accountNumber));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "Account not found.");
    }

    /**
     * Test updating an account
     */
    @Test
    public void testValidUpdateAccount(){
        String password = "Password123";
        Date dob = Date.valueOf("1990-03-03");
        String address = "435 Snow Hill Road";
        Account a = new Account(password, address, dob);
        when(accountRepository.findAccountByAccountNumber(a.getAccountNumber())).thenReturn(a);

        String password2 = "SaferPassword1";
        Account a2 = new Account(password2, address, dob);

        when(accountRepository.save(a)).thenReturn(a2);
        Account output = accountService.updateAccount(a2);
        assertEquals(output, a2);
    }

    /**
     * Test updating an account that doesn't exist
     */
    @Test
    public void testMissingUpdateAccount(){
        String password = "Password123";
        Date dob = Date.valueOf("1990-03-03");
        String address = "435 Snow Hill Road";
        Account a = new Account(password, address, dob);

        HRSException e = assertThrows(HRSException.class, () -> accountService.updateAccount(a));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "Account not found.");
    }

    /**
     * Test updating an account with invalid info
     */
    @Test
    public void testInvalidInfoUpdateAccount(){
        String password = "Password123";
        Date dob = Date.valueOf("1990-03-03");
        String address = "435 Snow Hill Road";
        Account a = new Account(password, address, dob);
        when(accountRepository.findAccountByAccountNumber(a.getAccountNumber())).thenReturn(a);

        String password2 = "SaferPassword";
        Account a2 = new Account(password2, address, dob);

        HRSException e = assertThrows(HRSException.class, () -> accountService.updateAccount(a2));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(), "Invalid Password");
    }

}
