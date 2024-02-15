package persistance.entity;

import java.util.UUID;

public class Product {
    public final UUID id = UUID.randomUUID();
    public String name;
    public int price;
    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }
}
