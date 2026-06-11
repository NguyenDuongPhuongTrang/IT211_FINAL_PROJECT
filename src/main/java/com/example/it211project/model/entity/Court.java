package com.example.it211project.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "courts")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courtName;
    private String location;
    private Double price;

    @OneToMany(
            mappedBy = "court",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CourtImage> images;
}
