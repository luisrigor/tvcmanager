package com.gsc.tvcmanager.repository.toyota.impl;


import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsPrevisionSales;
import com.gsc.tvcmanager.repository.toyota.PrevisionRepositoryCustom;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;

public class PrevisionRepositoryCustomImpl implements PrevisionRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void mergeUsedCarsPrevisionSales(TVCUsedCarsPrevisionSales usedCarsPrevisionSales, String createdBy) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        em.createNativeQuery("MERGE INTO TVC_USED_CARS_PREVISION_SALES AS PUCS"
                        + "USING (VALUES ("+usedCarsPrevisionSales.getId()+")) AS AUX (ID) "
                        + "ON PUCS.ID = AUX.ID"
                        + "WHEN MATCHED THEN"
                        + "UPDATE SET STATUS=?, PREVISION_TVC=?, PREVISION_SN=?, CHANGED_BY=?, DT_CHANGED=? "
                        + "WHEN NOT MATCHED THEN"
                        + "INSERT (OID_DEALER,YEAR,MONTH,PREVISION_TYPE,STATUS,PREVISION_TVC,PREVISION_SN,CREATED_BY,DT_CREATED)"
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? )")
                       .setParameter(1, usedCarsPrevisionSales.getStatus())
                        .setParameter(2, usedCarsPrevisionSales.getPrevisionTvc())
                        .setParameter(3, usedCarsPrevisionSales.getPrevisionSn())
                        .setParameter(4, createdBy)
                        .setParameter(5, ts)
                        .setParameter(6, usedCarsPrevisionSales.getOidDealer())
                        .setParameter(7, usedCarsPrevisionSales.getYear())
                        .setParameter(8, usedCarsPrevisionSales.getMonth())
                        .setParameter(9, usedCarsPrevisionSales.getPrevisionType())
                        .setParameter(10, usedCarsPrevisionSales.getStatus())
                        .setParameter(11, usedCarsPrevisionSales.getPrevisionTvc())
                        .setParameter(12, usedCarsPrevisionSales.getPrevisionSn())
                        .setParameter(13, createdBy)
                        .executeUpdate();
    }
}
