package org.example.tp_noteqd_cinema.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    private String heure;

    @ManyToOne(optional = false)
    private Salle salle;

    @ManyToOne(optional = false)
    private Film film;
}
