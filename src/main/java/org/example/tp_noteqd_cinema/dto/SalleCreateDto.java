package org.example.tp_noteqd_cinema.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SalleCreateDto {
    @NotBlank
    private String nom;
    @NotNull
    @Min(1)
    private Integer capacite;
}
