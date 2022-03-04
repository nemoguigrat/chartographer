package ru.ivanov.intern.chartographer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.intern.chartographer.exeption.ChartNotFoundException;
import ru.ivanov.intern.chartographer.exeption.ValidationException;
import ru.ivanov.intern.chartographer.service.ChartService;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class ChartographerController {
    private final ChartService chartService;

    @RequestMapping(path = "/chartas/", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public String createChart(@RequestParam Integer width, @RequestParam Integer height)
            throws IOException, ValidationException {

        return chartService.createChart(width, height);
    }

    @RequestMapping(path = "/chartas/{id}/", method = RequestMethod.POST)
    public void savePartChart(@PathVariable String id, @RequestParam Integer x, @RequestParam Integer y,
                              @RequestParam Integer width, @RequestParam Integer height, @RequestBody byte[] image)
            throws IOException, ChartNotFoundException, ValidationException {

        chartService.insertChartPart(id, x, y, width, height, image);
    }

    @RequestMapping(path = "/chartas/{id}/", method = RequestMethod.GET, produces = "image/bmp")
    public byte[] getPartChart(@PathVariable String id, @RequestParam Integer x, @RequestParam Integer y,
                               @RequestParam Integer width, @RequestParam Integer height)
            throws IOException, ChartNotFoundException, ValidationException {

        return chartService.getChartPart(id, x, y, width, height);
    }

    @RequestMapping(path = "/chartas/{id}/", method = RequestMethod.DELETE)
    public void deletePartFile(@PathVariable String id)
            throws IOException, ChartNotFoundException {

        chartService.deleteChart(id);
    }

}
