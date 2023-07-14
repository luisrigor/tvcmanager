package com.gsc.tvcmanager.repository.toyota;

import com.gsc.tvcmanager.dto.PrevisionHtDTO;
import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsPrevisionSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TVCUsedCarsPrevisionSalesRepository extends JpaRepository<TVCUsedCarsPrevisionSales, Integer>, TVCUsedCarsPrevisionSalesRepositoryCustom {

    @Query("SELECT TVCU FROM TVCUsedCarsPrevisionSales TVCU WHERE TVCU.oidDealer = :oidDealer AND TVCU.year = :year " +
            "AND TVCU.month<= :month AND TVCU.previsionType = :previsionType ORDER BY month")
    List<TVCUsedCarsPrevisionSales> getMonthPrevisionByDealerHt(@Param("oidDealer") String oidDealer, @Param("year") Integer year,
                                                                @Param("month") Integer month, @Param("previsionType") String previsionType);


    @Query("SELECT NEW com.gsc.tvcmanager.dto.PrevisionHtDTO(TVCU.oidDealer, SUM(TVCU.previsionTvc), SUM(TVCU.previsionSn)) FROM TVCUsedCarsPrevisionSales TVCU " +
            "WHERE TVCU.year = :year AND TVCU.month<= :month AND TVCU.previsionType = :previsionType AND TVCU.status = 'Fechado' GROUP BY oidDealer ")
    List<PrevisionHtDTO> getAllPrevisionHt( @Param("year") Integer year, @Param("month") Integer month, @Param("previsionType") String previsionType);


    @Modifying
    @Query("UPDATE TVCUsedCarsPrevisionSales TVCU SET TVCU.status = :status WHERE TVCU.id = :id")
    void changeUsedCarsPrevisionSalesStatus(@Param("id") Integer id, @Param("status") String status);

}
