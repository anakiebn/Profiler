package co.za.access.profiler.dataCollection.googleSearch.repository;

import co.za.access.profiler.dataCollection.googleSearch.model.Source;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceRepository extends JpaRepository<Source,Long> {
}
