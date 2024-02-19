package persistance.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
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

                Product product = new Product(id, name, price);
                return Optional.of(product);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

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

            pstmt.setString(1, entity.getId().toString());
            pstmt.setString(2, entity.getName());
            pstmt.setInt(3, entity.getPrice());

            pstmt.execute(sql);
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
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean remove(Product entity) {
        String sql = "DELETE FROM Product WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(url); PreparedStatement pstmt = connection.prepareStatement(sql)) {


            pstmt.setString(1, entity.getName());
            return pstmt.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Product findByName(String name) {
        String sql = "SELECT * FROM product WHERE name = ?";
        try(Connection connection = DriverManager.getConnection(url); PreparedStatement pstmt = connection.prepareStatement(sql)) {


            pstmt.setString(1, name);

            ResultSet productSet = pstmt.executeQuery();
            String resultName = productSet.getString("name");
            UUID id = UUID.fromString(productSet.getString("id"));
            int price = productSet.getInt("price");

            return new Product(id, resultName, price);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Set<Product> findByPrice(int price) {
        Set<Product> products = new HashSet<>();
        String sql = "SELECT * FROM product WHERE price = ?";
        try(Connection connection = DriverManager.getConnection(url); PreparedStatement pstmt = connection.prepareStatement(sql)) {


            pstmt.setInt(1, price);

            ResultSet productSet = pstmt.executeQuery();


            while(productSet.next()) {
                String resultName = productSet.getString("name");
                UUID id = UUID.fromString(productSet.getString("id"));
                int productPrice = productSet.getInt("price");

                products.add(new Product(id, resultName, price));
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }
}
