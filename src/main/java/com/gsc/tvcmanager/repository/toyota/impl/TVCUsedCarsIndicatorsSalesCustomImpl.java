package com.gsc.tvcmanager.repository.toyota.impl;

import com.gsc.tvcmanager.exceptions.IndicatorsException;
import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsIndicatorsSales;
import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsIndicatorsSalesLines;
import com.gsc.tvcmanager.repository.toyota.TVCUsedCarsIndicatorsSalesCustom;
import com.sc.commons.utils.DataBaseTasks;
import lombok.extern.log4j.Log4j;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;

@Log4j
public class TVCUsedCarsIndicatorsSalesCustomImpl implements TVCUsedCarsIndicatorsSalesCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void mergeUsedCarsIndicatorsSales(TVCUsedCarsIndicatorsSales oUsedCarsIndicatorsSales,
                                             List<TVCUsedCarsIndicatorsSalesLines> vecUsedCarsIndicatorsSalesLines, String createdBy) {
        StringBuilder SQL = new StringBuilder("");

        SQL.append("MERGE INTO TVC_USED_CARS_INDICATORS_SALES AS IUS ");
        SQL.append("USING (VALUES ("+oUsedCarsIndicatorsSales.getId()+")) AS AUX (ID) ");
        SQL.append("ON IUS.ID = AUX.ID ");
        SQL.append("WHEN MATCHED THEN ");
        SQL.append("UPDATE SET OID_DEALER=?1, YEAR=?2, MONTH=?3, TOT_USED_CARS=?4, STATUS=?5, CHANGED_BY=?6, DT_CHANGED=?7 ");
        SQL.append("WHEN NOT MATCHED THEN ");
        SQL.append("INSERT (OID_DEALER,YEAR,MONTH,TOT_USED_CARS,STATUS,CREATED_BY,DT_CREATED) ");
        SQL.append("     VALUES (?8, ?9, ?10, ?11, ?12, ?13, ?14 )");

        Query query = em.createNativeQuery(SQL.toString());
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            log.debug("SQL:" + SQL.toString() + " " + oUsedCarsIndicatorsSales.toString() + " createdBy:" + createdBy);
            query.setParameter(1, oUsedCarsIndicatorsSales.getOidDealer())
                    .setParameter(2, oUsedCarsIndicatorsSales.getYear())
                    .setParameter(3, oUsedCarsIndicatorsSales.getMonth())
                    .setParameter(4, oUsedCarsIndicatorsSales.getTotUsedCars())
                    .setParameter(5, oUsedCarsIndicatorsSales.getStatus())
                    .setParameter(6, createdBy)
                    .setParameter(7, ts)
                    .setParameter(8, oUsedCarsIndicatorsSales.getOidDealer())
                    .setParameter(9, oUsedCarsIndicatorsSales.getYear())
                    .setParameter(10, oUsedCarsIndicatorsSales.getMonth())
                    .setParameter(11, oUsedCarsIndicatorsSales.getTotUsedCars())
                    .setParameter(12, oUsedCarsIndicatorsSales.getStatus())
                    .setParameter(13, createdBy)
                    .setParameter(14, ts)
                    .executeUpdate();
        }catch (Exception e) {
            throw new IndicatorsException("Error saving UsedCarsIndicatorsSales info", e);
        }

        try {
            for (TVCUsedCarsIndicatorsSalesLines oUsedCarsIndicatorsSalesLines: vecUsedCarsIndicatorsSalesLines) {
                oUsedCarsIndicatorsSalesLines.setIdUsedCarsIndicatorsSales(oUsedCarsIndicatorsSales.getId());
                SQL = new StringBuilder("");
                SQL.append("MERGE INTO TVC_USED_CARS_INDICATORS_SALES_LINES AS IUSL ");
                SQL.append("USING (VALUES (" + oUsedCarsIndicatorsSales.getId() + ",'" + oUsedCarsIndicatorsSalesLines.getUsedCarsIndicators() + "')) AS AUX (ID_USED_CARS_INDICATORS_SALES,USED_CARS_INDICATORS) ");
                SQL.append("ON IUSL.ID_USED_CARS_INDICATORS_SALES = AUX.ID_USED_CARS_INDICATORS_SALES AND IUSL.USED_CARS_INDICATORS = AUX.USED_CARS_INDICATORS ");
                SQL.append("WHEN MATCHED THEN ");
                SQL.append("UPDATE SET ID_USED_CARS_INDICATORS_SALES=?1, USED_CARS_INDICATORS=?2, INDICATOR_VALUE=?3, ");
                SQL.append("CHANGED_BY=?4, DT_CHANGED=?5 ");
                SQL.append("WHEN NOT MATCHED THEN ");
                SQL.append("    INSERT (ID_USED_CARS_INDICATORS_SALES, USED_CARS_INDICATORS,INDICATOR_VALUE,CREATED_BY,DT_CREATED) ");
                SQL.append("     VALUES (?6, ?7, ?8, ?9, ?10)");

                query = em.createNativeQuery(SQL.toString());

                query.setParameter(1, oUsedCarsIndicatorsSalesLines.getIdUsedCarsIndicatorsSales())
                        .setParameter(2, oUsedCarsIndicatorsSalesLines.getUsedCarsIndicators())
                        .setParameter(3, oUsedCarsIndicatorsSalesLines.getIndicatorValue() == -1 ? Types.VARCHAR:oUsedCarsIndicatorsSalesLines.getIndicatorValue())
                        .setParameter(4, createdBy)
                        .setParameter(5, ts)
                        .setParameter(6, oUsedCarsIndicatorsSalesLines.getIdUsedCarsIndicatorsSales())
                        .setParameter(7, oUsedCarsIndicatorsSalesLines.getUsedCarsIndicators())
                        .setParameter(8, oUsedCarsIndicatorsSalesLines.getIndicatorValue() == -1 ? Types.VARCHAR:oUsedCarsIndicatorsSalesLines.getIndicatorValue())
                        .setParameter(9, createdBy)
                        .setParameter(10, ts)
                        .executeUpdate();

                log.debug("SQL:" + SQL.toString() + " " + oUsedCarsIndicatorsSalesLines.toString() + " createdBy:" + createdBy);
            }
        } catch (Exception e) {
            throw new IndicatorsException("Error saving UsedCarsIndicatorsSalesLines info", e);
        }
    }


}
