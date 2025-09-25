package org.example.tp_noteqd_cinema.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String email;

    @Column(nullable = false)
    private String motDePasse;

    private boolean carteFideliteActive;

    private String carteExpiration; // optional if carte active (ISO date string)
}
