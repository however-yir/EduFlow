package group.teachingmanagerbk.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * jwt令牌工具
 */
public class JwtUtil {

    final static String secret = "teaching-manager-bk-ui-system";

    /* 生成JWT令牌 */
    public static String genJWT(Map<String,Object> claims) {
        Date deadline = new Date(System.currentTimeMillis() + 3*3600*1000);
        return Jwts.builder()
                    .signWith(SignatureAlgorithm.HS512,secret)
                    .setClaims(claims)
                    .setExpiration(deadline)
                    .compact();
    }

    /* 解析JWT令牌 */
    public static Map<String, Object> parseJWT(String jwt) throws RuntimeException {
        return  Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
    }

}
