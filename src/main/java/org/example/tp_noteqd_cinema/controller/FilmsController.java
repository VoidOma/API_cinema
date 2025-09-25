package org.example.tp_noteqd_cinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.tp_noteqd_cinema.dto.FilmDto;
import org.example.tp_noteqd_cinema.exception.ForbiddenException;
import org.example.tp_noteqd_cinema.service.FilmService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/films")
@Tag(name = "Films", description = "Gestion des films")
public class FilmsController {

    private final FilmService filmService;

    public FilmsController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    @Operation(summary = "Lister les films", operationId = "filmsList")
    public ResponseEntity<List<FilmDto>> list() {
        return ResponseEntity.ok(filmService.listFilms());
    }

    // JSON payload
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Ajouter un film (producteur/distributeur) - JSON", operationId = "filmsAddJson")
    public ResponseEntity<FilmDto> addJson(@RequestHeader(name = "X-ROLE", required = false) String role,
                                           @RequestBody @Valid FilmDto dto) {
        requireProducerOrDistributor(role);
        FilmDto created = filmService.addFilm(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Form-data or x-www-form-urlencoded payload
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(summary = "Ajouter un film (producteur/distributeur) - form-data/x-www-form-urlencoded", operationId = "filmsAddForm")
    public ResponseEntity<FilmDto> addForm(@RequestHeader(name = "X-ROLE", required = false) String role,
                                           @ModelAttribute @Valid FilmDto dto) {
        requireProducerOrDistributor(role);
        FilmDto created = filmService.addFilm(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    private void requireProducerOrDistributor(String role) {
        if (role == null) {
            throw new ForbiddenException("Rôle requis: PRODUCTEUR ou DISTRIBUTEUR (header X-ROLE)");
        }
        String r = role.trim().toUpperCase();
        if (!("PRODUCTEUR".equals(r) || "DISTRIBUTEUR".equals(r))) {
            throw new ForbiddenException("Accès interdit: rôle requis PRODUCTEUR ou DISTRIBUTEUR");
        }
    }
}
