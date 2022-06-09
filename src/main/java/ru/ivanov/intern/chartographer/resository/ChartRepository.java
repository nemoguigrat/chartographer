package ru.ivanov.intern.chartographer.resository;

import org.springframework.stereotype.Repository;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.model.Chart;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ChartRepository {
    private final Map<Integer, Chart> charts;

    private AtomicInteger chartId;

    public ChartRepository(){
        this.chartId = new AtomicInteger(-1);
        this.charts = new ConcurrentHashMap<>();
    }

    public Chart get(int id) throws ChartNotFoundException {
        Chart chart = charts.get(id);
        if (chart == null)
            throw new ChartNotFoundException("Карта с id {" + id + "} не найдена.");
        return chart;
    }

    public void delete(int id) throws ChartNotFoundException {
        if (charts.remove(id) == null)
            throw new ChartNotFoundException("Карта с id {" + id + "} не найдена.");
    }

    public Chart save(Chart chart) {
        int current_value = chartId.incrementAndGet();
        chart.setId(current_value);
        charts.put(current_value, chart);
        return chart;
    }
}
