package com.api.e2e.http;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@SuppressWarnings("deprecation")
public class OauthToken {

    public static String createToken() {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        String jws = Jwts.builder().setIssuer("adcash").setSubject("api").claim("name", "apiUser").claim("scope", "CRUD").setIssuedAt(now).setExpiration(new Date(nowMillis + 300000))
                .signWith(SignatureAlgorithm.HS256, DatatypeConverter.parseBase64Binary("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")).compact();
        return jws;
    }

    public static boolean Verify(String jwt) {
        Claims claims = null;
        boolean isVerified = true;
        try {
            claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=")).parseClaimsJws(jwt).getBody();
        } catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            isVerified = false;
        }
        return isVerified;
    }

}
