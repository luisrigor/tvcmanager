package com.gsc.tvcmanager.repository.toyota;

import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsIndicatorsSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IndicatorRepository extends JpaRepository<TVCUsedCarsIndicatorsSales, Integer>, TVCUsedCarsIndicatorsSalesCustom {

    @Query(value = "SELECT * FROM PVM_MONTHLYREPORT WHERE 1=1 AND YEAR = :year AND MONTH = :month AND OID_DEALER = :oidDealer ORDER BY id DESC ", nativeQuery = true)
    Optional<TVCUsedCarsIndicatorsSales> getUsedCarsIndicatorsSales(@Param("year") int year, @Param("month") int month, @Param("oidDealer") String oidDealer);

}
