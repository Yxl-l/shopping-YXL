package com.yxl.shopping.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private static String signKey = "SVRIRUlaiuvgsiguaiug43245345345345gnWithSignatureAlgor34534534534534534534534fissguisegNQQ==";//密钥
    private static Long expire = 43200000L;//令牌过期时间

    /**
     * 生成JWT令牌
     */
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
    }

    /**
     * 解析JWT令牌
     *
     * @param jwt JWT令牌
     * @return JWT第二部分负载 payload 中存储的内容
     */
    public static Map<String, Object> parseJWT(String jwt) {
        Claims body = null;
        try {
            body = Jwts.parser()
                    .setSigningKey("SVRIRUlaiuvgsiguaiug43245345345345gnWithSignatureAlgor34534534534534534534534fissguisegNQQ==")
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception ignored) {
        }
        return body;
    }

}
