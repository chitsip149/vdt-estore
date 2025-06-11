package com.trang.estore.auth;


import com.trang.estore.users.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;

import javax.crypto.SecretKey;
import java.util.Date;

@Data
public class Jwt {
    private final Claims claims;
    private final SecretKey secretKey;

    public Jwt(Claims claims, SecretKey secret) {
        this.claims = claims;
        this.secretKey = secret;
    }

    public boolean isExpired() {
        return !claims.getExpiration().after(new Date());
    }

    public Long getUserId(){
        return Long.parseLong(claims.getSubject());
    }

    public Role getRole(){
        return Role.valueOf(claims.get("role", String.class));
    }

    public String toString(){
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }
}
