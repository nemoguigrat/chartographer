package ru.ivanov.intern.chartographer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.core.io.ClassPathResource;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.exeption.ValidationException;
import ru.ivanov.intern.chartographer.service.ChartService;
import ru.ivanov.intern.chartographer.util.FilesUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class ChartServiceTest {
    private final ChartService chartService = new ChartService();
    private final List<Path> filesToBeDeleted = new ArrayList<>();

    @ParameterizedTest
    @CsvSource({"0, 0", "-10, -10", "20001,50001"})
    public void createChart_THROW_EXEPTION(int width, int height) {
        Assertions.assertThrows(ValidationException.class,
                () -> chartService.createChart(width, height));
    }

    @Test
    public void createChart_SAVE_IMAGE() throws IOException, ValidationException {
        String fileId = chartService.createChart(10, 10);
        Path path = getFilePath(fileId);
        filesToBeDeleted.add(path);
        Assertions.assertTrue(Files.exists(path));
    }

    @ParameterizedTest
    @CsvSource({"-1,-1", "10,10", "11,11"})
    public void getChartPart_THROW_VALIDATION_EXEPTION(int x, int y) {
        Assertions.assertThrows(ValidationException.class,
                () -> chartService.getChartPart("test", 9, 9, 5001, 5001),
                "Некорректные размеры.");
        Assertions.assertThrows(ValidationException.class,
                () -> chartService.getChartPart("test", x, y, 10, 10),
                "Некорректные координаты.");
    }

    @Test
    public void getChartPart_THROW_NOT_FOUND_EXEPTION() {
        Assertions.assertThrows(ChartNotFoundException.class,
                () -> chartService.getChartPart(" ", 10, 10, 10, 10));
    }

    @ParameterizedTest
    @CsvSource({"0,0,6,6", "0,0,12,12", "3,3,7,7"})
    public void getChartPart(int x, int y, int width, int height)
            throws IOException, ChartNotFoundException, ValidationException {
        byte[] imageByteArray = chartService.getChartPart("test", x, y, width, height);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageByteArray));

        Assertions.assertEquals(Color.white.getRGB(), image.getRGB(0, 0));
        Assertions.assertEquals(Color.black.getRGB(), image.getRGB(width - 1, height - 1));
    }

    @ParameterizedTest
    @CsvSource({"0,0,6,6", "5,5,5,5", "3,3,7,7"})
    public void insertChartPart(int x, int y, int insertWidth, int insertHeight) throws IOException, ChartNotFoundException, ValidationException {
        FilesUtil.createBmp(10, 10, "insert");
        Path path = getFilePath("insert");
        filesToBeDeleted.add(path);
        byte[] imageInsertByte = createTestImageToInsert(insertWidth, insertHeight);
        chartService.insertChartPart("insert", x, y, insertWidth, insertHeight, imageInsertByte);
        BufferedImage image = ImageIO.read(path.toFile());

        Assertions.assertEquals(Color.white.getRGB(),
                image.getRGB(x, y));
        Assertions.assertEquals(Color.white.getRGB(),
                image.getRGB(x + Math.min(image.getWidth() - 1 - x, insertWidth - 1),
                        y + Math.min(image.getHeight() - 1 - y, insertHeight - 1)));

    }

    @Test
    public void deleteChart() throws IOException {
        FilesUtil.createBmp(10, 10, "delete");
        Path path = getFilePath("delete");
        Assertions.assertTrue(Files.exists(path));
        Assertions.assertDoesNotThrow(() -> chartService.deleteChart("delete"));
        Assertions.assertFalse(Files.exists(path));

    }

    @AfterEach
    public void cleanup() {
        filesToBeDeleted.forEach(path -> {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Path getFilePath(String id) throws IOException {
        return Paths.get(new ClassPathResource(".").getFile().getPath(), "/media", id + ".bmp");
    }

    private byte[] createTestImageToInsert(int insertWidth, int insertHeight) throws IOException {
        BufferedImage imageToInsert = new BufferedImage(insertWidth, insertHeight, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < imageToInsert.getWidth(); x++)
            for (int y = 0; y < imageToInsert.getHeight(); y++)
                imageToInsert.setRGB(x, y, Color.white.getRGB());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(imageToInsert, "bmp", stream);
        return stream.toByteArray();
    }

}