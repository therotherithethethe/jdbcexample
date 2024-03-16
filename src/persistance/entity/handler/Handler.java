package persistance.entity.handler;

import java.util.UUID;
import persistance.entity.ValidatableEntityNotifier;

public abstract class Handler<T extends ValidatableEntityNotifier> {
    public void validate(T entity) {
        if(entity.getName().length() < 3) {
            entity.getValidationMessages().add("name cant be smaller than 3 symbols");
        }
        if(entity.getName().length() > 16) {
            entity.getValidationMessages().add("name cant be bigger than 16 symbols");
        }
    }
}
