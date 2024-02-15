package persistance.entity;

import java.util.UUID;

public class Resources {
    public final UUID id = UUID.randomUUID();
    public final String name;
    public final Unit unit;
    public Resources(String name, Unit unit) {
        this.name = name;
        this.unit = unit;
    }

}
