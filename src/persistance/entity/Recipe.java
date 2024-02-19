package persistance.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Recipe {
    public final UUID id = UUID.randomUUID();
    public Set<Resources> resources = new HashSet<>();
    public String name;
    public Recipe(String name, Set<Resources> resources) {
        this.name = name;
        this.resources = resources;
    }
    public UUID getId() {
        return id;
    }

    public Set<Resources> getResources() {
        return new HashSet<>(resources);
    }

}
