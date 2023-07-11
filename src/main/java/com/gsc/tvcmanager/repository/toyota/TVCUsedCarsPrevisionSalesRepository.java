package com.gsc.tvcmanager.repository.toyota;

import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsPrevisionSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TVCUsedCarsPrevisionSalesRepository extends JpaRepository<TVCUsedCarsPrevisionSales, Integer> {

    @Query("SELECT TVCU FROM TVCUsedCarsPrevisionSales TVCU WHERE TVCU.oidDealer = :oidDealer AND TVCU.year = :year " +
            "AND TVCU.month<= :month AND TVCU.previsionType = :previsionType ORDER BY month")
    List<TVCUsedCarsPrevisionSales> getMonthPrevisionByDealerHt(@Param("oidDealer") String oidDealer, @Param("year") Integer year,
                                                                @Param("month") Integer month, @Param("previsionType") String previsionType);
}
