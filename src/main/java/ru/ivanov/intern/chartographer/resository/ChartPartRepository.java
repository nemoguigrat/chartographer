package ru.ivanov.intern.chartographer.resository;

import org.springframework.stereotype.Repository;
import ru.ivanov.intern.chartographer.model.ChartPart;

@Repository
public class ChartPartRepository {
    private Integer partId;

    public ChartPartRepository(){
        this.partId = -1;
    }

    public ChartPart save(ChartPart chart) {
        partId++;
        chart.setId(partId);
        return chart;
    }
}
