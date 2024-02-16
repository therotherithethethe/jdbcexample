package persistance.entity;

import static java.lang.StringTemplate.STR;

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

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
    @Override
    public String toString() {
        return STR."UUID: \{id}, Name: \{name}, Price: \{price}";
    }
}
