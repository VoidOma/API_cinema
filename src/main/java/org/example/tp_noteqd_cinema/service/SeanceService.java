package org.example.tp_noteqd_cinema.service;

import org.example.tp_noteqd_cinema.domain.Film;
import org.example.tp_noteqd_cinema.domain.Salle;
import org.example.tp_noteqd_cinema.domain.Seance;
import org.example.tp_noteqd_cinema.dto.SeanceCreateDto;
import org.example.tp_noteqd_cinema.exception.BadRequestException;
import org.example.tp_noteqd_cinema.exception.NotFoundException;
import org.example.tp_noteqd_cinema.repository.FilmRepository;
import org.example.tp_noteqd_cinema.repository.SalleRepository;
import org.example.tp_noteqd_cinema.repository.SeanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SeanceService {
    private final SeanceRepository seanceRepository;
    private final SalleRepository salleRepository;
    private final FilmRepository filmRepository;

    public SeanceService(SeanceRepository seanceRepository, SalleRepository salleRepository, FilmRepository filmRepository) {
        this.seanceRepository = seanceRepository;
        this.salleRepository = salleRepository;
        this.filmRepository = filmRepository;
    }

    public Seance addSeance(SeanceCreateDto dto) {
        Salle salle = salleRepository.findById(dto.getSalleId())
                .orElseThrow(() -> new NotFoundException("Salle introuvable"));
        Film film = filmRepository.findById(dto.getFilmId())
                .orElseThrow(() -> new NotFoundException("Film introuvable"));

        // Validate date is today or in the future
        LocalDate seanceDate;
        try {
            seanceDate = LocalDate.parse(dto.getDate());
        } catch (Exception e) {
            throw new BadRequestException("La date doit être au format ISO yyyy-MM-dd");
        }
        if (seanceDate.isBefore(LocalDate.now())) {
            throw new BadRequestException("La date de séance doit être aujourd'hui ou future");
        }

        Seance seance = Seance.builder()
                .date(dto.getDate())
                .heure(dto.getHeure())
                .salle(salle)
                .film(film)
                .build();
        return seanceRepository.save(seance);
    }

    public Seance getById(Long id) {
        return seanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Séance introuvable"));
    }
}
