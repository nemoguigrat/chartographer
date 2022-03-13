package ru.ivanov.intern.chartographer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.model.Chart;
import ru.ivanov.intern.chartographer.model.ChartPart;
import ru.ivanov.intern.chartographer.resository.ChartPartRepository;
import ru.ivanov.intern.chartographer.resository.ChartRepository;

public class RepositoriesTest {
    @Test
    public void testSaveChartRepository() throws ChartNotFoundException {
        ChartRepository chartRepository = new ChartRepository();
        for (var i = 0; i < 100; i++) {
            chartRepository.save(new Chart(100, 100));
        }

        Assertions.assertEquals(99, chartRepository.get(99).getId());
    }

    @Test
    public void testSaveChartPartRepository() {
        ChartPartRepository chartPartRepository = new ChartPartRepository();
        for (var i = 0; i < 100; i++) {
            chartPartRepository.save(new ChartPart(100, 100, 100, 100));
        }

        Assertions.assertEquals(100, chartPartRepository.save(
                new ChartPart(0, 0, 0, 0)).getId());
    }
}
