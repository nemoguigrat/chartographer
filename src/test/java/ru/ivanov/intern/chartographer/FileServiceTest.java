package ru.ivanov.intern.chartographer;

import ch.qos.logback.core.util.FileUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.core.io.ClassPathResource;
import ru.ivanov.intern.chartographer.model.Chart;
import ru.ivanov.intern.chartographer.model.ChartPart;
import ru.ivanov.intern.chartographer.service.FileService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class FileServiceTest {
    private final FileService fileService;
    private final String mediaFolder;

    public FileServiceTest() throws IOException {
        this.mediaFolder = Paths.get(new ClassPathResource(".").getFile().getPath(), "media").toString();
        this.fileService = new FileService(new DefaultApplicationArguments(mediaFolder));
    }

    @BeforeEach
    public void init() throws IOException {
        fileService.init();
    }

    @Test
    public void testSaveImage() throws IOException {
        byte[] testImage = createTestImageToInsert(10, 10, Color.white);
        fileService.createChartFolder(0);
        fileService.saveImage(0, 0, testImage);
        Assertions.assertTrue(Files.exists(Paths.get(mediaFolder, String.valueOf(0), 0 + ".bmp")));
    }

    @Test
    public void testCreateOutputImage() throws IOException {
        Chart chart = new Chart(100, 100);
        chart.setId(1);
        fileService.createChartFolder(1);
        List<ChartPart> chartPartData = new ArrayList<>();
        Color[] colors = new Color[] {Color.white, Color.blue, Color.cyan, Color.red, Color.green};
        for (int i = 0; i < 5; i++){
            ChartPart chartPart = new ChartPart(i + 10,  i + 10,20, 20);
            chartPart.setId(i + 100);
            chartPartData.add(chartPart);
            byte[] bytes = createTestImageToInsert(5, 5, colors[i]);
            fileService.saveImage(1, i + 100, bytes);
        }
        chart.setChartPart(chartPartData);
        byte[] output = fileService.createOutputImage(chart, 0, 0, 100, 100);
        BufferedImage outputImage = ImageIO.read(new ByteArrayInputStream(output));
        for (var i = 0; i < 5; i++) {
            Assertions.assertEquals(colors[i].getRGB(),
                    outputImage.getRGB(chartPartData.get(i).getX(),chartPartData.get(i).getY()));
        }
    }

    @Test
    public void testDeleteDirectory() throws IOException {
        Path path = Paths.get(mediaFolder, "1000000");
        Files.createDirectory(path);
        Assertions.assertTrue(Files.exists(path));
        fileService.deleteDirectory(1000000);
        Assertions.assertFalse(Files.exists(path));
    }

    @AfterEach
    public void destroy() throws IOException {
        FileUtils.deleteDirectory(Paths.get(mediaFolder).toFile());
    }

    private byte[] createTestImageToInsert(int insertWidth, int insertHeight, Color color) throws IOException {
        BufferedImage imageToInsert = new BufferedImage(insertWidth, insertHeight, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < imageToInsert.getWidth(); x++)
            for (int y = 0; y < imageToInsert.getHeight(); y++)
                imageToInsert.setRGB(x, y, color.getRGB());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(imageToInsert, "bmp", stream);
        return stream.toByteArray();
    }
}