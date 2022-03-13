package ru.ivanov.intern.chartographer.service;

import ch.qos.logback.core.util.FileUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.model.Chart;
import ru.ivanov.intern.chartographer.model.ChartPart;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    private final String mediaFolder;

    public FileService(ApplicationArguments args) {
        this.mediaFolder = args.getSourceArgs()[0];
    }

    @PostConstruct
    public void init() throws IOException {
        Path folder = Paths.get(mediaFolder);
        if (!Files.exists(folder)) {
            Files.createDirectory(folder);
        }
    }

    @PreDestroy
    public void destroy() throws IOException {
        FileUtils.deleteDirectory(Paths.get(mediaFolder).toFile());
    }

    /**
     * Сохраняет переданное изображение на диске.
     * @param chartId id карты
     * @param chartPartId id части карты
     * @param image сохраняемое изображение в виде потока байт
     * @throws IOException ошибка записи или чтения файла
     */
    public void saveImage(int chartId, int chartPartId, byte[] image) throws IOException {
        BufferedImage imagePart = ImageIO.read(new ByteArrayInputStream(image));
        File file = Files.createFile(Paths.get(mediaFolder,
                String.valueOf(chartId), chartPartId + ".bmp")).toFile();
        ImageIO.write(imagePart, "bmp", file);
    }

    /**
     * Создает директорию под названием chartId, в которой будут хранится в виде файлов части карты
     * @param chartId id карты
     * @throws IOException ошибка записи или чтения файла
     */
    public void createChartFolder(int chartId) throws IOException {
        Path folder = Paths.get(mediaFolder, String.valueOf(chartId));
        if (!Files.exists(folder)) {
            Files.createDirectory(folder);
        }
    }

    /**
     * Собирает изображение из частей карты по переднным данным.
     * @param chart объект карты
     * @param x левовый верхний угол фрагмента
     * @param y правый верхний угол фрагмента
     * @param width ширина возвращаемого изображения
     * @param height высота возвращаемого изображения
     * @return изобаржение в формате BMP (поток байт)
     * @throws IOException ошибка чтения или записи файла
     */
    public byte[] createOutputImage(Chart chart, int x, int y, int width, int height) throws IOException {
        int partWidth = Math.min(chart.getWidth() - x, width);
        int partHeight = Math.min(chart.getHeight() - y, height);

        List<ChartPart> intersectionParts =
                chart.getChartPart().stream().filter(part -> intersect(part, x, y, partWidth, partHeight))
                        .collect(Collectors.toList());
        BufferedImage outputChart = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics outputChartGraphics = outputChart.getGraphics();
        for (ChartPart part : intersectionParts) {
            BufferedImage image = ImageIO.read(getImageFile(chart.getId(), part.getId()).toFile());
            outputChartGraphics.drawImage(image, part.getX(), part.getY(),
                    part.getWidth(), part.getHeight(), null);
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(outputChart, "bmp", stream);
        return stream.toByteArray();
    }

    /**
     * Удаляет карту по переданному идентификатору
     * @param id уникальный идентификатор изображения
     * @throws IOException ошибка чтения или записи файла
     */
    public boolean deleteDirectory(int id) throws IOException {
        Path folder = Paths.get(mediaFolder, String.valueOf(id));
        if (Files.exists(folder)) {
            FileUtils.deleteDirectory(folder.toFile());
            return true;
        }
        return false;
    }

    private boolean intersect(ChartPart chartPart, int x, int y, int width, int height) {
        int chartPartWidth = chartPart.getWidth();
        int chartPartHeight = chartPart.getHeight();
        if (chartPartWidth <= 0 || chartPartHeight <= 0 || width <= 0 || height <= 0) {
            return false;
        }
        int chartPartX = chartPart.getX();
        int chartPartY = chartPart.getY();
        chartPartWidth += chartPartX;
        chartPartHeight += chartPartY;
        width += x;
        height += y;
        return ((chartPartWidth < chartPartX || chartPartWidth > x) &&
                (chartPartHeight < chartPartY || chartPartHeight > y) &&
                (width < x || width > chartPartX) &&
                (height < y || height > chartPartY));
    }

    private Path getImageFile(int chartId, int imageId) {
        return Paths.get(mediaFolder, String.valueOf(chartId), imageId + ".bmp");
    }
}
