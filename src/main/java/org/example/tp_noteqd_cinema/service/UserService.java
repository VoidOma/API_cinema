package org.example.tp_noteqd_cinema.service;

import org.example.tp_noteqd_cinema.domain.Utilisateur;
import org.example.tp_noteqd_cinema.dto.UserRegistrationDto;
import org.example.tp_noteqd_cinema.exception.BadRequestException;
import org.example.tp_noteqd_cinema.exception.NotFoundException;
import org.example.tp_noteqd_cinema.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {
    private final UtilisateurRepository utilisateurRepository;

    public UserService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public Utilisateur register(UserRegistrationDto dto) {
        utilisateurRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new BadRequestException("Email déjà utilisé");
        });
        Utilisateur user = Utilisateur.builder()
                .nom(dto.getNom())
                .email(dto.getEmail())
                .motDePasse(dto.getMotDePasse())
                .carteFideliteActive(false)
                .build();
        return utilisateurRepository.save(user);
    }

    public Utilisateur buyLoyaltyCard(Long userId) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));
        user.setCarteFideliteActive(true);
        user.setCarteExpiration(LocalDate.now().plusYears(1).toString());
        return utilisateurRepository.save(user);
    }
}
