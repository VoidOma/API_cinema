package org.example.tp_noteqd_cinema.service;

import org.example.tp_noteqd_cinema.domain.Reservation;
import org.example.tp_noteqd_cinema.domain.Seance;
import org.example.tp_noteqd_cinema.domain.Utilisateur;
import org.example.tp_noteqd_cinema.dto.ReservationRequestDto;
import org.example.tp_noteqd_cinema.dto.ReservationResponseDto;
import org.example.tp_noteqd_cinema.exception.CapacityExceededException;
import org.example.tp_noteqd_cinema.exception.NotFoundException;
import org.example.tp_noteqd_cinema.repository.ReservationRepository;
import org.example.tp_noteqd_cinema.repository.SeanceRepository;
import org.example.tp_noteqd_cinema.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final SeanceRepository seanceRepository;

    public ReservationService(ReservationRepository reservationRepository, UtilisateurRepository utilisateurRepository, SeanceRepository seanceRepository) {
        this.reservationRepository = reservationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.seanceRepository = seanceRepository;
    }

    public ReservationResponseDto reserver(ReservationRequestDto request) {
        Utilisateur user = utilisateurRepository.findById(request.getUtilisateurId())
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));
        Seance seance = seanceRepository.findById(request.getSeanceId())
                .orElseThrow(() -> new NotFoundException("Séance introuvable"));

        int dejaReservees = reservationRepository.sumNombrePlacesBySeance(seance);
        int capacite = seance.getSalle().getCapacite();
        int restantes = capacite - dejaReservees;
        if (request.getNombrePlaces() > restantes) {
            throw new CapacityExceededException("Capacité dépassée: places restantes=" + restantes);
        }

        Reservation reservation = Reservation.builder()
                .utilisateur(user)
                .seance(seance)
                .nombrePlaces(request.getNombrePlaces())
                .build();
        Reservation saved = reservationRepository.save(reservation);

        BigDecimal unitPrice = seance.getFilm().getPrix();
        BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(request.getNombrePlaces()));
        boolean reduction = user.isCarteFideliteActive() && (user.getCarteExpiration() == null || !LocalDate.parse(user.getCarteExpiration()).isBefore(LocalDate.now()));
        if (reduction) {
            total = total.multiply(BigDecimal.valueOf(0.9)); // 10% de réduction
        }
        total = total.setScale(2, RoundingMode.HALF_UP);

        return ReservationResponseDto.builder()
                .reservationId(saved.getId())
                .utilisateurId(user.getId())
                .seanceId(seance.getId())
                .nombrePlaces(saved.getNombrePlaces())
                .prixTotal(total)
                .reductionAppliquee(reduction)
                .build();
    }
}
