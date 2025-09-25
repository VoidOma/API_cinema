package org.example.tp_noteqd_cinema.controller;

import org.example.tp_noteqd_cinema.dto.FilmDto;
import org.example.tp_noteqd_cinema.exception.GlobalExceptionHandler;
import org.example.tp_noteqd_cinema.service.FilmService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmsController.class)
@Import(GlobalExceptionHandler.class)
class FilmsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @Test
    @DisplayName("GET /api/films returns list of films")
    void listFilms_ok() throws Exception {
        FilmDto dto = new FilmDto();
        dto.setId(1L);
        dto.setTitre("Inception");
        dto.setGenre("Sci-Fi");
        dto.setDuree(148);
        dto.setDateSortie("2010-07-16");
        dto.setPrix(BigDecimal.valueOf(12.5));
        dto.setRealisateur("Christopher Nolan");
        when(filmService.listFilms()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/films"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titre", is("Inception")))
                .andExpect(jsonPath("$[0].prix", is(12.5)));
    }

    @Test
    @DisplayName("POST /api/films without X-ROLE returns 403 Forbidden from GlobalExceptionHandler")
    void addFilm_missingRole_forbidden() throws Exception {
        String body = "{" +
                "\"titre\":\"Tenet\"," +
                "\"genre\":\"Sci-Fi\"," +
                "\"duree\":150," +
                "\"dateSortie\":\"2020-08-26\"," +
                "\"prix\":10.0," +
                "\"realisateur\":\"Christopher Nolan\"" +
                "}";

        mockMvc.perform(post("/api/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("Rôle requis")));
    }

    @Test
    @DisplayName("POST /api/films with role PRODUCTEUR returns 201 and created film")
    void addFilm_withRole_created() throws Exception {
        FilmDto created = new FilmDto();
        created.setId(42L);
        created.setTitre("Tenet");
        created.setPrix(BigDecimal.TEN);
        when(filmService.addFilm(Mockito.any(FilmDto.class))).thenReturn(created);

        String body = "{" +
                "\"titre\":\"Tenet\"," +
                "\"prix\":10.0" +
                "}";

        mockMvc.perform(post("/api/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-ROLE", "PRODUCTEUR")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(42)))
                .andExpect(jsonPath("$.titre", is("Tenet")));
    }
}
