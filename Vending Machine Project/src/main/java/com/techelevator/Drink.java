package com.techelevator;

public class Drink extends Item {

    public Drink(String slot, String name, double price, String category, int quantity) {
        super(slot, name, price, "Drink", quantity);

    }

    @Override
    public void dispenseMessage() {
        System.out.println("Glug Glug, Yum!");
    }


}
