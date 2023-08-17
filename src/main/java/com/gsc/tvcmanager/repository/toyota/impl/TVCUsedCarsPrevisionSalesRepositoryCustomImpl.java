package com.gsc.tvcmanager.repository.toyota.impl;

import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsPrevisionSales;
import com.gsc.tvcmanager.repository.toyota.TVCUsedCarsPrevisionSalesRepositoryCustom;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class TVCUsedCarsPrevisionSalesRepositoryCustomImpl implements TVCUsedCarsPrevisionSalesRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<TVCUsedCarsPrevisionSales> getAllPrevisionHtD(Integer year, Integer month, String reportType, String previsionType) {
        String sql = "SELECT * FROM TVC_USED_CARS_PREVISION_SALES WHERE YEAR= :year AND MONTH= :month AND PREVISION_TYPE= :previsionType ";
        if("EXCELL".equals(reportType)){
            sql = sql + " AND STATUS='Fechado'";
        }

        Query query = em.createNativeQuery(sql, TVCUsedCarsPrevisionSales.class);
        List<TVCUsedCarsPrevisionSales> previsionSalesList =
                query.setParameter("year", year)
                .setParameter("month", month)
                .setParameter("previsionType", previsionType)
                .getResultList();
        return previsionSalesList;
    }
}
