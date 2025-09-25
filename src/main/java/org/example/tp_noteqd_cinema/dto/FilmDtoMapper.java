package org.example.tp_noteqd_cinema.dto;

import org.example.tp_noteqd_cinema.domain.Film;

public class FilmDtoMapper {
    public static FilmDto toDto(Film film) {
        if (film == null) return null;
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setTitre(film.getTitre());
        dto.setGenre(film.getGenre());
        dto.setDuree(film.getDuree());
        dto.setDateSortie(film.getDateSortie());
        dto.setPrix(film.getPrix());
        dto.setRealisateur(film.getRealisateur());
        return dto;
    }

    public static Film toEntity(FilmDto dto) {
        if (dto == null) return null;
        return Film.builder()
                .id(dto.getId())
                .titre(dto.getTitre())
                .genre(dto.getGenre())
                .duree(dto.getDuree())
                .dateSortie(dto.getDateSortie())
                .prix(dto.getPrix())
                .realisateur(dto.getRealisateur())
                .build();
    }
}
