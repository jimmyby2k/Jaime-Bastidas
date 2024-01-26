package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {
    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
    }
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public ResponseEntity<AccountResponse> showBalanceForUser(Principal principal) {
        String username = principal.getName();
        Account account = accountDao.showAccountInfoForUser(username);
        if (account != null){
            AccountResponse response = new AccountResponse(username, account.getBalance());
            return ResponseEntity.ok(response);
        } else {
            return null;
        }
    }
    static class AccountResponse{
        private String username;
        private BigDecimal balance;

        AccountResponse(String username, BigDecimal balance){
            this.username =username;
            this.balance = balance;
        }

        public AccountResponse() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponse>> listUser(Principal principal){
        String currentUser = principal.getName();
        List<User> users = userDao.getAllUserExceptCurrentUser(currentUser);

        List<UserResponse> userResponses = new ArrayList<>();
        for (User user: users){
            userResponses.add(new UserResponse(user.getUsername()));
        }
        return ResponseEntity.ok(userResponses);
    }

    static class UserResponse{
        private String username;

        UserResponse(String username){
            this.username = username;
        }

        public String getUsername(){
            return username;
        }
    }
}
