package com.techelevator;

public class Gum extends Item {


    public Gum(String slot, String name, double price, String category, int quantity) {
        super(slot, name, price, "Gum", quantity);
    }

    @Override
    public void dispenseMessage() {

        System.out.println("Chew Chew, Yum!");

    }
}

