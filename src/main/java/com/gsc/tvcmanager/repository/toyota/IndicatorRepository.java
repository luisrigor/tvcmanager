package com.gsc.tvcmanager.repository.toyota;

import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsIndicatorsSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IndicatorRepository extends JpaRepository<TVCUsedCarsIndicatorsSales, Integer>, TVCUsedCarsIndicatorsSalesCustom {

    @Query(value = "SELECT TVCUC FROM TVCUsedCarsIndicatorsSales TVCUC WHERE TVCUC.year = :year AND TVCUC.month = :month AND TVCUC.oidDealer = :oidDealer ORDER BY id DESC")
    Optional<TVCUsedCarsIndicatorsSales> getUsedCarsIndicatorsSales(@Param("year") int year, @Param("month") int month, @Param("oidDealer") String oidDealer);

}
