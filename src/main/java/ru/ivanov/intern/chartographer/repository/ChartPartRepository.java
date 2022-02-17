package ru.ivanov.intern.chartographer.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ivanov.intern.chartographer.entity.ChartFile;
import ru.ivanov.intern.chartographer.entity.ChartPart;

public interface ChartPartRepository extends CrudRepository<ChartPart, Long> {
    ChartPart findTopByOrderByIdDesc();
}
