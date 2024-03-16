package persistance.entity.objectpool.impl;

import persistance.entity.Product;
import persistance.entity.objectpool.ObjectPool;

public class ProductPool extends ObjectPool<Product> {
    protected Product create(String name, int price) {
        return new Product(name, price);
    }

    @Override
    protected Product create() {
        return null;
    }
}
