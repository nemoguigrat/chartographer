package ru.ivanov.intern.chartographer.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "charts")
public class ChartFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer width;

    private Integer height;

    @OneToMany(mappedBy = "chart")
    private Set<ChartPart> parts = new HashSet<>();
}
