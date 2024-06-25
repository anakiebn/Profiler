package co.za.access.Profiler.dataCollection.repository;

import co.za.access.Profiler.dataCollection.model.Source;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceRepository extends JpaRepository<Source,Long> {
}
