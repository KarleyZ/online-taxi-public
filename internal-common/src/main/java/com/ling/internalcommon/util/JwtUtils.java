package com.ling.internalcommon.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ling.internalcommon.dto.TokenResult;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    //盐
    private static final String SIGN = "KTVCPU@333!";
    //我们的token的第二部分只包含乘客的手机号
    private static final String JWT_KEY_PHONE="phone";
    //乘客是1，司机是2
    private static final String JWT_KEY_IDENTITY="identity";

    //生成token
    public static String generatorToken(String phone,String identity){
        Map<String,String> map =new HashMap<>();
        map.put(JWT_KEY_PHONE,phone);
        map.put(JWT_KEY_IDENTITY,identity);
        //token 过期时间:1天
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        Date date = calendar.getTime();

        JWTCreator.Builder builder = JWT.create();
        //整合map
        map.forEach(
            (k,v)->{
                builder.withClaim(k,v);
            }
        );
        //整合过期时间
        //builder.withExpiresAt(date);暂时不用了，使用redis中的过期时间来管理

        //生成token
        String sign = builder.sign(Algorithm.HMAC256(SIGN));

        return sign;
    }

    //解析token
    public static TokenResult parseToken(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        String phone = verify.getClaim(JWT_KEY_PHONE).asString();
        String identity = verify.getClaim(JWT_KEY_IDENTITY).asString();
        TokenResult tokenResult = new TokenResult();
        tokenResult.setPhone(phone);
        tokenResult.setIdentity(identity);
        return tokenResult;
    }
    //测试
    public static void main(String[] args) {
        String s = generatorToken("18849221699","1");
        System.out.println(s);
        System.out.println("解析后+++++++++++++++++++");
        System.out.println(parseToken(s).toString());
    }
}
