package persistance.entity;

import java.util.Map;

public record Resources(Map<Product, Unit> products) {

}
