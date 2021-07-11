package com.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtilities {

    private String key = "secret";

    public String extractUsername(String token){
       return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpirationTime(String token){
        return extractClaims(token,Claims::getExpiration);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsKVP) {
        Claims claims = extractAllClaims(token);
        return claimsKVP.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody(); //TODO:JWS
    }

    public String generateToken(UserDetails userDetails){
        return createToken(userDetails.getUsername());
    }

    private String createToken(String subject){
        Map<String,Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+20000))
                .signWith(SignatureAlgorithm.HS512,key)
                .compact();
    }




}
