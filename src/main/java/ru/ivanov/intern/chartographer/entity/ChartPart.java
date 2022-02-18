package ru.ivanov.intern.chartographer.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "parts")
public class ChartPart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer width;

    private Integer height;

    private Integer x;

    private Integer y;

    @ManyToOne
    private ChartFile chart;

    public ChartPart(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
