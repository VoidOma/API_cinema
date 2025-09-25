package org.example.tp_noteqd_cinema.service;

import org.example.tp_noteqd_cinema.domain.Film;
import org.example.tp_noteqd_cinema.dto.FilmDto;
import org.example.tp_noteqd_cinema.dto.FilmDtoMapper;
import org.example.tp_noteqd_cinema.repository.FilmRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<FilmDto> listFilms() {
        return filmRepository.findAll().stream()
                .map(FilmDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    public FilmDto addFilm(FilmDto dto) {
        Film film = FilmDtoMapper.toEntity(dto);
        Film saved = filmRepository.save(film);
        return FilmDtoMapper.toDto(saved);
    }
}
