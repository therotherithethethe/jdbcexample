package persistance.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import persistance.entity.Product;
import persistance.repository.contract.ProductRepository;

public class ProductRepositoryImpl implements ProductRepository {

    private static final String url = "jdbc:sqlite:data/bakery.db";
    @Override
    public Optional<Product> findById(UUID id) {
        String sql = "SELECT * FROM Product WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id.toString());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");

                Product product = new Product(name, price);
                return Optional.of(product);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Return an empty Optional if the product was not found or an exception occurred
        return Optional.empty();
    }

    @Override
    public Set<Product> findAll() {
        Set<Product> products = new HashSet<>();
        String sql = "SELECT * FROM product";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Assuming id is stored as a string in the database
                String name = rs.getString("name");
                int price = rs.getInt("price");

                Product product = new Product(name, price);
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return products;
    }

    @Override
    public void add(Product entity) {
        if(productExists(entity.getName()))
            return;

        String sql = "INSERT OR IGNORE INTO product (id, name, price) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the values for the prepared statement
            pstmt.setString(1, entity.getId().toString());
            pstmt.setString(2, entity.getName());
            pstmt.setInt(3, entity.getPrice());

            // Execute the INSERT statement
            int affectedRows = pstmt.executeUpdate();

            // Check if the insert was successful
            if (affectedRows > 0) {
                // Return the Product entity
                return;
            } else {
                // Handle the case where the product could not be inserted
                System.out.println("A product was not inserted.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private boolean productExists(String productName) {
        String sql = "SELECT COUNT(*) FROM product WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Product exists
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false; // Product does not exist or error occurred
    }

    @Override
    public boolean remove(Product entity) {
        return false;
    }

    @Override
    public Product findByName(String name) {
        return null;
    }

    @Override
    public Set<Product> findByPrice(int price) {
        return null;
    }
}
