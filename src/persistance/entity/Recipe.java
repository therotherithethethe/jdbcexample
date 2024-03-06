package persistance.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Recipe {
    public final UUID id;
    public Set<Resources> resources = new HashSet<>();
    public String name;
    public Recipe(String name, Set<Resources> resources) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.resources = resources;
    }
    public Recipe(UUID id, String name, Set<Resources> resources) {
        this.id = id;
        this.name = name;
        this.resources = resources;
    }
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Resources> getResources() {
        return new HashSet<>(resources);
    }

    public interface RecipeBuilder {
        Recipe build();
    }
    public interface RecipeBuilderResources {
        RecipeBuilder resources(Set<Resources> resources);
    }
    public interface RecipeBuilderName {
        RecipeBuilderResources name(String name);
    }
    public interface RecipeBuilderId {
        RecipeBuilderName id(UUID id);
    }
    public static RecipeBuilderId builderId() {
        return id -> name -> resources -> () -> new Recipe(id, name, resources);
    }
    public static RecipeBuilderName builder() {
        return name -> resources -> () -> new Recipe(name, resources);
    }
}
