package com.cia103g5.common.jwt;

import com.cia103g5.user.Identifiable;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**#############################
 #                             #
 #         Jwt 工具包           #
 #                             #
 ##############################*/

@Component
public class JwtUtil {

    private static final String ISS = "Hogwarts";
    private static final String SECRET = "AlohomoraIsASpellUsedToOpenDoors";
    private static final int EXPIRE_TIME = 600;

    // 生成 Token
    public String generateToken(Identifiable identifiable) {
        Claims claims = Jwts.claims().setSubject("");
        claims.put("roles", identifiable.getIdentity());
        claims.put("userId", identifiable.getId());

        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.MINUTE, EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(ISS)
                .setExpiration(exp.getTime())
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    // 驗證 Token 是否有效(暫時用不到 都經過過濾器驗證)
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
//                    .build()
//                    .parseClaimsJws(token);
//            return true; // Token 驗證通過
//        } catch (JwtException | IllegalArgumentException e) {
//            return false; // Token 無效
//        }
//    }

    // 獲取 Token 中的 Claims
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
