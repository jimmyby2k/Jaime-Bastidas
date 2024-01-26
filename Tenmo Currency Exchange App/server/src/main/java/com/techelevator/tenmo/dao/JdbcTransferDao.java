package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.controller.TransferController;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.Transaction;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int create(Transfer transfer) {
        Transfer newTransfer = null;
        String sql = "INSERT INTO transfer (sender_id, receiver_id, amount, status) VALUES (?, ?, ?, ?) RETURNING transfer_id";
        int newTransferId = jdbcTemplate.queryForObject(sql, int.class, transfer.getSenderId(), transfer.getReceiverId(), transfer.getAmount(), transfer.getStatus());

        return newTransferId;
    }

    @Override
    public List<Transaction> getTransactions(int userId) {
        String sql = "SELECT t.transfer_id, sender.username AS sender, receiver.username AS receiver, t.amount " +
                "FROM transfer t " +
                "JOIN tenmo_user sender ON t.sender_id = sender.user_id " +
                "JOIN tenmo_user receiver ON t.receiver_id = receiver.user_id " +
                "WHERE t.sender_id = ? OR t.receiver_id = ?" +
                "ORDER BY transfer_id ASC";
        List<Transaction> transfers = new ArrayList<>();
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()){
            Transaction current = mapRowToTransactions (results);
            transfers.add(current);
        }
        return transfers;
    }

    @Override
    public List<Transfer> findTransferByUserId(int userId) {
        return null;
    }

    @Override
    public Transfer findTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, sender_id, receiver_id, amount, status FROM transfer WHERE transfer_id = ?";
        System.out.println(transferId);
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()){
                transfer = mapRowToTransfer(results);
            }
        }  catch (CannotGetJdbcConnectionException e) {
        }
        return transfer;
    }

    @Override
    public Transfer save(Transfer transfer) {
        return null;
    }

    @Override
    public void updateStatus(int transferId, String newStatus) {

    }

    @Override
    public Transaction getTransaction(int transferId) {
        String sql = "SELECT t.transfer_id, sender.username AS sender, receiver.username AS receiver, t.amount " +
                "FROM transfer t " +
                "JOIN tenmo_user sender ON t.sender_id = sender.user_id " +
                "JOIN tenmo_user receiver ON t.receiver_id = receiver.user_id " +
                "WHERE t.transfer_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId);
        if (rowSet.next()){
            return mapRowToTransactions(rowSet);
        }
        throw new UsernameNotFoundException("Transaction " + transferId + " was not found.");
    }

    private Transfer mapRowToTransfer(SqlRowSet results){
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setSenderId(results.getInt("sender_id"));
        transfer.setReceiverId(results.getInt("receiver_id"));
        transfer.setAmount(results.getBigDecimal("amount"));
        transfer.setStatus(results.getString("status"));
        return transfer;

    }

    private Transaction mapRowToTransactions(SqlRowSet results){
        Transaction transaction = new Transaction();
        transaction.setTransferId(results.getInt("transfer_id"));
        transaction.setTransferAmount(results.getBigDecimal("amount"));
        transaction.setFrom(results.getString("sender"));
        transaction.setTo(results.getString("receiver"));
        return transaction;
    }
}
