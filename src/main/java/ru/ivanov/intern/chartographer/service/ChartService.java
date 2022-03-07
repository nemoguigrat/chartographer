package ru.ivanov.intern.chartographer.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.exeption.ValidationException;
import ru.ivanov.intern.chartographer.util.ChartFilesUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChartService {
    private final ChartFilesUtils filesUtil;

    public String createChart(int width, int height) throws ValidationException, IOException {
        if (validateSize(width, height, 20000, 50000)) {
            String id = UUID.randomUUID().toString();
            filesUtil.createBmp(width, height, id);
            return id;
        } else {
            throw new ValidationException("Некорректные размеры.");
        }
    }

    public byte[] getPartChart(String id, int x, int y, int width, int height)
            throws ChartNotFoundException, IOException, ValidationException {
        if (validateSize(width, height, 5000, 5000)) {
            BufferedImage chart = getChartImage(filesUtil.getFilePath(id));

            if (validateCoordinate(x, y, chart.getWidth(), chart.getHeight())) {
                return filesUtil.getFilePart(chart, x, y, width, height);
            } else {
                throw new ValidationException("Некорректные координаты.");
            }
        } else {
            throw new ValidationException("Некорректные размеры.");
        }
    }

    public void insertPartChart(String id, int x, int y, int width, int height, byte[] imageBytes)
            throws ChartNotFoundException, ValidationException, IOException {
        if (validateSize(width, height, 20000, 50000)) {
            Path imagePath = filesUtil.getFilePath(id);
            BufferedImage chart = getChartImage(imagePath);

            if (validateCoordinate(x, y, chart.getWidth(), chart.getHeight())) {
                filesUtil.insertPartBmp(chart, imagePath.toFile(), x, y, width, height, imageBytes);
            } else {
                throw new ValidationException("Некорректные координаты.");
            }
        } else {
            throw new ValidationException("Некорректные размеры.");
        }
    }

    public void deleteChart(String id) throws ChartNotFoundException, IOException {
        if (!filesUtil.deleteFile(id)) {
            throw new ChartNotFoundException("Карта с id {" + id + "} не найдена.");
        }
    }

    private boolean validateSize(int width, int height, int maxWidth, int maxHeight) {
        return width > 0 && height > 0 && width <= maxWidth && height <= maxHeight;
    }

    private boolean validateCoordinate(int x, int y, int fileWidth, int fileHeight) {
        return x < fileWidth && y < fileHeight && x >= 0 && y >= 0;
    }

    private BufferedImage getChartImage(Path path) throws IOException, ChartNotFoundException {
        if (filesUtil.isBmpExists(path)) {
            return ImageIO.read(path.toFile());
        } else {
            throw new ChartNotFoundException("Карта с id {" + path.getFileName() + "} не найдена.");
        }
    }
}
