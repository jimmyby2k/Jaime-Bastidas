package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    Account showAccountInfoForUser(String username);

    List<Account> findAll();
    Account save(Account account);
    void updateBalance(int accountId, BigDecimal newBalance);
    BigDecimal getBalance(int accountId);
}
