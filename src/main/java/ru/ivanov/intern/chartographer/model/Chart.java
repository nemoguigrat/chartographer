package ru.ivanov.intern.chartographer.model;

import lombok.Data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class Chart {
    private Integer id;

    private Integer width;

    private Integer height;

    private List<ChartPart> chartPart;

    public Chart(int width, int height){
        chartPart = new CopyOnWriteArrayList<>();
        this.width = width;
        this.height = height;
    }
}
