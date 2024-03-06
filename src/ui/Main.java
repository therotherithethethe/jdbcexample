package ui;

import persistance.entity.Product;
import persistance.repository.Repository;
import persistance.repository.impl.ProductRepositoryImplSingleton;

public class Main {

    public static void main(String[] args) {
        Product product = new Product("Bread", 15);
        Repository<Product> productRepository = ProductRepositoryImplSingleton.getInstance();
        productRepository.add(product);
        productRepository.findAll().forEach((item) -> System.out.println(item.toString()));
    }
}
