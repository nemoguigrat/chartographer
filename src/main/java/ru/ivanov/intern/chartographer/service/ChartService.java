package ru.ivanov.intern.chartographer.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.exeption.ValidationException;
import ru.ivanov.intern.chartographer.util.FilesUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChartService {

    //Создает новое пустое изображение с заданными размерами и возвращает id изображения
    public String createChart(int width, int height) throws ValidationException, IOException {
        if (validateSize(width, height, 20000, 50000)){
            String id = UUID.randomUUID().toString();
            FilesUtil.createBmp(width, height, id);
            return id;
        } else
            throw new ValidationException("Некорректные размеры.");
    }

    //Выдает кусок файла
    public byte[] getChartPart(String id, int x, int y, int width, int height)
            throws ChartNotFoundException, IOException, ValidationException {
        if (validateSize(width, height, 5000, 5000)){
            BufferedImage chart = getChartImage(FilesUtil.getResource(id));
            if (validateCoordinate(x, y, chart.getWidth(), chart.getHeight())) {
                return FilesUtil.getFilePart(chart, x, y, width, height);
            } else
                throw new ValidationException("Некорректные координаты.");
        } else
            throw  new ValidationException("Некорректные размеры.");
}

    //Вставляет кусок файла
    public void insertChartPart(String id, int x, int y, int width, int height, byte[] imageBytes)
            throws ChartNotFoundException, ValidationException, IOException {
        if (validateSize(width, height, 20000, 50000)){
            Resource resource = FilesUtil.getResource(id);
            BufferedImage chart = getChartImage(resource);
            if (validateCoordinate(x, y, chart.getWidth(), chart.getHeight()))
                FilesUtil.insertPartBmp(chart, resource.getFile(), x, y, width, height, imageBytes);
            else
                throw new ValidationException("Некорректные координаты.");
        } else
            throw new ValidationException("Некорректные размеры.");
    }

    //Удаляет файл
    public void deleteChart(String id) throws ChartNotFoundException, IOException {
        if (!FilesUtil.deleteFile(id))
            throw new ChartNotFoundException("Карта с id {" + id + "} не найдена.");
    }

    //Проверяет корректность переданных данных
    private boolean validateSize(int width, int height, int maxWidth, int maxHeight) {
        return width > 0 && height > 0 && width <= maxWidth && height <= maxHeight;
    }

    //Проверяет корректность переданных координат
    private boolean validateCoordinate(int x, int y, int fileWidth, int fileHeight) {
        return x < fileWidth && y < fileHeight && x >= 0 && y >= 0;
    }

    private BufferedImage getChartImage(Resource resource) throws IOException, ChartNotFoundException {
        if (FilesUtil.isBmpExists(resource)) {
            return ImageIO.read(resource.getFile());
        } else
            throw new ChartNotFoundException("Карта с id {" + resource.getFilename() + "} не найдена.");
    }
}
