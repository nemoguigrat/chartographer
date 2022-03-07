package ru.ivanov.intern.chartographer.util;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ChartFilesUtils {
    private final String mediaFolder;

    public ChartFilesUtils(ApplicationArguments args) {
        this.mediaFolder = args.getSourceArgs()[0];
    }

    public void createBmp(int width, int height, String filename) throws IOException {
        Path folder = Paths.get(mediaFolder);
        if (!Files.exists(folder)) {
            Files.createDirectory(folder);
        }
        File file = Files.createFile(Paths.get(mediaFolder, filename + ".bmp")).toFile();
        ImageIO.write(
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB),
                "bmp",
                file);
    }

    public boolean deleteFile(String id) throws IOException {
        return Files.deleteIfExists(getFilePath(id));
    }

    public void insertPartBmp(BufferedImage bmpImage, File file, int x,
                              int y, int width, int height, byte[] imageBytes) throws IOException {
        BufferedImage imagePart = ImageIO.read(new ByteArrayInputStream(imageBytes));
        bmpImage.getGraphics().drawImage(imagePart, x, y, width, height, null);
        ImageIO.write(bmpImage, "bmp", file);
    }

    public byte[] getFilePart(BufferedImage file, int x, int y, int width, int height) throws IOException {
        int partWidth = Math.min(file.getWidth() - x, width);
        int partHeight = Math.min(file.getHeight() - y, height);

        BufferedImage imagePart = file.getSubimage(x, y, partWidth, partHeight);
        if (partWidth != width || partHeight != height) {
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            resizedImage.getGraphics().drawImage(imagePart, 0, 0, null);
            imagePart = resizedImage;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(imagePart, "bmp", stream);
        return stream.toByteArray();
    }

    public boolean isBmpExists(Path path) {
        return Files.exists(path);
    }

    public Path getFilePath(String filename) {
        return Paths.get(mediaFolder, filename + ".bmp");
    }
}
