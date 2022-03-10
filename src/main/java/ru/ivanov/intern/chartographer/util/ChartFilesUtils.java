package ru.ivanov.intern.chartographer.util;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
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

    /**
     *
     * @param width
     * @param height
     * @param filename
     * @throws IOException
     */
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

    /**
     *
     * @param id
     * @return
     * @throws IOException
     */
    public boolean deleteFile(String id) throws IOException {
        return Files.deleteIfExists(getFilePath(id));
    }

    /**
     *
     * @param bmpImage
     * @param file
     * @param x
     * @param y
     * @param width
     * @param height
     * @param imageBytes
     * @throws IOException
     */
    public void insertPartBmp(BufferedImage bmpImage, File file, int x,
                              int y, int width, int height, byte[] imageBytes) throws IOException {
        ImageOutputStream output = ImageIO.createImageOutputStream(file);
        BufferedImage imagePart = ImageIO.read(new ByteArrayInputStream(imageBytes));
        bmpImage.getGraphics().drawImage(imagePart, x, y, width, height, null);
        ImageIO.write(bmpImage, "bmp", output);
        output.close();
    }

    /**
     *
     * @param file
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
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

    /**
     *
     * @param path
     * @return
     */
    public boolean isBmpExists(Path path) {
        return Files.exists(path);
    }

    /**
     *
     * @param filename
     * @return
     */
    public Path getFilePath(String filename) {
        return Paths.get(mediaFolder, filename + ".bmp");
    }
}
