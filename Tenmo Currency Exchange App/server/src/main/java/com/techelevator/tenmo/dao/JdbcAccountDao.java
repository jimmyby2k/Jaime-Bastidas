package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Account showAccountInfoForUser(String username) {
        String sql = "SELECT account_id, account.user_id, balance FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE tenmo_user.username = ?";

            Account account = new Account();

            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);
            while (result.next()) {
                    Account current = mapRowToAccount(result);
                return current;
            }

        return null;
    }

    @Override
    public List<Account> findAll() {
        return null;
    }

    @Override
    public Account save(Account account) {
        return null;
    }

    @Override
    public void updateBalance(int accountId, BigDecimal newBalance) {
        String sql = "UPDATE account SET balance = balance + ? WHERE user_id = ?";
        jdbcTemplate.update(sql, newBalance, accountId);
    }


    @Override
    public BigDecimal getBalance(int accountId) {

        String sql = "SELECT account.balance " +
                "FROM account " +
                "WHERE user_id = ?;";
        BigDecimal accountBalance = null;   // or new BigDecimal("1")
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()){
            accountBalance = results.getBigDecimal("balance");
        }
        return accountBalance;
    }

    private Account mapRowToAccount(SqlRowSet results){
        Account account = new Account();
        account.setAccountId(results.getInt("account_id"));
        account.setUserId(results.getInt("user_id"));
        account.setBalance(results.getBigDecimal("balance"));
        return account;
    }
}
