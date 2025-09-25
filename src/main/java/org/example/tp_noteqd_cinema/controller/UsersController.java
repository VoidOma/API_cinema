package org.example.tp_noteqd_cinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.tp_noteqd_cinema.domain.Utilisateur;
import org.example.tp_noteqd_cinema.dto.UserRegistrationDto;
import org.example.tp_noteqd_cinema.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utilisateurs")
@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs et carte de fidélité")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    // JSON payload
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Enregistrer un utilisateur - JSON", operationId = "usersRegisterJson")
    public ResponseEntity<Utilisateur> registerJson(@RequestBody @Validated UserRegistrationDto dto) {
        Utilisateur created = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Form-data or x-www-form-urlencoded payload
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(summary = "Enregistrer un utilisateur - form-data/x-www-form-urlencoded", operationId = "usersRegisterForm")
    public ResponseEntity<Utilisateur> registerForm(@ModelAttribute @Validated UserRegistrationDto dto) {
        Utilisateur created = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{id}/carte")
    @Operation(summary = "Acheter une carte de fidélité (valable 1 an)", operationId = "usersBuyCard")
    public ResponseEntity<Utilisateur> buyCard(@PathVariable Long id) {
        Utilisateur updated = userService.buyLoyaltyCard(id);
        return ResponseEntity.ok(updated);
    }
}
