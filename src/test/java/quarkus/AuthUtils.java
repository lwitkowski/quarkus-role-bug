package quarkus;

import io.restassured.specification.RequestSpecification;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Set;

import static io.restassured.RestAssured.given;

/**
 * Utilities for generating a JWT for testing.
 */
public class AuthUtils {

    private static final PrivateKey pk = readPrivateKey("/privateKey.pem");

    private AuthUtils() {
        // no-op: utility class
    }

    public static RequestSpecification givenAdmin() {
        return givenUser("test-principal-admin", false, "admin");
    }

    public static RequestSpecification givenNonAdmin() {
        return givenUser("test-principal-user", false, "user");
    }

    private static RequestSpecification givenUser(String username, boolean isServiceAccount, String... groups) {
        String accessToken = generateToken(username, isServiceAccount, groups);
        return given()
            .auth()
            .oauth2(accessToken);
    }

    public static RequestSpecification givenAnonymous() {
        return given();
    }

    private static String generateToken(String username, boolean isServiceAccount, String... groups) {
        JwtClaimsBuilder builder = Jwt.claims()
            .subject(username)
            .claim("name", username)
            .claim("azp", username)
            .claim("groups", Set.of(groups));

        if (isServiceAccount) {
            builder = builder.claim("clientId", username);
        }
        return builder
            .expiresAt(System.currentTimeMillis() + 3600)
            .sign(pk);
    }

    private static PrivateKey readPrivateKey(final String pemResName) {
        try (InputStream contentIS = AuthUtils.class.getResourceAsStream(pemResName)) {
            byte[] tmp = new byte[4096];
            int length = contentIS.read(tmp);
            return decodePrivateKey(new String(tmp, 0, length, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static PrivateKey decodePrivateKey(final String pemEncoded) throws Exception {
        byte[] encodedBytes = toEncodedBytes(pemEncoded);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    private static byte[] toEncodedBytes(final String pemEncoded) {
        final String normalizedPem = removeBeginEnd(pemEncoded);
        return Base64.getDecoder().decode(normalizedPem);
    }

    private static String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        return pem.trim();
    }

}
