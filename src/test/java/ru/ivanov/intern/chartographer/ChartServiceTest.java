package ru.ivanov.intern.chartographer;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.event.annotation.AfterTestClass;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.exeption.ValidationException;
import ru.ivanov.intern.chartographer.model.Chart;
import ru.ivanov.intern.chartographer.resository.ChartPartRepository;
import ru.ivanov.intern.chartographer.resository.ChartRepository;
import ru.ivanov.intern.chartographer.service.ChartService;
import ru.ivanov.intern.chartographer.service.FileService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class ChartServiceTest {
    @InjectMocks
    private ChartService chartService;

    @Mock
    private ChartRepository chartRepository;

    @Mock
    private ChartPartRepository chartPartRepository;

    @Mock
    private FileService fileService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({"0, 0", "-10, -10", "20001,50001"})
    public void createChart_THROW_EXEPTION(int width, int height) {
        doNothing().when(fileService).createChartFolder(anyInt());
        Assertions.assertThrows(ValidationException.class,
                () -> chartService.createChart(width, height));
    }

    @ParameterizedTest
    @CsvSource({"-1,-1", "10,10", "11,11"})
    public void getChartPart_THROW_VALIDATION_EXEPTION(int x, int y) throws ChartNotFoundException {
        when(chartRepository.get(anyInt())).thenReturn(new Chart(10, 10));
        Assertions.assertThrows(ValidationException.class,
                () -> chartService.getPartChart(1000, 9, 9, 5001, 5001),
                "Некорректные размеры.");
        Assertions.assertThrows(ValidationException.class,
                () -> chartService.getPartChart(1000, x, y, 10, 10),
                "Некорректные координаты.");
    }

    @Test
    public void getChartPart_THROW_NOT_FOUND_EXEPTION() throws ChartNotFoundException {
        when(chartRepository.get(anyInt())).thenThrow(ChartNotFoundException.class);
        Assertions.assertThrows(ChartNotFoundException.class,
                () -> chartService.getPartChart(-1, 10, 10, 10, 10));
    }
}