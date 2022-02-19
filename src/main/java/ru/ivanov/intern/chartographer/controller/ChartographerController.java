package ru.ivanov.intern.chartographer.controller;

import jdk.jfr.ContentType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.intern.chartographer.services.ChartService;
import ru.ivanov.intern.chartographer.services.ChartService;

import javax.imageio.stream.ImageOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@AllArgsConstructor
public class ChartographerController {
    private final ChartService chartService;

    @RequestMapping(path = "/chartas/", method = RequestMethod.POST)
    public ResponseEntity<String> createChart(@RequestParam Integer width,
                                              @RequestParam Integer height) {
        try {
            return new ResponseEntity<>(chartService.createChart(width, height), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/chartas/{id}/", method = RequestMethod.POST)
    public ResponseEntity<?> savePartChart(@PathVariable String id, @RequestParam Integer x,
                                           @RequestParam Integer y, @RequestParam Integer width,
                                           @RequestParam Integer height, @RequestBody byte[] image) {
        try {
            chartService.insertChartPart(id, x, y, width, height, image);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/chartas/{id}/", method = RequestMethod.GET)
    public ResponseEntity<?> getPartChart(@PathVariable String id,
                                      @RequestParam Integer x, @RequestParam Integer y,
                                      @RequestParam Integer width, @RequestParam Integer height) {
        byte[] bytes = chartService.getChartPart(id, x, y, width, height);
        return ResponseEntity.ok().body(bytes);
    }

    @RequestMapping(path = "/chartas/{id}/", method = RequestMethod.DELETE)
    public void deletePartFile(@PathVariable String id) {

    }

}
