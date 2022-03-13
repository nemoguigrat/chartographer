package ru.ivanov.intern.chartographer.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.exeption.ValidationException;
import ru.ivanov.intern.chartographer.model.Chart;
import ru.ivanov.intern.chartographer.model.ChartPart;
import ru.ivanov.intern.chartographer.resository.ChartPartRepository;
import ru.ivanov.intern.chartographer.resository.ChartRepository;

import java.io.IOException;

@Service
@AllArgsConstructor
public class ChartService {
    private final FileService fileService;
    private final ChartRepository chartRepository;
    private final ChartPartRepository chartPartRepository;

    /**
     * Создает карту с переданными размерами и сохраняет в репозиторий (данные хранятся в памяти)
     * @param width ширина карты
     * @param height высота карты
     * @return уникальный идентификатор карты
     * @throws ValidationException не корректные координаты
     * @throws IOException ошибка чтения или записи файла
     */
    public int createChart(int width, int height) throws ValidationException, IOException {
        if (validateSize(width, height, 20000, 50000)) {
            Chart chart = chartRepository.save(new Chart(width, height));
            fileService.createChartFolder(chart.getId());
            return chart.getId();
        } else {
            throw new ValidationException("Некорректные размеры.");
        }
    }

    /**
     * Сохраняет фрагмент на диске, а информацию о нем в репозитории (хранится в памяти)
     * @param id уникальный идентификатор карты
     * @param x координата карты, куда будет сохранен фрагмент
     * @param y координата карты, куда будет сохранен фрагмент
     * @param width ширина передаваемого изображения
     * @param height высота передаваемого изображения
     * @param imageBytes передаваемое изображение в формате BMP
     * @throws ChartNotFoundException файла с переданным id не существует
     * @throws ValidationException не корректные координаты
     * @throws IOException ошибка чтения или записи файла
     */
    public void insertPartChart(int id, int x, int y, int width, int height, byte[] imageBytes)
            throws ChartNotFoundException, ValidationException, IOException {
        if (validateSize(width, height, 20000, 50000)) {
            Chart chart = chartRepository.get(id);
            if (validateCoordinate(x, y, chart.getWidth(), chart.getHeight())) {
                int partWidth = Math.min(chart.getWidth() - x, width);
                int partHeight = Math.min(chart.getHeight() - y, height);

                ChartPart chartPart = chartPartRepository.save(new ChartPart(x, y, partWidth, partHeight));
                chart.getChartPart().add(chartPart);
                fileService.saveImage(chart.getId(), chartPart.getId(), imageBytes);
            } else {
                throw new ValidationException("Некорректные координаты.");
            }
        } else {
            throw new ValidationException("Некорректные размеры.");
        }
    }

    /**
     * Получает фрагмент карты с переданными координатами и размерами
     * @param id уникальный идентификатор карты
     * @param x левовый верхний угол фрагмента
     * @param y правый верхний угол фрагмента
     * @param width ширина возвращаемого изображения
     * @param height высота возвращаемого изображения
     * @return изобаржение в формате BMP (поток байт)
     * @throws ChartNotFoundException файла с переданным id не существует
     * @throws IOException ошибка чтения или записи файла
     * @throws ValidationException не корректные координаты
     */
    public byte[] getPartChart(int id, int x, int y, int width, int height)
            throws ChartNotFoundException, IOException, ValidationException {
        if (validateSize(width, height, 5000, 5000)) {
            Chart chart = chartRepository.get(id);

            if (validateCoordinate(x, y, chart.getWidth(), chart.getHeight())) {
                return fileService.createOutputImage(chart, x, y, width, height);
            } else {
                throw new ValidationException("Некорректные координаты.");
            }
        } else {
            throw new ValidationException("Некорректные размеры.");
        }
    }

    /**
     * Удаляет карту по переданному идентификатору
     * @param id уникальный идентификатор изображения
     * @throws ChartNotFoundException файла с переданным id не существует
     * @throws IOException ошибка чтения или записи файла
     */
    public void deleteChart(int id) throws ChartNotFoundException, IOException {
        chartRepository.delete(id);
        if (!fileService.deleteDirectory(id)) {
            throw new ChartNotFoundException("Файла с id {" + id + "} не существует.");
        }
    }

    private boolean validateSize(int width, int height, int maxWidth, int maxHeight) {
        return width > 0 && height > 0 && width <= maxWidth && height <= maxHeight;
    }

    private boolean validateCoordinate(int x, int y, int fileWidth, int fileHeight) {
        return x < fileWidth && y < fileHeight && x >= 0 && y >= 0;
    }
}
