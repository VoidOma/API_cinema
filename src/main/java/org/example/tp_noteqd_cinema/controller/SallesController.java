package org.example.tp_noteqd_cinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.tp_noteqd_cinema.domain.Salle;
import org.example.tp_noteqd_cinema.dto.SalleCreateDto;
import org.example.tp_noteqd_cinema.exception.ForbiddenException;
import org.example.tp_noteqd_cinema.service.SalleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salles")
@Tag(name = "Salles", description = "Gestion des salles (admin)")
public class SallesController {

    private final SalleService salleService;

    public SallesController(SalleService salleService) {
        this.salleService = salleService;
    }

    // JSON payload
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Ajouter une salle de projection (admin) - JSON", operationId = "sallesAddJson")
    public ResponseEntity<Salle> addJson(@RequestHeader(name = "X-ROLE", required = false) String role,
                                         @RequestBody @Validated SalleCreateDto dto) {
        requireAdmin(role);
        Salle created = salleService.addSalle(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Form-data or x-www-form-urlencoded payload
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(summary = "Ajouter une salle de projection (admin) - form-data/x-www-form-urlencoded", operationId = "sallesAddForm")
    public ResponseEntity<Salle> addForm(@RequestHeader(name = "X-ROLE", required = false) String role,
                                         @ModelAttribute @Validated SalleCreateDto dto) {
        requireAdmin(role);
        Salle created = salleService.addSalle(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    private void requireAdmin(String role) {
        if (role == null) {
            throw new ForbiddenException("Rôle requis: ADMIN (header X-ROLE)");
        }
        if (!"ADMIN".equalsIgnoreCase(role.trim())) {
            throw new ForbiddenException("Accès interdit: rôle requis ADMIN");
        }
    }
}
