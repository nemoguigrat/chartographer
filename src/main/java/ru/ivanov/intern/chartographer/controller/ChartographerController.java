package ru.ivanov.intern.chartographer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.intern.chartographer.entity.ChartFile;
import ru.ivanov.intern.chartographer.repository.ChartFileRepository;
import ru.ivanov.intern.chartographer.services.ChartService;
import ru.ivanov.intern.chartographer.services.ChartService;

import java.io.IOException;
import java.util.Map;

@RestController
@AllArgsConstructor
public class ChartographerController {
    private final ChartService chartService;
    private final ChartFileRepository chartFileRepository;

    @RequestMapping(path = "/chartas/", method = RequestMethod.POST)
    public ResponseEntity<Long> createChart(@RequestParam Integer width,
                                              @RequestParam Integer height) {
        try {
            return new ResponseEntity<>(chartService.createChart(width, height), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/chartas/{id}/", method = RequestMethod.POST)
    public ResponseEntity<?> savePartChart(@PathVariable Long id, @RequestParam Integer x,
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
    public MultipartFile getPartChart(@PathVariable Long id,
                                      @RequestParam Integer x, @RequestParam Integer y,
                                      @RequestParam Integer width, @RequestParam Integer height) {
        return null;
    }

    @RequestMapping(path = "/chartas/{id}/", method = RequestMethod.DELETE)
    public void deletePartFile(@PathVariable Long id) {

    }

}
