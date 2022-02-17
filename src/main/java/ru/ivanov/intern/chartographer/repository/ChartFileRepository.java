package ru.ivanov.intern.chartographer.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ivanov.intern.chartographer.entity.ChartFile;

public interface ChartFileRepository extends CrudRepository<ChartFile, Long> {
    ChartFile findTopByOrderByIdDesc();
}
