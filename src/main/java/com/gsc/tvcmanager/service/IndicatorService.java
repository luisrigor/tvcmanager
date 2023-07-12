package com.gsc.tvcmanager.service;

import com.gsc.tvcmanager.security.UserPrincipal;

public interface IndicatorService {

    void getIndicatorsUsedSalesInfo(UserPrincipal userPrincipal, String oidDealer);
}
