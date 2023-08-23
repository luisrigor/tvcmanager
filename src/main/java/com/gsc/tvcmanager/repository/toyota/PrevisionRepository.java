package com.gsc.tvcmanager.repository.toyota;


import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsPrevisionSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PrevisionRepository extends JpaRepository<TVCUsedCarsPrevisionSales, Integer>, PrevisionRepositoryCustom {

    @Query(value = "SELECT * FROM TVC_USED_CARS_PREVISION_SALES WHERE YEAR = :year AND MONTH = :month AND PREVISION_TYPE= :previsionType", nativeQuery = true)
    List<TVCUsedCarsPrevisionSales> previsionMonthly(@Param("year") Integer year,@Param("month") Integer month, @Param("previsionType") String previsionType);
    @Query(value = " SELECT * FROM TVC_USED_CARS_PREVISION_SALES WHERE YEAR= :year AND MONTH= :month AND PREVISION_TYPE= :previsionType AND STATUS='Fechado'",nativeQuery = true)
    List<TVCUsedCarsPrevisionSales> previsionExcell(@Param("year") Integer year,@Param("month") Integer month, @Param("previsionType") String previsionType);
}
