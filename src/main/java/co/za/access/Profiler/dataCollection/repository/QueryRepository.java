package co.za.access.Profiler.dataCollection.repository;

import co.za.access.Profiler.dataCollection.model.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QueryRepository extends JpaRepository<Query,Long> {

    Optional<Query> findByName(String name);
}
