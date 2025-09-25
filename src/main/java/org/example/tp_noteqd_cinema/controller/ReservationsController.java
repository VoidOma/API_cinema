package org.example.tp_noteqd_cinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.tp_noteqd_cinema.dto.ReservationRequestDto;
import org.example.tp_noteqd_cinema.dto.ReservationResponseDto;
import org.example.tp_noteqd_cinema.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Réservations", description = "Gestion des réservations")
public class ReservationsController {

    private final ReservationService reservationService;

    public ReservationsController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // JSON payload
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Réserver des places pour une séance - JSON", operationId = "reservationsReserverJson")
    public ResponseEntity<ReservationResponseDto> reserverJson(@RequestBody @Validated ReservationRequestDto dto) {
        ReservationResponseDto response = reservationService.reserver(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Form-data or x-www-form-urlencoded payload
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(summary = "Réserver des places pour une séance - form-data/x-www-form-urlencoded", operationId = "reservationsReserverForm")
    public ResponseEntity<ReservationResponseDto> reserverForm(@ModelAttribute @Validated ReservationRequestDto dto) {
        ReservationResponseDto response = reservationService.reserver(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
