package com.gsc.tvcmanager.repository.toyota;


import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsPrevisionSales;

public interface PrevisionRepositoryCustom {

    void mergeUsedCarsPrevisionSales(TVCUsedCarsPrevisionSales usedCarsPrevisionSales, String createdBy);
}
