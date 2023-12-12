package com.pluralsight;
import java.sql.*;
import java.util.Scanner;
import org.apache.commons.dbcp2.BasicDataSource;

public class MainApp {
    public static Scanner scan = new Scanner(System.in);
    public static String username, password;
    public static BasicDataSource dataSource = new BasicDataSource();

    public static void main(String[] args) {
        if(args.length != 2){
            login();
        }
        else{
            username = args[0];
            dataSource.setUsername(username);
            password = args[1];
            dataSource.setPassword(password);
        }
        start();
    }

    public static void login() {
        System.out.print("User: ");
        username = scan.nextLine();
        dataSource.setUsername(username);
        System.out.print("Pass: ");
        password = scan.nextLine();
        dataSource.setPassword(password);
    }

    public static void start() {
        Connection conn = null;
        PreparedStatement prepState = null;
        ResultSet rSet = null;

        try {
            String query;
            dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
            conn = dataSource.getConnection();
            System.out.print("What do you want to do?\n\t1) Display All Products\n\t2) Display All Customers\n\t3) Display All Categories\n\t0) Exit\nSelect An Option: ");
            String choice = scan.nextLine();
            switch (choice) {
                case "0":
                    System.out.println("\nThank you, have a great day!");
                    System.exit(1);
                case "1":
                    query = "SELECT * FROM products";
                    prepState = conn.prepareStatement(query);
                    rSet = prepState.executeQuery(query);
                    System.out.println("\nList of All Products: ");
                    while (rSet.next()) {
                        String productInfo = rSet.getString("ProductID") + ") " + rSet.getString("ProductName");
                        System.out.println(productInfo);
                    }
                    System.out.println("");
                    start();
                    break;
                case "2":
                    int temp = 1;
                    query = "SELECT ContactName, CompanyName, City, Country, Phone FROM Customers ORDER BY Country";
                    prepState = conn.prepareStatement(query);
                    rSet = prepState.executeQuery(query);
                    System.out.println("\nList of All Customers: ");
                    while (rSet.next()) {
                        String customerInfo = temp + ") "
                                + rSet.getString("ContactName")
                                + ", " + rSet.getString("CompanyName")
                                + ", " + rSet.getString("City")
                                + ", " + rSet.getString("Country")
                                + ", " + rSet.getString("Phone");
                        System.out.println(customerInfo);
                        temp++;
                    }
                    System.out.println("");
                    start();
                    break;
                case "3":
                    query = "SELECT CategoryID, CategoryName FROM categories";
                    prepState = conn.prepareStatement(query);
                    rSet = prepState.executeQuery(query);
                    System.out.println("\nList of All Categories: ");
                    while(rSet.next()) {
                        String categoryInfo = rSet.getString("CategoryID") + ") " + rSet.getString("CategoryName");
                        System.out.println(categoryInfo);
                    }
                    System.out.print("Input The ID of a Category to Display All Products of: ");
                    Integer categoryChoice = Integer.parseInt(scan.nextLine());
                    query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products WHERE CategoryID = " + categoryChoice;
                    prepState = conn.prepareStatement(query);
                    rSet = prepState.executeQuery(query);
                    System.out.println("\nList of All Products: ");
                    while(rSet.next()) {
                        String categoryInfo = rSet.getString("ProductID") + ") "
                                + rSet.getString("ProductName") + ", $"
                                + rSet.getString("UnitPrice") + ", "
                                + rSet.getString("UnitsInStock") + " In Stock";
                        System.out.println(categoryInfo);
                    }
                    System.out.println("");
                    start();
                    break;
                default:
                    System.out.println("Please enter a valid option (0 - 2).");
                    start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rSet != null) {
                try {
                    rSet.close();
                } catch (Exception rSetError) {
                    rSetError.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception connError) {
                    connError.printStackTrace();
                }
            }
            if (prepState != null) {
                try {
                    prepState.close();
                } catch (Exception prepStateError) {
                    prepStateError.printStackTrace();
                }
            }
        }
    }
}