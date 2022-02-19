package ru.ivanov.intern.chartographer.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesUtil {
    private static final String MEDIA = "src/main/resources/media";

    public static void createBmp(int width, int height, String filename) throws IOException {
        File file = new File(MEDIA + "/" + filename + ".bmp");
        ImageIO.write(
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB),
                "bmp",
                file);
    }

    public static void insertPartBmp(BufferedImage file, Path filePath, int x,
                                     int y, int width, int height, byte[] imageBytes) throws IOException {
        BufferedImage imagePart = ImageIO.read(new ByteArrayInputStream(imageBytes));
        Graphics graphics = file.getGraphics();
        graphics.drawImage(imagePart, x, y, width, height, null);
        ImageIO.write(file, "bmp", filePath.toFile());
    }

    public static byte[] getFilePart(BufferedImage file, int x,
                                     int y, int width, int height) throws IOException {
        BufferedImage imagePart = file.getSubimage(x, y, width, height);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(imagePart, "bmp", stream);
        return stream.toByteArray();
    }

    public static boolean isBmpExists(String filename) {
        return Files.exists(getPath(filename));
    }

    public static boolean isBmpExists(Path path) {
        return Files.exists(path);
    }

    public static Path getPath(String filename) {
        return Paths.get(MEDIA + "/" + filename + ".bmp");
    }
}
