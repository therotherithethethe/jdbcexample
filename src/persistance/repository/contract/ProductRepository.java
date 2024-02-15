package persistance.repository.contract;

import persistance.entity.Product;
import java.util.Set;
import persistance.repository.Repository;

public interface ProductRepository extends Repository<Product> {
    Product findByName(String name);
    Set<Product> findByPrice(int price);

}
