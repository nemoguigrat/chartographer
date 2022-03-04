package ru.ivanov.intern.chartographer.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesUtil {
    public static void createBmp(int width, int height, String filename) throws IOException {
        Path mediaFolder = Paths.get(new ClassPathResource(".").getFile().getPath(), "/media");
        if (!Files.exists(mediaFolder))
            Files.createDirectory(mediaFolder);
        File file = Files.createFile(Paths.get(mediaFolder.toString(), filename + ".bmp")).toFile();
        ImageIO.write(
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB),
                "bmp",
                file);
    }

    public static boolean deleteFile(String id) throws IOException{
        return Files.deleteIfExists(getResource(id).getFile().toPath());
    }

    public static void insertPartBmp(BufferedImage bmpImage, File file, int x,
                                     int y, int width, int height, byte[] imageBytes) throws IOException {
        BufferedImage imagePart = ImageIO.read(new ByteArrayInputStream(imageBytes));
        Graphics graphics = bmpImage.getGraphics();
        graphics.drawImage(imagePart, x, y, width, height, null);
        ImageIO.write(bmpImage, "bmp", file);
    }

    public static byte[] getFilePart(BufferedImage file, int x, int y, int width, int height) throws IOException {
        int partWidth = Math.min(file.getWidth() - x, width);
        int partHeight = Math.min(file.getHeight() - y, height);

        BufferedImage imagePart = file.getSubimage(x, y, partWidth, partHeight);
        if (partWidth != width || partHeight != height){
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            resizedImage.getGraphics().drawImage(imagePart, 0, 0, null);
            imagePart = resizedImage;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(imagePart, "bmp", stream);
        return stream.toByteArray();
    }

    public static boolean isBmpExists(Resource resource) {
        return resource.exists();
    }

    public static Resource getResource(String filename) {
        return new ClassPathResource("/media/" + filename + ".bmp", FilesUtil.class.getClassLoader());
    }
}
