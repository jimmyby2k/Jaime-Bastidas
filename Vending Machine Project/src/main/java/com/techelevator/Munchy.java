package com.techelevator;

public class Munchy extends Item {

    public Munchy(String slot, String name, double price, String category, int quantity) {
        super(slot, name, price, "Munchy", quantity);
    }

    @Override
    public void dispenseMessage() {
        System.out.println("Crunch Crunch, Yum!");
    }


}
