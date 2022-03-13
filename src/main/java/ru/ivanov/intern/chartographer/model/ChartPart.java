package ru.ivanov.intern.chartographer.model;

import lombok.Data;

@Data
public class ChartPart {
    private Integer id;

    private Integer x;

    private Integer y;

    private Integer width;

    private Integer height;

    public ChartPart(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
