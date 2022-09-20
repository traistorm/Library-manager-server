package com.example.librarymanage.SpringSecurity;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
    private final String JWT_SECRET = "traistorm";

    //Thời gian có hiệu lực của chuỗi jwt
    private final long JWT_EXPIRATION = 60 * 60 * 3 * 1000;

    // Tạo ra jwt từ thông tin user
    public String generateToken(CustomUserDetails userDetails) {
        System.out.println("generate");
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
//        System.out.println(now.getTime());
//        System.out.println(expiryDate.getTime());
        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(Long.toString(userDetails.getUser().getUserid()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    // Lấy thông tin user từ jwt
    public Long getUserIdFromJWT(String token) {
        System.out.println("get id");
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("validate");
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken).getBody();;
            return true;
        } catch (MalformedJwtException ex) {
            System.out.println("Check");
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {

            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {

            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {

            log.error("JWT claims string is empty.");
        }
        catch (SignatureException ex) {

            log.error("JWT signature does not match locally computed signature");
        }
        return false;
    }
}
