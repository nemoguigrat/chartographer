package ru.ivanov.intern.chartographer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.exeption.ValidationException;
import ru.ivanov.intern.chartographer.services.ChartService;
import java.io.IOException;

@RestController
@AllArgsConstructor
public class ChartographerController {
    private final ChartService chartService;

    @RequestMapping(path = "/chartas/", method = RequestMethod.POST)
    public ResponseEntity<String> createChart(@RequestParam Integer width,
                                              @RequestParam Integer height) {
        try {
            return new ResponseEntity<>(chartService.createChart(width, height), HttpStatus.CREATED);
        } catch (ValidationException | IOException e) {
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
        } catch (ChartNotFoundException | IOException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ValidationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/chartas/{id}/", method = RequestMethod.GET)
    public ResponseEntity<?> getPartChart(@PathVariable String id,
                                      @RequestParam Integer x, @RequestParam Integer y,
                                      @RequestParam Integer width, @RequestParam Integer height) {
        try {
            byte[] bytes = chartService.getChartPart(id, x, y, width, height);
            return ResponseEntity.ok().body(bytes);
        } catch (ChartNotFoundException | IOException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ValidationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/chartas/{id}/", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePartFile(@PathVariable String id) {
        try {
            chartService.deleteChart(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ChartNotFoundException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
