package co.za.access.profiler.dataCollection.googleSearch.repository;


import co.za.access.profiler.dataCollection.googleSearch.model.PageResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface PageResultRepository extends JpaRepository<PageResult,Long> {

//    @Query("SELECT pr FROM PageResult pr LEFT JOIN pr.query q WHERE q.name = :queryName")
    Optional<PageResult> findByQuery(String query);
}
