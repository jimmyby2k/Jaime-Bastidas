package com.techelevator;

public class Candy extends Item {
    public Candy(String slot, String name, double price, String category, int quantity) {
        super(slot, name, price, "Candy", quantity);
    }

    @Override
    public void dispenseMessage() {
        System.out.println("Yummy Yummy, So Sweet!");
    }
}
