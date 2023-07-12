package com.gsc.tvcmanager.repository.toyota;

import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsPrevisionSales;

import java.util.List;

public interface TVCUsedCarsPrevisionSalesRepositoryCustom {

    List<TVCUsedCarsPrevisionSales> getAllPrevisionHtD(Integer year, Integer month, String previsionType, String reportType);
}
