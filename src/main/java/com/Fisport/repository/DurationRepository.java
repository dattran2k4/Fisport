package com.Fisport.repository;

import com.Fisport.model.Duration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface DurationRepository extends JpaRepository<Duration, Long> {

//    @Query(value = """
//            SELECT dur.minutes
//            FROM field_type_duration d
//            JOIN field_type ft ON ft.id = d.field_type_id
//            JOIN field f ON f.field_type_id = ft.id
//            JOIN sub_field sf ON sf.field_id = f.id
//            JOIN duration dur ON dur.id = d.duration_id
//            WHERE sf.id = :id
//            AND NOT EXISTS (
//                SELECT 1 FROM booking b
//                WHERE b.subfield_id = :id
//                  AND b.booking_date = :date
//                  AND b.status IN ('PENDING', 'PAID')
//                  AND :start < b.end_time
//                  AND ADDTIME(:start, CONCAT(dur.minutes, ':00')) > b.start_time
//            )
//            """, nativeQuery = true)
//    List<Integer> findAvailableDurationsForBooking(
//            @Param("id") Long id,
//            @Param("date") LocalDate date,
//            @Param("start") LocalTime start
//    );
}
