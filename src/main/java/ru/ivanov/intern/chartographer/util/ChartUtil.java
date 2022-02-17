package ru.ivanov.intern.chartographer.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChartUtil {
    private static final String MEDIA = "src/main/resources/media";

    public static void createBmp(int width, int height, String filename) throws IOException {
        File file = new File(MEDIA + "/" + filename + ".bmp");
        ImageIO.write(
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB ),
                "bmp",
                file);
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

    public static void insertPartToImage(int x, int y, int width, int height,
                                         BufferedImage chart, BufferedImage part){

    }
}
