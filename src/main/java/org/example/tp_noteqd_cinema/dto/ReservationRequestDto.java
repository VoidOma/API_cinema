package org.example.tp_noteqd_cinema.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Réservation Request DTO")
public class ReservationRequestDto {
    @NotNull
    private Long utilisateurId;
    @NotNull
    private Long seanceId;
    @NotNull
    @Min(1)
    private Integer nombrePlaces;
}
