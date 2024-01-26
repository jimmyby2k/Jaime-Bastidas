package com.techelevator;

import com.sun.tools.javac.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;


/*
 * This class is provided to you as a *suggested* class to start
 * your project. Feel free to refactor this code as you see fit.
 */
public class VendingMachineCLI {

    Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        VendingMachineCLI cli = new VendingMachineCLI();


        cli.run();
    }


    public void run() {

        moneyToChange(8.5);


        VendingMachine vendingMachine = new VendingMachine(0, "main.csv");
        List<Item> inventory = vendingMachine.loadSnacksFromInventory();
        File log = new File("log.txt");
        Date date = Date.from(Instant.now());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");


        //First time the menu shows
        int mainMenuOption = 0;
        //1

        //Check the chosen number
        //If the number is NOT 3, repeat, because when it's 3 we want to exit, we don't want it to repeat when 3
        while (mainMenuOption != 3) {

            showMainMenu();

            try {
                mainMenuOption = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid option, please enter a valid number");
                mainMenuOption = 0;
            }
            input.nextLine();

            //If the option is 1
            if (mainMenuOption == 1) {

                System.out.println();
                showInventory(inventory);
                System.out.println();


            } else if (mainMenuOption == 2) {

                DecimalFormat format = new DecimalFormat("$#.00");

                mainMenuOption = 0;

                System.out.println("Current Money Provided: " + format.format(vendingMachine.getBalance()));

                showPurchaseMenu();

                int subMenuOption;

                try {
                    subMenuOption = input.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid option, please enter a valid number");
                    subMenuOption = 0;
                }
                input.nextLine();

                int bogoCounter = 1;

                while (subMenuOption != 3) {


                    if (subMenuOption == 1) {

                        System.out.println("Current Money Provided: " + format.format(vendingMachine.getBalance()));

                        String moneyFeedMenu = "y";
                        while (moneyFeedMenu.equals("y")) {
                            System.out.println("Feed money in whole dollars amount: ");

                            int money;

                            try {
                                money = input.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter money value in whole number digits");
                                money = 0;
                            }
                            input.nextLine();

                            vendingMachine.addMoney(money);


                            try (PrintWriter dataOutput = new PrintWriter(
                                    // Passing true to the FileOutputStream constructor says to append
                                    new FileOutputStream(log, true)
                            )) {

                                dataOutput.println(simpleDateFormat.format(date) + " FEED MONEY: " + format.format(money) + " " + format.format(vendingMachine.getBalance()));


                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }


                            System.out.println("Current Money Provided: " + format.format(vendingMachine.getBalance()));
                            System.out.println("Continue adding money? (y/n)");
                            moneyFeedMenu = String.valueOf(input.next().charAt(0)).toLowerCase();
                        }
                        System.out.println("Current Money Provided: " + format.format(vendingMachine.getBalance()));

                    } else if (subMenuOption == 2) {


                        showInventory(inventory);
                        System.out.println();
                        System.out.println("Current Balance is " + format.format(vendingMachine.getBalance()));
                        System.out.println("Choose an item");
                        String itemChoice = input.next().toUpperCase();



                        if (vendingMachine.getMap().containsKey(itemChoice)) {


                            int chosenItemIndex = vendingMachine.getMap().get(itemChoice);
                            Item chosenItem = inventory.get(chosenItemIndex);

                            if (vendingMachine.getBalance() >= chosenItem.getPrice()) {

                                double price = chosenItem.getPrice();
                                String message;

                                if (bogoCounter % 2 == 0){

                                    price -= 1.00;
                                    message = chosenItem.getName() + " gets BOGODO discount a dollar off! " + format.format(price);

                                } else {

                                    message = chosenItem.getName() + " " + format.format(price);
                                    price = chosenItem.getPrice();
                                }

                                bogoCounter++;

                                if (chosenItem.getQuantity() > 0){



                                    vendingMachine.subtractMoney(price);

                                    System.out.println(message);



                                    chosenItem.dispenseMessage();
                                    chosenItem.removeOneFromQuantity();



                                    try (PrintWriter dataOutput = new PrintWriter(
                                            // Passing true to the FileOutputStream constructor says to append
                                            new FileOutputStream(log, true)
                                    )) {

                                        dataOutput.println(simpleDateFormat.format(date) +
                                                " " +
                                                chosenItem.getName() +
                                                " " +
                                                chosenItem.getSlot() +
                                                " " +
                                                format.format(chosenItem.getPrice()) +
                                                " " +
                                                format.format(vendingMachine.getBalance()));


                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }



                                } else {
                                    System.out.println("Product currently sold out");
                                }


                            } else {

                                System.out.println("Not enough money in balance for this item");

                            }

                        } else {
                            System.out.println("Invalid slot selection");
                        }

                        if (vendingMachine.getBalance() > 0) {
                            System.out.println("Current Balance is " + format.format(vendingMachine.getBalance()));
                            System.out.println();
                        }

                    }


                    showPurchaseMenu();

                    try {
                        subMenuOption = input.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid option, please enter a valid number");
                        subMenuOption = 0;
                    }
                    input.nextLine();


                }

                if (vendingMachine.getBalance() > 0) {



                    try (PrintWriter dataOutput = new PrintWriter(
                            // Passing true to the FileOutputStream constructor says to append
                            new FileOutputStream(log, true)
                    )) {

                        dataOutput.println(simpleDateFormat.format(date) + " GIVE CHANGE: " + format.format(vendingMachine.getBalance()) + " " + "$0.00");


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    System.out.println(moneyToChange(vendingMachine.getBalance()));
                    vendingMachine.subtractMoney(vendingMachine.getBalance());

                }


            }
        }
    }


    public String moneyToChange(double currentBalance) {

        double moneyValue = currentBalance * 100;

        int dollars = (int) moneyValue / 100;
        double dollarRemainder = moneyValue % 100;

        dollarRemainder = Math.ceil(dollarRemainder);

        int quarters = (int) dollarRemainder / 25 + (dollars * 4);
        double quarterRemainder = dollarRemainder % 25;
        int dimes = (int) quarterRemainder / 10;
        double dimesRemainder = quarterRemainder % 10;
        int nickles = (int) dimesRemainder / 5;

        StringBuilder sb = new StringBuilder();

        sb.append("You change is ");

        if (quarters > 0) {
            if (quarters == 1) {
                sb.append(quarters + " " + "quarter" + ", ");
            } else {
                sb.append(quarters + " " + "quarters" + ", ");
            }
        }

        if (dimes > 0) {
            if (dimes == 1) {
                sb.append(dimes + " " + "dime" + ", ");
            } else {
                sb.append(dimes + " " + "dimes" + ", ");
            }
        }

        if (nickles > 0) {
            sb.append(nickles + " " + "nickel");
        }

        return sb.toString();

    }

    public void showInventory(List<Item> inventory) {

        System.out.printf("%-5s %-16s %-9s %-1s%n", "Slot", "Product", "Price", "Quantity");

        for (Item item : inventory) {

                System.out.println(item);

        }

    }


    public void showMainMenu() {

        System.out.println("(1) Display Vending Machine Items");
        System.out.println("(2) Purchase");
        System.out.println("(3) Exit");
        System.out.print("Enter 1 2 or 3 ");
        System.out.println();

    }

    public void showPurchaseMenu() {

        System.out.println("(1) Feed Money");
        System.out.println("(2) Select Product");
        System.out.println("(3) Finish Transaction");
        System.out.print("Enter 1 2 or 3 ");
        System.out.println();

    }

}
