package ui;

import java.sql.*;
import java.util.Scanner;
import persistance.entity.Product;
import persistance.repository.Repository;
import persistance.repository.contract.ProductRepository;
import persistance.repository.impl.ProductRepositoryImpl;

public class Main {

    public static void main(String[] args) {
        Product product = new Product("Bread", 15);
        Repository<Product> productRepository = new ProductRepositoryImpl();
        productRepository.add(product);
        productRepository.findAll().forEach((item) -> System.out.println(item.toString()));
    }
}
