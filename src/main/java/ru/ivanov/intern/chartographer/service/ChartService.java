package ru.ivanov.intern.chartographer.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.exeption.ValidationException;
import ru.ivanov.intern.chartographer.util.ChartFilesUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChartService {
    private final ChartFilesUtils filesUtil;

    /**
     * Создает харту с переданными размерами
     * @param width ширина харты
     * @param height высота харты
     * @return уникальный идентификатор харты
     * @throws ValidationException
     * @throws IOException
     */
    public String createChart(int width, int height) throws ValidationException, IOException {
        if (validateSize(width, height, 20000, 50000)) {
            String id = UUID.randomUUID().toString();
            filesUtil.createBmp(width, height, id);
            return id;
        } else {
            throw new ValidationException("Некорректные размеры.");
        }
    }

    /**
     * Вставляет переданный фрагмент в харту
     * @param id уникальный идентификатор харты
     * @param x координата харты, куда будет сохранен фрагмент
     * @param y координата харты, куда будет сохранен фрагмент
     * @param width ширина передаваемого изображения
     * @param height высота передаваемого изображения
     * @param imageBytes передаваемое изображение в формате BMP
     * @throws ChartNotFoundException
     * @throws ValidationException
     * @throws IOException
     */
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

    /**
     * Получает фрагмент харты с переданными координатами и размерами
     * @param id уникальный идентификатор харты
     * @param x левовый верхний угол фрагмента
     * @param y правый верхний угол фрагмента
     * @param width ширина возвращаемого изображения
     * @param height высота возвращаемого изображения
     * @return изобаржение в формате BMP (поток байт)
     * @throws ChartNotFoundException
     * @throws IOException
     * @throws ValidationException
     */
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

    /**
     * Удаляет харту по переданному идентификатору
     * @param id уникальный идентификатор изображения
     * @throws ChartNotFoundException
     * @throws IOException
     */
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

    /**
     * Получает {@code BufferedImage} по передаваемому пути к файлу, если такой файл существует
     * @param path путь к файлу
     * @return файл в виде изображения
     * @throws IOException
     * @throws ChartNotFoundException
     */
    private BufferedImage getChartImage(Path path) throws IOException, ChartNotFoundException {
        if (filesUtil.isBmpExists(path)) {
            return ImageIO.read(ImageIO.createImageInputStream(path.toFile()));
        } else {
            throw new ChartNotFoundException("Карта с id {" + path.getFileName() + "} не найдена.");
        }
    }
}
