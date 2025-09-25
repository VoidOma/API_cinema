package org.example.tp_noteqd_cinema.repository;

import org.example.tp_noteqd_cinema.domain.Film;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FilmRepositoryTest {

    @Autowired
    private FilmRepository filmRepository;

    @Test
    void saveAndFindAll() {
        Film film = Film.builder()
                .titre("Inception")
                .genre("Sci-Fi")
                .duree(148)
                .prix(BigDecimal.valueOf(10))
                .realisateur("Christopher Nolan")
                .build();
        filmRepository.save(film);

        List<Film> films = filmRepository.findAll();
        assertThat(films).hasSize(1);
        assertThat(films.get(0).getTitre()).isEqualTo("Inception");
    }
}
