package api.saylix.uz.utils;

import api.saylix.uz.dto.JwtResponseDTO;
import api.saylix.uz.enums.UsersRoles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    private static final int tokenLiveTime = 1000 * 3600 * 24; // 1-day
    private static final String secretKey = "fGq1vJxJ8mvV2kjJh78v5K2n6sY9xQzP3gKmRpF3zAs=";

    // Token yaratish (faqat bitta rol bilan)
    public static String encode(String username, String id, UsersRoles role) {
        return Jwts
                .builder()
                .subject(username)
                .claim("id", String.valueOf(id))
                .claim("role", role.name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenLiveTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Tokenni tahlil qilish
    public static JwtResponseDTO decode(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String username = claims.getSubject();
        String id = (String) claims.get("id");
        String roleStr = (String) claims.get("role");

        UsersRoles role = UsersRoles.valueOf(roleStr);

        return new JwtResponseDTO(id, username, role);
    }

    private static SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
