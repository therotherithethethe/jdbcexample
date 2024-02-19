package persistance.repository.contract;

import java.util.Set;
import persistance.entity.Product;
import persistance.entity.Recipe;
import persistance.repository.Repository;

public interface RecipeRepository extends Repository<Recipe> {
    Set<Recipe> findByName(String name);
    Set<Recipe> findByPrice(int price);
    Set<Recipe> findByProduct(Product product);

}
