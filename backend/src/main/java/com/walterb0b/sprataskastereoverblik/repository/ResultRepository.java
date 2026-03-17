package com.walterb0b.sprataskastereoverblik.repository;

import com.walterb0b.sprataskastereoverblik.model.Athlete;
import com.walterb0b.sprataskastereoverblik.model.Discipline;
import com.walterb0b.sprataskastereoverblik.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByAthleteId(Long athleteId);
    List<Result> findByAthleteIdOrderByResultDateDesc(Long athleteId);
    boolean existsByAthleteAndDisciplineAndResultDateAndResultValue(
            Athlete athlete,
            Discipline discipline,
            LocalDate resultDate,
            Double resultValue
    );

    @Modifying
    @Query(value = "TRUNCATE TABLE results RESTART IDENTITY", nativeQuery = true)
    void truncate();

    @Query(value = """
        SELECT
            a.id AS athleteId,
            a.name AS athleteName,
            MAX(CASE WHEN r.discipline = 'SHOT_PUT' THEN r.result_value END) AS shotPut,
            MAX(CASE WHEN r.discipline = 'DISCUS' THEN r.result_value END) AS discus,
            MAX(CASE WHEN r.discipline = 'HAMMER' THEN r.result_value END) AS hammer,
            MAX(CASE WHEN r.discipline = 'JAVELIN' THEN r.result_value END) AS javelin,
            MAX(CASE WHEN r.discipline = 'WEIGHT_THROW' THEN r.result_value END) AS weightThrow
        FROM athletes a
        LEFT JOIN results r ON r.athlete_id = a.id
        GROUP BY a.id, a.name
        ORDER BY a.name
        """, nativeQuery = true)
        List<OverviewProjection> getPrOverview();

        @Query(value = """
        SELECT
            a.id AS athleteId,
            a.name AS athleteName,
            MAX(CASE WHEN r.discipline = 'SHOT_PUT' THEN r.result_value END) AS shotPut,
            MAX(CASE WHEN r.discipline = 'DISCUS' THEN r.result_value END) AS discus,
            MAX(CASE WHEN r.discipline = 'HAMMER' THEN r.result_value END) AS hammer,
            MAX(CASE WHEN r.discipline = 'JAVELIN' THEN r.result_value END) AS javelin,
            MAX(CASE WHEN r.discipline = 'WEIGHT_THROW' THEN r.result_value END) AS weightThrow
        FROM athletes a
        LEFT JOIN results r
            ON r.athlete_id = a.id
           AND EXTRACT(YEAR FROM r.result_date) = :year
        GROUP BY a.id, a.name
        ORDER BY a.name
        """, nativeQuery = true)
        List<OverviewProjection> getSbOverview(@Param("year") int year);
}
