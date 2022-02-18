package ru.ivanov.intern.chartographer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ivanov.intern.chartographer.entity.ChartFile;
import ru.ivanov.intern.chartographer.entity.ChartPart;

@Repository
public interface ChartPartRepository extends CrudRepository<ChartPart, Long> {
    ChartPart findTopByOrderByIdDesc();
}
