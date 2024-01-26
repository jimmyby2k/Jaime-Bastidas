package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests{

    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }
    
    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }
    
    @Test
    public void getAllUsersExceptCurrentUser() {
        // Arrange
        insertTestUserIntoDatabase("testuser1", "password1");
        insertTestUserIntoDatabase("testuser2", "password2");
        insertTestUserIntoDatabase("testuser3", "password3");

        // Act
        List<User> users = sut.getAllUserExceptCurrentUser("testuser1");

        // Assert
        Assert.assertNotNull(users);
        Assert.assertEquals(4, users.size()); // Assuming there are two other users in the database

   }

    // Helper method to insert a test user into the database
    private void insertTestUserIntoDatabase(String username, String password) {
        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (?, ?)";
        sut.jdbcTemplate.update(sql, username, password);
    }
}
