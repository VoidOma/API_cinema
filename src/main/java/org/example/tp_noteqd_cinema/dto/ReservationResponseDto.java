package org.example.tp_noteqd_cinema.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ReservationResponseDto {
    private Long reservationId;
    private Long utilisateurId;
    private Long seanceId;
    private Integer nombrePlaces;
    private BigDecimal prixTotal;
    private boolean reductionAppliquee;
}
