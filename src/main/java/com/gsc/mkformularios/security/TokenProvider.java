package com.gsc.mkformularios.security;

import com.gsc.mkformularios.config.datasource.toyota.DbClient;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.constants.AppProfile;
import com.gsc.mkformularios.exceptions.AuthTokenException;
import com.gsc.mkformularios.model.toyota.entity.LoginKey;
import com.gsc.mkformularios.model.toyota.entity.ServiceLogin;
import com.gsc.mkformularios.repository.toyota.ConfigurationRepository;
import com.gsc.mkformularios.repository.toyota.LoginKeyRepository;
import com.gsc.mkformularios.repository.toyota.ServiceLoginRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.gsc.mkformularios.constants.DATAConstants.APP_LEXUS;
import static com.gsc.mkformularios.constants.DATAConstants.APP_TOYOTA;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String CREATED_BY = "Tcap-TokenProvider";

    private static final String ISSUER = "Tcap";
    private static final String AUDIENCE = "Tcap Clients";
    private static final String ROLES = "roles";
    private static final String JWT_ENVIRONMENT = "environment";
    private static final String JWT_CLIENT_ID = "client";
    private static final String OID_DEALER_PARENT = "dealer_parent";
    private static final String OID_NET = "oid_net";

    private final ConfigurationRepository configurationRepository;
    private final ServiceLoginRepository serviceLoginRepository;
    private final LoginKeyRepository loginKeyRepository;
    private final String activeProfile;
    private final DbContext dbContext;

    public TokenProvider(ConfigurationRepository configurationRepository, LoginKeyRepository loginKeyRepository,
                         ServiceLoginRepository serviceLoginRepository, @Value("${spring.profiles.active}") String activeProfile,
                         DbContext dbContext) {
        this.configurationRepository = configurationRepository;
        this.loginKeyRepository = loginKeyRepository;
        this.serviceLoginRepository = serviceLoginRepository;
        this.activeProfile = activeProfile;
        this.dbContext = dbContext;
    }

    public void setDataSourceContext(Long client){
        if (client == APP_LEXUS) {
            dbContext.setBranchContext(DbClient.DB_LEXUS);
        } else if (client == APP_TOYOTA) {
            dbContext.setBranchContext(DbClient.DB_TOYOTA);
        }
    }

    public String createToken(Authentication authentication, String appId) throws AuthenticationException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if(appId.equals(String.valueOf(APP_TOYOTA))) {
            userPrincipal.setOidNet("SC00010001");
        } else if (appId.equals(String.valueOf(APP_LEXUS))) {
            userPrincipal.setOidNet("SC00010002");
        }

        Optional<LoginKey> loginKey = getKey();

        if (!loginKey.isPresent()) {
            throw new AuthenticationServiceException("Unable to find key for token generation");
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + configurationRepository.getTokenExpirationMsec());

        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(userPrincipal.getUsername())
                .setAudience(AUDIENCE)
                .setExpiration(expiryDate)
                .setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString())
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(loginKey.get().getKeyValue())))
                .setHeaderParam("kid", loginKey.get().getId())
                .claim(JWT_ENVIRONMENT, activeProfile)
                .claim(JWT_CLIENT_ID, Integer.parseInt(appId))
                .claim(ROLES, userPrincipal.getRoles())
                .claim(OID_DEALER_PARENT, userPrincipal.getOidDealerParent())
                .claim(OID_NET, userPrincipal.getOidNet())
                .compact();
    }

    private Optional<LoginKey> getKey() {
        Optional<LoginKey> key = loginKeyRepository.findFirstByEnabledIsTrue();
        if (key.isPresent()) {
            return key;
        }
        return generateNewKey();
    }

    private Optional<LoginKey> generateNewKey() {
        if (!configurationRepository.isKeyCreationEnabled()) {
            return Optional.empty();
        }

        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        LoginKey loginKey = new LoginKey();
        loginKey.setKeyValue(Encoders.BASE64.encode(key.getEncoded()));
        loginKey.setEnabled(Boolean.TRUE);
        loginKey.setCreatedBy(CREATED_BY);
        loginKey.setCreated(LocalDateTime.now());

        return Optional.of(loginKeyRepository.saveAndFlush(loginKey));
    }

    public JwtAuthenticationToken validateToken(String authToken) throws AuthenticationException {
        this.setDataSourceContext(Long.valueOf(APP_TOYOTA));
        if (authToken.contains(":")) {
            return validateServiceToken(authToken);
        }
        return validateUserToken(authToken);

    }

    private JwtAuthenticationToken validateUserToken(String authToken) throws AuthenticationException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKeyResolver(new DBSigningKeyResolver())
                    .requireIssuer(ISSUER)
                    .requireAudience(AUDIENCE)
                    .require(JWT_ENVIRONMENT, activeProfile)
                    .build()
                    .parseClaimsJws(authToken)
                    .getBody();

            @SuppressWarnings("unchecked")
            Set<AppProfile> roles = (Set<AppProfile>) claims.get(ROLES, List.class).stream()
                    .map(role -> AppProfile.valueOf(role.toString()))
                    .collect(Collectors.toSet());

            return JwtAuthenticationToken.authenticated(
                    new UserPrincipal(
                            claims.getSubject(),
                            roles,
                            claims.get(JWT_CLIENT_ID, Long.class),
                            claims.get(OID_NET, String.class),
                            claims.get(OID_DEALER_PARENT, String.class)
                    ),
                    Collections.emptyList()
            );
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("Invalid access token.");
        } catch (ExpiredJwtException ex) {
            throw new AuthTokenException("The session has expired " + ex.getMessage(), ex);
        }
    }

    private JwtAuthenticationToken validateServiceToken(String authToken) throws AuthenticationException {
        String[] parts = authToken.split(":", -1);
        if (parts.length != 2) {
            throw new BadCredentialsException("Invalid access token.");
        }

        Optional<ServiceLogin> serviceLogin = serviceLoginRepository.findByNameAndValue(parts[0], parts[1]);

        ServiceLogin sl = serviceLogin.orElseThrow(() -> new BadCredentialsException("Invalid access token."));

        return JwtAuthenticationToken.authenticated(
                new UserPrincipal(
                        parts[0],
                        getRoles(sl),
                        JwtAuthenticationManager.CLIENT_ID
                ),
                Collections.emptyList()
        );
    }

    private Set<AppProfile> getRoles(ServiceLogin sl) {
        Set<AppProfile> profiles = new HashSet<>();

//        Set<AppProfile> roles = (Set<AppProfile>) claims.get(ROLES, List.class).stream()
//                .map(role -> AppProfile.valueOf(role.toString()))
//                .collect(Collectors.toSet());
//
//        if (Objects.equals(sl.getUploadFile(), Boolean.TRUE)) {
//            profiles.add(AppProfile.UPLOAD_FILE);
//        }
//
//        if (Objects.equals(sl.getCleanupProjects(), Boolean.TRUE)) {
//            profiles.add(AppProfile.CLEANUP_PROJECTS);
//        }
//
//        if (Objects.equals(sl.getDownloadProjectFiles(), Boolean.TRUE)) {
//            profiles.add(AppProfile.DOWNLOAD_PROJECT_FILES);
//        }

        return profiles;
    }

    private class DBSigningKeyResolver extends SigningKeyResolverAdapter {

        @SuppressWarnings("rawtypes")
        @Override
        public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
            //inspect the header or claims, lookup and return the signing key
            Long keyId = Long.valueOf(jwsHeader.getKeyId()); //or any other field that you need to inspect
            return loginKeyRepository.findById(keyId)
                    .map(key -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(key.getKeyValue())))
                    .orElse(null);
        }
    }

}
