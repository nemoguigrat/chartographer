package ru.ivanov.intern.chartographer.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.exeption.ValidationException;
import ru.ivanov.intern.chartographer.util.FilesUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
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
            throw new ValidationException("Не корректные размеры.");
    }

    //Выдает кусок файла
    public byte[] getChartPart(String id, int x, int y, int width, int height)
            throws ChartNotFoundException, IOException, ValidationException {
        if (validateSize(width, height, 5000, 5000)){
            BufferedImage chart = getChartImage(FilesUtil.getPath(id));
            if (validateCoordinate(x, y, chart.getWidth(), chart.getHeight())) {
                return FilesUtil.getFilePart(chart, x, y, width, height);
            } else
                throw new ValidationException("Некорректные координаты.");
        } else
            throw  new ValidationException("Некооретные размеры.");
}

    //Вставляет кусок файла
    public void insertChartPart(String id, int x, int y, int width, int height, byte[] imageBytes)
            throws ChartNotFoundException, ValidationException, IOException {
        if (validateSize(width, height, 20000, 50000)){
            Path path = FilesUtil.getPath(id);
            BufferedImage chart = getChartImage(path);
            if (validateCoordinate(x, y, chart.getWidth(), chart.getHeight()))
                FilesUtil.insertPartBmp(chart, path, x, y, width, height, imageBytes);
            else
                throw new ValidationException("Некорректные координаты.");
        } else
            throw  new ValidationException("Некооретные размеры.");
    }

    //Удаляет файл
    public void deleteChart(String id) {
    }

    //Проверяет корректность переданных данных
    private boolean validateSize(int width, int height, int maxWidth, int maxHeight) {
        return width >= 0 && height >= 0 && width <= maxWidth && height <= maxHeight;
    }

    //Проверяет корректность переданных координат
    private boolean validateCoordinate(int x, int y, int fileWidth, int fileHeight) {
        return x <= fileWidth && y <= fileHeight && x >= 0 && y >= 0;
    }

    private BufferedImage getChartImage(Path path) throws IOException, ChartNotFoundException {
        if (FilesUtil.isBmpExists(path)) {
            return ImageIO.read(path.toFile());
        } else
            throw new ChartNotFoundException("Карта с id {" + path.getFileName() + "} не найдена.");
    }
}
