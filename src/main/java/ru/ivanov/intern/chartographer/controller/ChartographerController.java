package ru.ivanov.intern.chartographer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.exeption.ValidationException;
import ru.ivanov.intern.chartographer.service.ChartService;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class ChartographerController {
    private final ChartService chartService;

    /**
     * Создает пустую харту с заданными размерами и сохраняет на диске
     * @param width ширина харты
     * @param height высота харты
     * @return id изображения в строковом представлении
     * @throws ValidationException не корректные данные
     */
    @RequestMapping(path = "/chartas/", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Integer createEmptyChart(@RequestParam Integer width, @RequestParam Integer height)
            throws ValidationException, IOException {

        return chartService.createChart(width, height);
    }

    /**
     * Вставка изображения в харту с переданным идентификатором
     * @param id уникальный идентификатор харты
     * @param x координата харты, куда будет сохранен фрагмент
     * @param y координата харты, куда будет сохранен фрагмент
     * @param width ширина передаваемого изображения
     * @param height высота передаваемого изображения
     * @param image передаваемое изображение в формате BMP
     * @throws IOException ошибка чтения или записи файла
     * @throws ChartNotFoundException карта не найдена
     * @throws ValidationException не корректные данные
     */
    @RequestMapping(path = "/chartas/{id}/", method = RequestMethod.POST)
    public void savePartChart(@PathVariable Integer id, @RequestParam Integer x, @RequestParam Integer y,
                              @RequestParam Integer width, @RequestParam Integer height, @RequestBody byte[] image)
            throws IOException, ChartNotFoundException, ValidationException {

        chartService.insertPartChart(id, x, y, width, height, image);
    }

    /**
     * Получает фрагмент изображения из харты с переданным идентификатором
     * @param id уникальный идентификатор харты
     * @param x левовый верхний угол фрагмента
     * @param y правый верхний угол фрагмента
     * @param width ширина возвращаемого изображения
     * @param height высота возвращаемого изображения
     * @return изобаржение в формате BMP (поток байт)
     * @throws IOException ошибка чтения или записи файла
     * @throws ChartNotFoundException карта не найдена
     * @throws ValidationException не корректные данные
     */
    @RequestMapping(path = "/chartas/{id}/", method = RequestMethod.GET, produces = "image/bmp")
    public byte[] getPartChart(@PathVariable Integer id, @RequestParam Integer x, @RequestParam Integer y,
                               @RequestParam Integer width, @RequestParam Integer height)
            throws IOException, ChartNotFoundException, ValidationException {

        return chartService.getPartChart(id, x, y, width, height);
    }

    /**
     * Удаляет харту с переданным идентификатором
     * @param id уникальный идентификатор харты
     * @throws IOException ошибка чтения или записи файла
     * @throws ChartNotFoundException карта не найдена
     */
    @RequestMapping(path = "/chartas/{id}/", method = RequestMethod.DELETE)
    public void deleteChart(@PathVariable Integer id)
            throws IOException, ChartNotFoundException {

        chartService.deleteChart(id);
    }
}
