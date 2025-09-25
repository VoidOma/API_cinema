package org.example.tp_noteqd_cinema.repository;

import org.example.tp_noteqd_cinema.domain.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Long> {
}
