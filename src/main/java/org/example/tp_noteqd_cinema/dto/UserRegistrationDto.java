package org.example.tp_noteqd_cinema.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @NotBlank
    private String nom;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String motDePasse;
}
