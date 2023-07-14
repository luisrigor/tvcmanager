package com.gsc.tvcmanager.repository.toyota;

import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsIndicatorsSales;
import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsIndicatorsSalesLines;

import java.util.List;

public interface TVCUsedCarsIndicatorsSalesCustom {

    void mergeUsedCarsIndicatorsSales(TVCUsedCarsIndicatorsSales oUsedCarsIndicatorsSales, List<TVCUsedCarsIndicatorsSalesLines> vecUsedCarsIndicatorsSalesLines, String createdBy);
}
