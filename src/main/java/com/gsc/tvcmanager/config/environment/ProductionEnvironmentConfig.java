package com.gsc.tvcmanager.config.environment;

import com.gsc.a2p.invoke.A2pApiInvoke;
import com.gsc.scgscwsauthentication.invoke.SCAuthenticationInvoke;
import com.gsc.scwscardb.core.invoke.CarInvoker;
import com.gsc.scwscardb.util.DATA;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Profile("production")
@Component
public class ProductionEnvironmentConfig implements EnvironmentConfig {

   @Override
   public SCAuthenticationInvoke getAuthenticationInvoker() {
      return new SCAuthenticationInvoke(com.gsc.scgscwsauthentication.invoke.DATA.URL_PRODUCTION);
   }

   @Override
   public CarInvoker getCarInvoker() {
      return new CarInvoker(DATA.WSCARDB_URL_PRODUCTION_HTTPS, DATA.HASH_TOKEN_BACKOFFICE_TOYOTA_PRODUCTION, false);
   }

   @Override
   public A2pApiInvoke getA2pApiInvoker() {
      return new A2pApiInvoke(com.gsc.a2p.util.DATA.A2P_SERVER_PRODUCTION);
   }
}
