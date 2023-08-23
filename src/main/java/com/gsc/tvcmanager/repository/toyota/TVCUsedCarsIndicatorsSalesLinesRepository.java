package com.gsc.tvcmanager.repository.toyota;

import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsIndicatorsSalesLines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TVCUsedCarsIndicatorsSalesLinesRepository extends JpaRepository<TVCUsedCarsIndicatorsSalesLines, Integer> {


    @Query(" SELECT SL FROM TVCUsedCarsIndicatorsSalesLines SL WHERE SL.idUsedCarsIndicatorsSales = :idUsedCars ORDER BY SL.id")
    List<TVCUsedCarsIndicatorsSalesLines>  getByUsedCarsIndicatorsSalesId(@Param("idUsedCars") Integer idUsedCars);
}
