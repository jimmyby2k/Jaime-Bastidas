package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private int senderId;
    private int receiverId;
    private BigDecimal amount;
    private String status;

    private String createdAt;

    public Transfer() {
    }

    public Transfer(int transferId, int senderId, int receiverId, BigDecimal amount, String status, String createdAt) {
        this.transferId = transferId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.status = status;
    }

    public Transfer(int senderId, int receiverId, BigDecimal amount, String status) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.status = status;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
