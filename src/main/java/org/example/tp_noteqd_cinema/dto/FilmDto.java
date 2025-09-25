package org.example.tp_noteqd_cinema.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Film DTO")
public class FilmDto {
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String genre;

    @Positive(message = "La durée doit être un entier positif")
    private Integer duree;

    private String dateSortie;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le prix doit être positif ou nul")
    private BigDecimal prix;

    private String realisateur;
}
