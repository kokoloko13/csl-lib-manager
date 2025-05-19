package pl.dgutowski.csl_lib_manager.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import pl.dgutowski.csl_lib_manager.user.UserRole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class JwtServiceTest {

    @Value("${security.jwt.secret}")
    private String TEST_SECRET_KEY;

    @Value("${security.jwt.expiration}")
    private Long TEST_JWT_EXPIRATION;
    JwtService jwtService = new JwtService(TEST_SECRET_KEY, 1000*60);

    private String TEST_EMAIL = "test@test.pl";
    private UserRole TEST_USER_ROLE = UserRole.USER;
    private String TEST_TOKEN;

    @BeforeEach
    void setup() {
        TEST_TOKEN = jwtService.generateToken(TEST_EMAIL, TEST_USER_ROLE);
    }

    @Test
    void generateToken() {
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(TEST_SECRET_KEY)))
                .build()
                .parseSignedClaims(TEST_TOKEN)
                .getPayload();

        assertEquals(TEST_EMAIL, claims.getSubject());
        assertEquals(TEST_USER_ROLE.getRoleName(), claims.get("role"));
        assertEquals("CSL Library Manager", claims.getIssuer());
        assertNotNull(claims.getExpiration());
        assertNotNull(claims.getIssuedAt());

    }

    @Test
    void extractEmail() {
        assertEquals(
                jwtService.extractEmail(TEST_TOKEN),
                TEST_EMAIL
        );
    }

    @Test
    void extractExpiration() {
        assertNotNull(jwtService.extractExpiration(TEST_TOKEN));
    }

    @Test
    void extractClaim() {
        assertEquals(
                jwtService.extractClaim(TEST_TOKEN, claims -> claims.get("role")),
                TEST_USER_ROLE.getRoleName()
        );
    }

    @Test
    void validateToken_valid() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(TEST_EMAIL);

        assertTrue(jwtService.validateToken(TEST_TOKEN, userDetails));
    }

    @Test
    void validateToken_invalid() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("otheremail@email.pl");

        assertFalse(jwtService.validateToken(TEST_TOKEN, userDetails));
    }

}
