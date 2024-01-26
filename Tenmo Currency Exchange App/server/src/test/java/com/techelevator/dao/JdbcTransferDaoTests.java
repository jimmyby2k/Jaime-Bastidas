package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

public class JdbcTransferDaoTests extends BaseDaoTests{
    private JdbcTransferDao sut;
    private Transfer TRANSFER_1 = new Transfer(1,2,BigDecimal.valueOf(100).setScale(2),"approved");
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void createNewTransfer_return_new_transfer() {
        Transfer testTransfer = new Transfer(3001,1001,1002,BigDecimal.valueOf(100).setScale(2),"approved", "01-01-2011");


        // Perform the create operation and get the new transfer ID
        int newTransferId = sut.create(testTransfer);

       // Retrieve the new transfer to verify its details
        Transfer newTransfer = sut.findTransferById(newTransferId);

        // Assertions
        Assert.assertNotNull(newTransfer);
        Assert.assertEquals(testTransfer.getSenderId(), newTransfer.getSenderId());
        Assert.assertEquals(testTransfer.getReceiverId(), newTransfer.getReceiverId());
        Assert.assertEquals(String.valueOf(testTransfer.getAmount()), newTransfer.getAmount(), BigDecimal.valueOf(100).setScale(2));
        Assert.assertEquals(testTransfer.getStatus(), newTransfer.getStatus());

    }

    @Test
    public void testFindTransferById(){
        Transfer testTransfer = new Transfer(1001,1002,BigDecimal.valueOf(999).setScale(2),"approved");

        int newTransferId = sut.create(testTransfer);

        Transfer transfer = sut.findTransferById(newTransferId);

        Assert.assertNotNull(transfer);
        Assert.assertEquals(newTransferId, transfer.getTransferId());
//        System.out.println("newtransferId : " + newTransferId);
//        System.out.println(transfer.getTransferId());
//        System.out.println("senderId: " + transfer.getSenderId());
//        System.out.println("values " + transfer.getAmount());

    }

    @Test
    public void testFindTransferById_Return_Not_Found_with_Invalid_Id(){
        int transferId = 3005;

        Transfer transfer = sut.findTransferById(transferId);

        Assert.assertNull(transfer);
    }


}
