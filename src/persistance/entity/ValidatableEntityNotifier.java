package persistance.entity;

import java.util.List;
import java.util.UUID;

public abstract class ValidatableEntityNotifier {
    protected UUID id;
    protected List<String> validationMessages;
    protected String name;

    public UUID getId() {
        return id;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }

    public String getName() {
        return name;
    }
}
