package org.example.tp_noteqd_cinema.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    private String genre;

    private Integer duree; // minutes

    private String dateSortie;

    private BigDecimal prix;

    private String realisateur;
}
