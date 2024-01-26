package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class VendingMachine {

    public VendingMachine(double balance, String inventoryFileName) {
        this.balance = balance;
        this.inventoryFileName = inventoryFileName;
    }

    private double balance;
    String inventoryFileName;
    private HashMap<String, Integer> map = new HashMap<>();

    public double getBalance() {
        return balance;
    }

    public HashMap<String, Integer> getMap() {
        return map;
    }

    public void addMoney(double insertedMoney) {

        if (insertedMoney > 0){
            balance += insertedMoney;
        }

    }

    public void subtractMoney(double itemPrice) {


        balance -= itemPrice;
    }

    public List<Item> loadSnacksFromInventory() {
        List<Item> result = new ArrayList<>();
        File input = new File(inventoryFileName);

        int listIndex = 0;

        if (input.exists() && input.isFile()) {
            try (Scanner inputScanner = new Scanner(input)) {
                while (inputScanner.hasNextLine()) {
                    String currentLine = inputScanner.nextLine();

                    String[] splitValues = currentLine.split(",");

                    String slot = splitValues[0];
                    String name = splitValues[1];
                    String price = splitValues[2];
                    double priceDouble = Double.parseDouble(price);
                    String category = splitValues[3];
                    int quantity = 5;

                    Item current;

                    if (category.equals("Gum")) {
                        current = new Gum(slot, name, priceDouble, category, quantity);

                    } else if (category.equals("Drink")) {
                        current = new Drink(slot, name, priceDouble, category, quantity);

                    } else if (category.equals("Candy")) {
                        current = new Candy(slot, name, priceDouble, category, quantity);

                    } else {
                        current = new Munchy(slot, name, priceDouble, category, quantity);
                    }

                    result.add(current);

                    map.put(slot, listIndex);
                    listIndex++;

                }

            } catch(FileNotFoundException e){
                System.out.println("Something went wrong");
            }

        }
        return result;
    }

}
