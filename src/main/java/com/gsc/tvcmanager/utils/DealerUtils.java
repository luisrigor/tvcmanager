package com.gsc.tvcmanager.utils;

import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DealerUtils {

    public List<Dealer> getActiveMainDealersForServices(String oidNet) throws SCErrorException {
        return  Dealer.getHelper().getActiveMainDealersForServices(oidNet, Dealer.OID_NET_TOYOTA.equals(oidNet) ? Dealer.OID_TOYOTA_SERVICE_USEDCARS : Dealer.OID_LEXUS_SERVICE_USEDCARS);
    }

    public Dealer getByObjectId(String oidNet, String oidDealer) throws SCErrorException {
        if (Dealer.OID_NET_LEXUS.equals(oidNet))
            return Dealer.getLexusHelper().getByObjectId(oidDealer);

        return Dealer.getToyotaHelper().getByObjectId(oidDealer);
    }
}
