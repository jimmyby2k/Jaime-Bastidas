package com.techelevator.tenmo.model;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class TransferDTO {
    private BigDecimal transferAmount;
    private String to;

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}

