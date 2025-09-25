package org.example.tp_noteqd_cinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.tp_noteqd_cinema.domain.Seance;
import org.example.tp_noteqd_cinema.dto.SeanceCreateDto;
import org.example.tp_noteqd_cinema.service.SeanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seances")
@Tag(name = "Séances", description = "Gestion des séances")
public class SeancesController {

    private final SeanceService seanceService;

    public SeancesController(SeanceService seanceService) {
        this.seanceService = seanceService;
    }

    // JSON payload
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Ajouter une séance - JSON", operationId = "seancesAddJson")
    public ResponseEntity<Seance> addJson(@RequestBody @Validated SeanceCreateDto dto) {
        Seance created = seanceService.addSeance(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Form-data or x-www-form-urlencoded payload
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(summary = "Ajouter une séance - form-data/x-www-form-urlencoded", operationId = "seancesAddForm")
    public ResponseEntity<Seance> addForm(@ModelAttribute @Validated SeanceCreateDto dto) {
        Seance created = seanceService.addSeance(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
