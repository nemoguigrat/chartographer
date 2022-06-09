package ru.ivanov.intern.chartographer.resository;

import org.springframework.stereotype.Repository;
import ru.ivanov.intern.chartographer.model.ChartPart;

import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ChartPartRepository {
    private AtomicInteger partId;

    public ChartPartRepository(){
        this.partId = new AtomicInteger(-1);
    }

    public ChartPart save(ChartPart chart) {
        int current_value = partId.incrementAndGet();
        chart.setId(current_value);
        return chart;
    }
}
