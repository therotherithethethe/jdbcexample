package persistance.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import persistance.entity.Product;
import persistance.entity.Recipe;
import persistance.entity.Resources;
import persistance.repository.contract.RecipeRepository;

public class RecipeRepositoryImpl implements RecipeRepository {

    private static final String url = "jdbc:sqlite:data/bakery.db";
    @Override
    public Optional<Recipe> findById(UUID id) {
        String sql = "SELECT * FROM recipes WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id.toString());

            ResultSet rs = pstmt.executeQuery();

            String recipeId = rs.getString("id");
            String recipeName = rs.getString("name");
            String[] resourcesGrams = rs.getString("resources").split(",");
            ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
            List<Resources> resources = new ArrayList<>();
            for(int i = 0; i < resourcesGrams.length; i++) {
                String[] currResourceGram = resourcesGrams[i].split(" ");
                Product currProduct = productRepository.findByName(currResourceGram[0]);
                resources.add(new Resources(currProduct, Integer.parseInt(currResourceGram[1])));
            }

            return Optional.of(new Recipe(UUID.fromString(recipeId), recipeName, new HashSet<>(resources)));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Set<Recipe> findAll() {
        Set<Recipe> recipes = new HashSet<>();
        String sql = "SELECT * FROM recipes";
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Assuming the same structure as in findById
                String recipeId = rs.getString("id");
                String recipeName = rs.getString("name");
                String[] resourcesGrams = rs.getString("resources").split(",");
                ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
                List<Resources> resources = new ArrayList<>();
                for (String resourceGram : resourcesGrams) {
                    String[] currResourceGram = resourceGram.split(" ");
                    Product currProduct = productRepository.findByName(currResourceGram[0]);
                    resources.add(new Resources(currProduct, Integer.parseInt(currResourceGram[1])));
                }
                recipes.add(new Recipe(UUID.fromString(recipeId), recipeName, new HashSet<>(resources)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    }

    @Override
    public void add(Recipe entity) {
        String sql = "INSERT INTO recipes (id, name, resources) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getId().toString());
            pstmt.setString(2, entity.getName());
            // Assuming Resources are stored as a concatenated string
            String resources = entity.getResources().stream()
                .map(r -> STR."\{r.product().getName()} \{r.grams()}")
                .collect(Collectors.joining(","));
            pstmt.setString(3, resources);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public boolean remove(Recipe entity) {
        String sql = "DELETE FROM recipes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getId().toString());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Set<Recipe> findByName(String name) {
        Set<Recipe> recipes = new HashSet<>();
        String sql = "SELECT * FROM recipes WHERE name LIKE ?";
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String recipeId = rs.getString("id");
                String recipeName = rs.getString("name");
                String[] resourcesGrams = rs.getString("resources").split(",");
                ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
                List<Resources> resources = new ArrayList<>();
                for (String resourceGram : resourcesGrams) {
                    String[] currResourceGram = resourceGram.split(" ");
                    Product currProduct = productRepository.findByName(currResourceGram[0]);
                    resources.add(new Resources(currProduct, Integer.parseInt(currResourceGram[1])));
                }
                recipes.add(new Recipe(UUID.fromString(recipeId), recipeName, new HashSet<>(resources)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    }

    @Override
    public Set<Recipe> findByPrice(int price) {
        Set<Recipe> recipes = new HashSet<>();
        String sql = "SELECT * FROM recipes WHERE price <= ?";
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, price);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Recipe recipe = createRecipeFromResultSet(rs);
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    }

    @Override
    public Set<Recipe> findByProduct(Product product) {
        Set<Recipe> recipes = new HashSet<>();
        String sql = "SELECT r.* FROM recipes r JOIN recipe_products rp ON r.id = rp.recipe_id WHERE rp.product_id = ?";
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getId().toString()); // Assuming product ID is the linking attribute
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Recipe recipe = createRecipeFromResultSet(rs);
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    }
}
