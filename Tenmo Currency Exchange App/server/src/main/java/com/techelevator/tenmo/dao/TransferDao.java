package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.Transfer;

import javax.swing.table.TableRowSorter;
import java.util.List;

public interface TransferDao {

    int create(Transfer transfer);

    List<Transaction> getTransactions(int userId);

    List<Transfer> findTransferByUserId(int userId);
    Transfer findTransferById(int transferId);
    Transfer save(Transfer transfer);
    void updateStatus(int transferId, String newStatus);
    Transaction getTransaction(int transferId);
}
