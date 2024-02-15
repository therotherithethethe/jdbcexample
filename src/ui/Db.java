package ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Db {
    public static void createDbTables() {
        String url = "jdbc:sqlite:test.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                try (Statement stmt = conn.createStatement()) {
                    try (Scanner scanner = new Scanner(System.in)) {
                        // Створення таблиці Product
                        String createProductTableSql = "CREATE TABLE Product ("
                            + "id INTEGER PRIMARY KEY,"
                            + "name TEXT NOT NULL,"
                            + "price REAL NOT NULL"
                            + ")";
                        stmt.execute(createProductTableSql);
                        System.out.println("Table Product created successfully.");

                        // Створення таблиці Ingredient
                        String createIngredientTableSql = "CREATE TABLE Ingredient ("
                            + "id INTEGER PRIMARY KEY,"
                            + "name TEXT NOT NULL,"
                            + "quantity INTEGER NOT NULL"
                            + ")";
                        stmt.execute(createIngredientTableSql);
                        System.out.println("Table Ingredient created successfully.");

                        // Створення таблиці Order
                        String createOrderTableSql = "CREATE TABLE Order ("
                            + "id INTEGER PRIMARY KEY,"
                            + "customer_name TEXT NOT NULL,"
                            + "order_date DATE NOT NULL"
                            + ")";
                        stmt.execute(createOrderTableSql);
                        System.out.println("Table Order created successfully.");

                        // Створення таблиці OrderDetail
                        String createOrderDetailTableSql = "CREATE TABLE OrderDetail ("
                            + "id INTEGER PRIMARY KEY,"
                            + "order_id INTEGER NOT NULL,"
                            + "product_id INTEGER NOT NULL,"
                            + "quantity INTEGER NOT NULL,"
                            + "FOREIGN KEY (order_id) REFERENCES Order(id),"
                            + "FOREIGN KEY (product_id) REFERENCES Product(id)"
                            + ")";
                        stmt.execute(createOrderDetailTableSql);

                    }
                    catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}


