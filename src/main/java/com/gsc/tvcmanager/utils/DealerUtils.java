package com.gsc.tvcmanager.utils;

import com.rg.dealer.Dealer;
import com.rg.objects.DealerCode;
import com.sc.commons.exceptions.SCErrorException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public Dealer getByObjectIdOI(String oidNet, String oidDealer) throws SCErrorException {
        return Dealer.getHelper().getByObjectId(oidNet, oidDealer);
    }

    public List<DealerCode> getDealerCodes(String oidNet, Dealer oDealer) throws SCErrorException {
        return oDealer.getCodes(Dealer.OID_NET_TOYOTA.equals(oidNet) ? Dealer.OID_TOYOTA_CODE_DEKRA : Dealer.OID_LEXUS_CODE_DEKRA);
    }
}
