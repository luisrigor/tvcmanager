package com.gsc.tvcmanager.security;


import com.gsc.scgscwsauthentication.response.AuthenticationExtraResponse;
import com.gsc.scgscwsauthentication.response.ExtranetUser;
import com.gsc.scgscwsauthentication.response.PairIdName;
import com.gsc.tvcmanager.config.environment.EnvironmentConfig;
import com.gsc.tvcmanager.constants.AppProfile;
import com.gsc.tvcmanager.model.toyota.entity.Client;
import com.gsc.tvcmanager.repository.toyota.ClientRepository;
import com.gsc.tvcmanager.repository.toyota.ConfigurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class JwtAuthenticationManager implements AuthenticationManager {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationManager.class);

    // For now we are using a hardcoded client
    public static final Long CLIENT_ID = 1L;

    private final ConfigurationRepository configurationRepository;
    private final EnvironmentConfig environmentConfig;
    private final ClientRepository clientRepository;

    public JwtAuthenticationManager(ConfigurationRepository configurationRepository, EnvironmentConfig environmentConfig, ClientRepository clientRepository) {
        this.configurationRepository = configurationRepository;
        this.environmentConfig = environmentConfig;
        this.clientRepository = clientRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!configurationRepository.isLoginEnabled()) {
            throw new AuthenticationServiceException("Login is disabled");
        }

        String loginToken = authentication.getPrincipal() != null ? authentication.getPrincipal().toString() : null;
        String tokenParts[] = loginToken.split("\\|\\|\\|");
        loginToken = tokenParts[1];

        if (!StringUtils.hasText(loginToken)) {
            throw new BadCredentialsException("Invalid login token");
        }

        String[] parts = loginToken.split("\\|\\|");
        if (parts.length != 2) {
            throw new BadCredentialsException("Bad credentials");
        }

        Optional<Client> client = clientRepository.findById(CLIENT_ID);

        if (!client.isPresent()) {
            throw new BadCredentialsException("Invalid client");
        }

        AuthenticationExtraResponse authenticationExtra = environmentConfig.getAuthenticationInvoker().authenticationExtra(parts[0], parts[1],  Integer.parseInt(tokenParts[0]));

        if (!authenticationExtra.getCode().equals("0") || authenticationExtra.getUser() == null) {
            throw new BadCredentialsException("Bad credentials");
        }

        ExtranetUser user = authenticationExtra.getUser();

        final String userId = user.getIdUser() + "||" + user.getLogin() + "||" +user.getMail();
        final Set<AppProfile> roles = getRoles(user);

        if (roles.isEmpty()) {
            throw new AuthenticationServiceException("No permissions");
        }

        UserPrincipal authUser = new UserPrincipal(userId, roles, Long.parseLong(tokenParts[0]));
        authUser.setOidDealerParent(user.getDealerParent().getObjectId());
        authUser.setOidDealer(user.getDealer().getObjectId());
        return JwtAuthenticationToken.authenticated(authUser, Collections.emptyList());
    }

    private Set<AppProfile> getRoles(ExtranetUser user) {
        Set<AppProfile> roles = new LinkedHashSet<>(AppProfile.values().length);
        Set<Integer> profiles = getProfiles(user);

        for (Integer profileId : profiles) {
            if (AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_TCAP.getIdToyota().equals(profileId) || AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_TCAP.getIdLexus().equals(profileId)) {
                roles.add(AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_TCAP);
                roles.add(AppProfile.TVC_MANAGER_ROLE_EDIT_REGISTER);
                roles.add(AppProfile.TVC_MANAGER_ROLE_DEALER_FILTER);
                roles.add(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS);
                roles.add(AppProfile.TVC_MANAGER_ROLE_OPEN_BUTTON);
            } else if (AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_CONC.getIdToyota().equals(profileId) || AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_CONC.getIdLexus().equals(profileId)) {
                roles.add(AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_CONC);
                roles.add(AppProfile.TVC_MANAGER_ROLE_NEW_RECORD);
                roles.add(AppProfile.TVC_MANAGER_ROLE_SAVE_BUTTONS);
            } else if (AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_INST.getIdToyota().equals(profileId) || AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_INST.getIdLexus().equals(profileId)) {
                roles.add(AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_INST);
                roles.add(AppProfile.TVC_MANAGER_ROLE_NEW_RECORD);
                roles.add(AppProfile.TVC_MANAGER_ROLE_SAVE_BUTTONS);
            }
        }
        return roles;
    }

    private Set<Integer> getProfiles(ExtranetUser user) {
        if (user.getProfiles() == null) {
            return Collections.emptySet();
        }
        Set<Integer> profiles = new LinkedHashSet<>(user.getProfiles().size());
        for (PairIdName profile : user.getProfiles()) {
            profiles.add(profile.getId());
        }
        return profiles;
    }

}
