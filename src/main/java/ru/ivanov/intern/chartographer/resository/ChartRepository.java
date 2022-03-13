package ru.ivanov.intern.chartographer.resository;

import org.springframework.stereotype.Repository;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.model.Chart;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ChartRepository {
    private final Map<Integer, Chart> charts;

    private int chartId;

    public ChartRepository(){
        this.chartId = -1;
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
        chartId++;
        chart.setId(chartId);
        charts.put(chartId, chart);
        return chart;
    }
}
