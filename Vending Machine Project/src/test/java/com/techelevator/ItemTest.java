package com.techelevator;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class ItemTest {


    private Item item;

    @Before
    public void setup()  {
        this.item = new Drink("A2", "Gingen Ayle", 1.85, "Drink", 5);
    }

    @Test
    public void removeOneFromQuantity(){

        int expected = 4;

        item.removeOneFromQuantity();

        assertEquals(expected, item.getQuantity());


    }


}
