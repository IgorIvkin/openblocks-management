package ru.openblocks.management.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.openblocks.management.config.http.CookieConfig;
import ru.openblocks.management.config.jwt.JwtTokenResolver;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * Service used to verify JWT-tokens, parse them and to issue a new tokens
 * for users of application.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    public static final String BEARER = "Bearer ";

    public static final String TOKEN_ISSUER = "openblocks-management";

    private final JwtTokenResolver jwtTokenResolver;

    @Value("${app.jwt.private-key}")
    private String privateKey;

    @Value("${app.jwt.public-key}")
    private String publicKey;

    @Value("${app.cookie.domain}")
    private String cookieDomain;

    private Algorithm algorithm;

    private JWTVerifier verifier;

    @PostConstruct
    public void initialize() throws
            NoSuchAlgorithmException,
            NoSuchProviderException,
            InvalidKeySpecException {

        KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC");

        ECPublicKey ecPublicKey = (ECPublicKey) keyFactory.generatePublic(
                new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey))
        );
        ECPrivateKey ecPrivateKey = (ECPrivateKey) keyFactory.generatePrivate(
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey))
        );
        algorithm = Algorithm.ECDSA384(ecPublicKey, ecPrivateKey);
        verifier = JWT.require(algorithm)
                .withIssuer(TOKEN_ISSUER)
                .build();

        log.info("JWT token service initialization is finished");
    }

    /**
     * Generates token for user with lifetime now + one month.
     *
     * @param userName name of user
     * @return JWT-token
     */
    public String issue(final String userName) {
        final LocalDateTime now = LocalDateTime.now();
        return JWT.create()
                .withIssuer(TOKEN_ISSUER)
                .withSubject(userName)
                .withIssuedAt(Timestamp.valueOf(now))
                .withExpiresAt(Timestamp.valueOf(now.plusDays(CookieConfig.JWT_TOKEN_COOKIE_DAYS_TO_REMEMBER)))
                .sign(algorithm);
    }

    /**
     * Refresh token returning a new token with increased lifetime.
     * Returns the same token in case if it is not expired.
     *
     * @param token JWT-token to refresh
     * @return new JWT-token
     */
    public String refresh(final String token) {
        final DecodedJWT decodedJwt = decode(token);
        try {
            verifier.verify(decodedJwt);
            return token;
        } catch (TokenExpiredException ex) {
            return issue(decodedJwt.getSubject());
        }
    }

    /**
     * Verifies token. Returns false in case if token is wrong, signed by wrong key
     * or in case if it's expired.
     *
     * @param token JWT-token to analyze
     * @return true, if token is valid
     */
    public boolean verify(final String token) {
        if (token != null) {
            try {
                final DecodedJWT jwt = verifier.verify(token);
                return true;
            } catch (TokenExpiredException ex) {
                log.error("JWT-token was expired", ex);
                return false;
            } catch (JWTVerificationException ex) {
                log.error("JWT-token verification error", ex);
                return false;
            }
        }
        return false;
    }

    /**
     * Decodes token parsing it to claims. This method is used to parse token without verification.
     * Useful when token is already expired.
     *
     * @param token JWT-token to decode
     * @return decoded token claims
     */
    public DecodedJWT decode(final String token) {
        if (token != null) {
            return JWT.decode(token);
        }
        return null;
    }

    /**
     * Sets an HTTP-Cookie with token value.
     *
     * @param token    JWT-token
     * @param response HTTP-response
     */
    public void setJwtTokenCookie(final String token, final HttpServletResponse response) {
        Cookie cookie = new Cookie(CookieConfig.JWT_TOKEN_COOKIE_NAME, token);
        cookie.setPath("/");
        cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(CookieConfig.JWT_TOKEN_COOKIE_DAYS_TO_REMEMBER));
        cookie.setDomain(cookieDomain);
        response.addCookie(cookie);
    }

    /**
     * "Expires" HTTP-Cookie for token.
     *
     * @param response HTTP-response
     */
    public void expireJwtTokenCookie(final HttpServletResponse response) {
        Cookie cookie = new Cookie(CookieConfig.JWT_TOKEN_COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        cookie.setDomain(cookieDomain);
        response.addCookie(cookie);
    }

    /**
     * Extracts JWT-token from request.
     *
     * @param request HTTP-request to servlet
     * @return JWT-token
     */
    public String extractJwtToken(final HttpServletRequest request) {
        return jwtTokenResolver.resolve(request);
    }


}
