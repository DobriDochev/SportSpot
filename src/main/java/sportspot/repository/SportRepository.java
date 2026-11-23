package sportspot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportspot.model.Sport;

import java.util.Optional;

@Repository
public interface SportRepository extends JpaRepository<Sport, Integer> {
    boolean existsByName(String name);
    Optional<Sport> findByName(String name);
}
