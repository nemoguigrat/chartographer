package ru.ivanov.intern.chartographer.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChartUtil {
    private static final String MEDIA = "src/main/resources/media";

    public static boolean saveBmp(Long chartPartId, byte[] imageBytes){
        File file = new File(MEDIA + "/" + chartPartId + ".bmp");
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            ImageIO.write(image, "bmp", file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isBmpExists(String filename){
        return Files.exists(getPath(filename));
    }

    public static boolean isBmpExists(Path path){
        return Files.exists(path);
    }

    public static Path getPath(String filename){
        return Paths.get(MEDIA + "/" + filename + ".bmp");
    }

//    public static void insertPartToImage(int x, int y, int width, int height,
//                                         BufferedImage chart, BufferedImage part){
//        Path path = ChartUtil.getPath(id);
//        if (ChartUtil.isBmpExists(path)){
//            BufferedImage chart = ImageIO.read(path.toFile());
//            // валидация -> выкинуть кастомные ошибки о выполнении
//            BufferedImage chartPart = ImageIO.read(new ByteArrayInputStream(imageBytes));
//            // валидация -> выкинуть кастомные ошибки о выполнении
//            Graphics g = chart.getGraphics();
//            g.drawImage(chartPart, x, y, width, height, null);
//            ImageIO.write(chart, "bmp", path.toFile());
//        }
//        else
//            throw new Exception();
//    }
}
