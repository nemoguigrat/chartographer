package ru.ivanov.intern.chartographer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ivanov.intern.chartographer.entity.ChartFile;

@Repository
public interface ChartFileRepository extends CrudRepository<ChartFile, Long> {
    ChartFile findTopByOrderByIdDesc();
}
