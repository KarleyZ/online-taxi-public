package com.ling.internalcommon.util;

public class RedisPrefixUtils {

    //存入redis的key的前缀
    public static String verificationCodePrefix ="verification-code-";

    public static String tokenPrefix = "token-";

    /**
     * 根据手机号生成redis中的key
     * @param phone
     * @return
     */
    public static String generatorKeyByPhone(String phone,String identity){
        String key = verificationCodePrefix + identity + "-" + phone;
        return key;
    }

    /**
     * 根据手机号生成token的key
     * @param passengerPhone
     * @param identity
     * @return
     */
    public static String generatorTokenKey(String passengerPhone, String identity, String tokenType){
        return tokenPrefix + passengerPhone + "-" + identity + "-" + tokenType;
    }
}
