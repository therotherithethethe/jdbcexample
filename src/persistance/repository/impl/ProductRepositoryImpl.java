package persistance.repository.impl;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import persistance.entity.Product;
import persistance.repository.contract.ProductRepository;

public class ProductRepositoryImpl implements ProductRepository {

    private final String url = "jdbc:sqlite:/data/bakery.db";
    @Override
    public Optional<Product> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Set<Product> findAll() {
        return null;
    }

    @Override
    public Product add(Product entity) {
        return null;
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
