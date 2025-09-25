package org.example.tp_noteqd_cinema.repository;

import org.example.tp_noteqd_cinema.domain.Seance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeanceRepository extends JpaRepository<Seance, Long> {
    int countById(Long id);
}
