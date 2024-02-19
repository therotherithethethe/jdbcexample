package persistance.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import persistance.entity.Product;
import persistance.entity.Recipe;
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


            return Optional.of(product);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Set<Recipe> findAll() {
        return null;
    }

    @Override
    public void add(Recipe entity) {

    }

    @Override
    public boolean remove(Recipe entity) {
        return false;
    }

    @Override
    public Set<Recipe> findByName(String name) {
        return null;
    }

    @Override
    public Set<Recipe> findByPrice(int price) {
        return null;
    }

    @Override
    public Set<Recipe> findByProduct(Product product) {
        return null;
    }
}
