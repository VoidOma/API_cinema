package org.example.tp_noteqd_cinema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SeanceCreateDto {
    @NotBlank(message = "La date est obligatoire")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La date doit être au format ISO yyyy-MM-dd")
    private String date;

    @NotBlank(message = "L'heure est obligatoire")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "L'heure doit être au format HH:mm (00:00 à 23:59)")
    private String heure;

    @NotNull
    private Long salleId;
    @NotNull
    private Long filmId;
}
