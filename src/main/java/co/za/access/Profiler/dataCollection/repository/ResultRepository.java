package co.za.access.Profiler.dataCollection.repository;

import co.za.access.Profiler.dataCollection.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface ResultRepository extends JpaRepository<Result,Long> {

    @Query("SELECT r FROM Result r WHERE r.pageResult.id = :pageResultId")
    Optional<Set<Result>> findAllByPageResultId(@Param("pageResultId") Long pageResultId);

}
