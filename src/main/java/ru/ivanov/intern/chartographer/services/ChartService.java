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
        if (!validateSize(width, height))
            throw new ValidationException("Не корректные размеры.");
        String id = UUID.randomUUID().toString();
        FilesUtil.createBmp(width, height, id);
        return id;
    }

    //Выдает кусок файла
    public byte[] getChartPart(String id, int x, int y, int width, int height) {
        // найти карту в базе данных
        // проверить, что размеры не выходят за изображение (валидация данных)
        // проверить, какие изображения входят в область передачи
        // вытащить эти изображения и в порядке их индексов сформировать изображение прямо в памяти
        // вернуть потом байтов
        return new byte[0];
    }

    //Вставляет кусок файла
    public void insertChartPart(String id, int x, int y, int width, int height, byte[] imageBytes)
            throws ChartNotFoundException, ValidationException, IOException {
        Path path = FilesUtil.getPath(id);
        if (FilesUtil.isBmpExists(path)) {
            BufferedImage chart = ImageIO.read(path.toFile());
            if (validateCoordinate(x, y, chart.getWidth(), chart.getHeight()))
                FilesUtil.insertPartBmp(chart, path, x, y, width, height, imageBytes);
            else
                throw new ValidationException("Некорректные координаты.");
        } else
            throw new ChartNotFoundException("Карта с id {" + id + "} не найдена.");
    }

    //Удаляет файл
    public void deleteChart(String id) {
    }

    //Проверяет корректность переданных данных
    private boolean validateSize(int width, int height) {
        return width >= 0 && height >= 0 && width <= 20000 && height <= 50000;
    }

    //Проверяет корректность переданных координат
    private boolean validateCoordinate(int x, int y, int fileWidth, int fileHeight) {
        return x <= fileWidth && y <= fileHeight && x >= 0 && y >= 0;
    }
}
