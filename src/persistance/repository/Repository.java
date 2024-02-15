package persistance.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Repository<E> {
    Optional<E> findById(UUID id);
    Set<E> findAll();

    E add(E entity);
    boolean remove(E entity);

}
