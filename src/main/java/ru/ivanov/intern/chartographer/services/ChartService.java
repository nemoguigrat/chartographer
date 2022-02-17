package ru.ivanov.intern.chartographer.services;

import lombok.AllArgsConstructor;
import org.hibernate.id.UUIDGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ivanov.intern.chartographer.entity.ChartFile;
import ru.ivanov.intern.chartographer.entity.ChartPart;
import ru.ivanov.intern.chartographer.repository.ChartFileRepository;
import ru.ivanov.intern.chartographer.repository.ChartPartRepository;
import ru.ivanov.intern.chartographer.util.ChartUtil;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
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
    private final ChartFileRepository chartFileRepository;
    private final ChartPartRepository chartPartRepository;

    //Создает новое пустое изображение с заданными размерами и возвращает id изображения
    public Long createChart(int width, int height){
        //валидация значений
        return chartFileRepository.save(new ChartFile(width, height)).getId();
    }

    //Выдает кусок файла
    public void getChartPart(){

    }

    //Вставляет кусок файла
    public void insertChartPart(Long id, int x, int y, int width, int height, byte[] imageBytes) throws Exception {
        if (chartFileRepository.existsById(id)){
            ChartFile chartFile = chartFileRepository.findById(id).get();
            //получить размеры файла и валидироват значение validateCoordinate()
            ChartPart chartPart = chartPartRepository.save(new ChartPart(x, y, width, height));
            if (ChartUtil.saveBmp(chartPart.getId(), imageBytes)){
                chartFile.getParts().add(chartPart);
                chartFileRepository.save(chartFile);
            }
            else {
                // бросить исключение, что изображение не удается сохранить
                chartPartRepository.delete(chartPart);
            }
        }
    }

    //Удаляет файл
    public void deleteChart(String id){
    }

    //Проверяет корректность переданных данных
    private boolean validateSize(int width, int height){
        return false;
    }

    //Проверяет корректность переданных координат
    private boolean validateCoordinate(int x, int y, int fileWidth, int fileHeight){
        return false;
    }

//    //Ищет путь к файлу
//    private Path findFile(String id) throws Exception{
//        return ChartUtil.getPath()
//    }
}
