package org.example.tp_noteqd_cinema.service;

import org.example.tp_noteqd_cinema.domain.Salle;
import org.example.tp_noteqd_cinema.dto.SalleCreateDto;
import org.example.tp_noteqd_cinema.repository.SalleRepository;
import org.springframework.stereotype.Service;

@Service
public class SalleService {
    private final SalleRepository salleRepository;

    public SalleService(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }

    public Salle addSalle(SalleCreateDto dto) {
        Salle salle = Salle.builder()
                .nom(dto.getNom())
                .capacite(dto.getCapacite())
                .build();
        return salleRepository.save(salle);
    }
}
