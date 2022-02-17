package ru.ivanov.intern.chartographer.services;

import lombok.AllArgsConstructor;
import org.hibernate.id.UUIDGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ivanov.intern.chartographer.entity.ChartFile;
import ru.ivanov.intern.chartographer.repository.ChartFileRepository;
import ru.ivanov.intern.chartographer.util.ChartUtil;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChartService {

    //Создает новое пустое изображение с заданными размерами и возвращает id изображения
    public String createChart(int width, int height) throws IOException {
        //валидация значений
        String id = UUID.randomUUID().toString();
        ChartUtil.createBmp(width, height, id);
        return id;
    }

    //Выдает кусок файла
    public void getChartPart(){

    }

    //Вставляет кусок файла ----
    // валидация(Exception) ->
    // найти файл(Exception) ->
    // отдать файл для изменения вместе с куском харты(Exception)
    // TODO декомпозировать метод, перенести функционал в утилы
    public void insertChartPart(String id, int x, int y, int width, int height, byte[] imageBytes) throws Exception {
        Path path = ChartUtil.getPath(id);
        if (ChartUtil.isBmpExists(path)){
            BufferedImage chart = ImageIO.read(path.toFile());
            // валидация -> выкинуть кастомные ошибки о выполнении
            BufferedImage chartPart = ImageIO.read(new ByteArrayInputStream(imageBytes));
            // валидация -> выкинуть кастомные ошибки о выполнении
            Graphics g = chart.getGraphics();
            g.drawImage(chartPart, x, y, width, height, null);
            ImageIO.write(chart, "bmp", path.toFile());
        }
        else
            throw new Exception();
    }

    //Удаляет файл
    public void deleteChart(String id){
    }

    //Проверяет корректность переданных данных
    private boolean validateSize(int width, int height){
        return false;
    }

    //Проверяет корректность переданных координат
    private boolean validateCoordinate(){
        return false;
    }

//    //Ищет путь к файлу
//    private Path findFile(String id) throws Exception{
//        return ChartUtil.getPath()
//    }
}
