package org.example.tp_noteqd_cinema.repository;

import org.example.tp_noteqd_cinema.domain.Reservation;
import org.example.tp_noteqd_cinema.domain.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("select coalesce(sum(r.nombrePlaces),0) from Reservation r where r.seance = :seance")
    int sumNombrePlacesBySeance(@Param("seance") Seance seance);
}
