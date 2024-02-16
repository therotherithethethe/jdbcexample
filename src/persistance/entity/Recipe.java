package persistance.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Recipe {
    public final String id = UUID.randomUUID().toString();
    public final Product product;
    public Set<Resources> resources = new HashSet<>();
    public Recipe(Product product, HashSet<Resources> resources) {
        this.product = product;
        this.resources = resources;
    }
    public Recipe(Product product) {
        this(product, new HashSet<>());
    }

    public String getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Set<Resources> getResources() {
        return new HashSet<>(resources);
    }

}
