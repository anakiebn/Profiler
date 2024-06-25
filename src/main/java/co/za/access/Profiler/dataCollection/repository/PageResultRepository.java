package co.za.access.Profiler.dataCollection.repository;

import co.za.access.Profiler.dataCollection.model.PageResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PageResultRepository extends JpaRepository<PageResult,Long> {

    @Query("SELECT pr FROM PageResult pr LEFT JOIN pr.query q WHERE q.name = :queryName")
    Optional<PageResult> findByQueryName(String queryName);
}
