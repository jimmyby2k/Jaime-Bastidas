package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private TransferDao transferDao;
    private UserDao userDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, UserDao userDao, AccountDao accountDao){
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public TransferResponse sendTransfer(@Valid @RequestBody TransferDTO transferDto, Principal principal) throws Exception {
        // Find userid of sender
        String sender_usename = principal.getName();
        Account senderAccount = accountDao.showAccountInfoForUser(sender_usename);
        int senderId = userDao.findIdByUsername(sender_usename);

        // find userid of receiver
        String receiver_uname = transferDto.getTo();
        Account receiverAccount = accountDao.showAccountInfoForUser(receiver_uname);
        int receiverId = userDao.findIdByUsername(receiver_uname);

        // Cannot send to yourself
        if (sender_usename.equals(receiver_uname)) {
            throw new Exception("Can not send money to yourself");
//            return new TransferResponse(0, BigDecimal.valueOf(0), sender_usename, receiver_uname);
        }

        // insert to data table
        BigDecimal amount = transferDto.getTransferAmount();
        BigDecimal senderBalance = senderAccount.getBalance();

        // cannot overdraft
        if (amount.compareTo(BigDecimal.valueOf(0)) <= 0 || senderBalance.compareTo(amount) < 0) {
            throw new Exception( "Can not send negative amount or more money than you have");
//            return new TransferResponse(0, BigDecimal.valueOf(0), sender_usename, receiver_uname);
        }

        // add amount to receiver and subtract amount from sender
        accountDao.updateBalance(senderId, amount.negate());
        accountDao.updateBalance(receiverId, amount);

        int transferId = transferDao.create(new Transfer(senderId, receiverId, amount, "Approved"));

        // response
        return new TransferResponse(transferId, amount, sender_usename, receiver_uname);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/transactions", method = RequestMethod.GET)
    public List<Transaction> getTransactions( Principal principal){
        List<TransferResponse> transactions;

        // get Id for sender
        String sender_uname = principal.getName();
        Account senderAccount = accountDao.showAccountInfoForUser(sender_uname);
        int senderId = userDao.findIdByUsername(sender_uname);


        List<Transaction> transfers = transferDao.getTransactions(senderId);

        return transfers;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/transaction/{id}", method = RequestMethod.GET)
    public Transaction getTransactionById(@PathVariable int id, Principal principal){

        return transferDao.getTransaction(id);
    }







    static class TransferRequest{
        private BigDecimal amount;
        private String recipient;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getRecipient() {
            return recipient;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }
    }

    static class TransferResponse{
        private int transferId;
        private BigDecimal transferAmount;
        private String from;
        private String to;

        public TransferResponse(int transferId, BigDecimal transferAmount, String senderId, String receiverId) {
            this.transferId = transferId;
            this.transferAmount = transferAmount;
            this.from = senderId;
            this.to = receiverId;
        }

        public int getTransferId() {
            return transferId;
        }

        public void setTransferId(int transferId) {
            this.transferId = transferId;
        }

        public BigDecimal getTransferAmount() {
            return transferAmount;
        }

        public void setTransferAmount(BigDecimal transferAmount) {
            this.transferAmount = transferAmount;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }
}

