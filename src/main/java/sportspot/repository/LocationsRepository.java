package sportspot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportspot.model.Locations;

@Repository
public interface LocationsRepository extends JpaRepository<Locations, Integer> {
    boolean existsByName(String name);
}
