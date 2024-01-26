package com.techelevator.dao;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;


public class JdbcAccountDaoTests extends BaseDaoTests{
    private Account ACCOUNT_1 = new Account(2001,1003,BigDecimal.valueOf(1000.00).setScale(2));
    private Account ACCOUNT_2 = new Account(2002,1004,BigDecimal.valueOf(1000.00).setScale(2));
    private static final Account ACCOUNT_5 = new Account(2006, 1001, new BigDecimal ("1000.00"));

    private JdbcAccountDao sut;


    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void showAccountInfoForUser_return_correct_account() {
        JdbcUserDao userDao = new JdbcUserDao(new JdbcTemplate(dataSource));
        boolean userCreated = userDao.create("TEST_USER","test_password");
        if (userCreated){
            Account test = sut.showAccountInfoForUser("TEST_USER");
            assertAccountMatch(test, ACCOUNT_2);
        }
    }
    @Test
    public void updateBalance_return_correct_balance(){
        JdbcUserDao userDao = new JdbcUserDao(new JdbcTemplate(dataSource));
        boolean userCreated = userDao.create("TEST_USER_2","test_password");
        if (userCreated){
            Account test = sut.showAccountInfoForUser("TEST_USER_2");
            int accountId = test.getAccountId();
            System.out.println(accountId);
            sut.updateBalance(accountId,BigDecimal.valueOf(100));
            assertAccountMatch(test,ACCOUNT_1);
        }
    }
    private void assertAccountMatch(Account expected, Account actual) {
        Assert.assertEquals("",expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals("",expected.getUserId(), actual.getUserId());
        Assert.assertEquals("",expected.getBalance(), actual.getBalance());
    }
    @Test
    public void getAccount(){
         Account account = sut.showAccountInfoForUser("bob");
        Assert.assertNotNull(account);
        Assert.assertEquals(ACCOUNT_5,account);
    }

    @Test
    public void updateBalance() {
        BigDecimal newBalance = new BigDecimal("1000");
        sut.updateBalance(1002, newBalance);
        BigDecimal expectedBalance = sut.getBalance(1002);
        Account account = sut.showAccountInfoForUser("user");
        Assert.assertEquals(expectedBalance, account.getBalance());
    }

}

