package persistance.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import persistance.entity.Product;
import persistance.entity.Recipe;
import persistance.entity.Resources;
import persistance.repository.Repository;
import persistance.repository.contract.RecipeRepository;

public class RecipeRepositoryImpl implements RecipeRepository {

    private static final String url = "jdbc:sqlite:data/bakery.db";
    @Override
    public Optional<Recipe> findById(UUID id) {
        String recipeIngredientsSql = "SELECT * FROM recipe_ingredients WHERE `recipe id` = ?";
        String recipesSql = "SELECT * FROM recipes WHERE `id` = ?";
        Repository<Product> prodRepo = new ProductRepositoryImpl();

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement recipeIngredientsPtsmt = conn.prepareStatement(recipeIngredientsSql);
            PreparedStatement recipesPtsmt = conn.prepareStatement(recipesSql)) {

            recipeIngredientsPtsmt.setString(1, id.toString());
            recipesPtsmt.setString(1, id.toString());

            ResultSet recipeIngredientsRs = recipeIngredientsPtsmt.executeQuery();
            ResultSet recipeNameRs = recipesPtsmt.executeQuery();
            String recipeName = recipeNameRs.getString("name");

            Set<Resources> ingredients = new HashSet<>();
            while(recipeIngredientsRs.next()) {
                String productId = recipeIngredientsRs.getString("product id");
                Optional<Product> productOpt = prodRepo.findById(UUID.fromString(productId));
                int grams = recipeIngredientsRs.getInt("weight");
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();
                    ingredients.add(new Resources(product, grams));
                }

            }
            return Optional.of(new Recipe(id, recipeName, ingredients));
        } catch (SQLException e) {

        }

        return Optional.empty();
    }

    @Override
    public Set<Recipe> findAll() {
        Set<Recipe> recipes = new HashSet<>();
        String sql = "select * from recipes";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement recipesPtsmt = conn.prepareStatement(sql)) {

            ResultSet recipesRs = recipesPtsmt.executeQuery();

            while(recipesRs.next()) {
                String id = recipesRs.getString("id");
                Optional<Recipe> recipeOpt = findById(UUID.fromString(id));
                if(recipeOpt.isPresent()) {
                    Recipe recipe = recipeOpt.get();
                    recipes.add(recipe);
                }

            }

        } catch (SQLException e) {
        }
        return recipes;
    }

    @Override
    public void add(Recipe entity) {
        String insertRecipeSql = "INSERT INTO recipes (id, name) VALUES (?, ?)";
        String insertIngredientSql = "INSERT INTO recipe_ingredients (`recipe id`, `product id`, weight) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement insertRecipeStmt = conn.prepareStatement(insertRecipeSql);
            PreparedStatement insertIngredientStmt = conn.prepareStatement(insertIngredientSql)) {

            // Insert the recipe
            insertRecipeStmt.setString(1, entity.getId().toString());
            insertRecipeStmt.setString(2, entity.getName());
            insertRecipeStmt.executeUpdate();

            // Insert each resource/ingredient
            for (Resources resource : entity.getResources()) {
                insertIngredientStmt.setString(1, entity.getId().toString());
                insertIngredientStmt.setString(2, resource.product().getId().toString());
                insertIngredientStmt.setInt(3, resource.grams());
                insertIngredientStmt.executeUpdate();
            }

        } catch (SQLException e) {
        }
    }

    @Override
    public boolean remove(Recipe entity) {
        String deleteIngredientsSql = "DELETE FROM recipe_ingredients WHERE `recipe id` = ?";
        String deleteRecipeSql = "DELETE FROM recipes WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement deleteIngredientsStmt = conn.prepareStatement(deleteIngredientsSql);
            PreparedStatement deleteRecipeStmt = conn.prepareStatement(deleteRecipeSql)) {

            // Delete ingredients
            deleteIngredientsStmt.setString(1, entity.getId().toString());
            deleteIngredientsStmt.executeUpdate();

            // Delete the recipe
            deleteRecipeStmt.setString(1, entity.getId().toString());
            int affectedRows = deleteRecipeStmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Set<Recipe> findByName(String name) {
        Set<Recipe> recipes = new HashSet<>();
        String sql = "SELECT id FROM recipes WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                findById(id).ifPresent(recipes::add);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    }

    @Override
    public Set<Recipe> findByPrice(int price) {
        Set<Recipe> matchingRecipes = new HashSet<>();
        // SQL query to calculate the total price of ingredients for each recipe
        String sql = "SELECT ri.`recipe id`, SUM(p.price * ri.weight) AS total_price " +
            "FROM recipe_ingredients ri " +
            "JOIN product p ON ri.`product id` = p.id " +
            "GROUP BY ri.`recipe id` " +
            "HAVING total_price = ?";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, price); // Set the price to match against the total price of ingredients
            ResultSet rs = stmt.executeQuery();

            // Iterate through the results and find recipes by ID
            while (rs.next()) {
                UUID recipeId = UUID.fromString(rs.getString("recipe id"));
                findById(recipeId).ifPresent(matchingRecipes::add);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return matchingRecipes;
    }

    @Override
    public Set<Recipe> findByProduct(Product product) {
        Set<Recipe> recipes = new HashSet<>();
        String sql = "SELECT DISTINCT `recipe id` FROM recipe_ingredients WHERE `product id` = ?";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getId().toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UUID recipeId = UUID.fromString(rs.getString("recipe id"));
                findById(recipeId).ifPresent(recipes::add);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recipes;
    }
}
