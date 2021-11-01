package com.hg.hyy.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSHA256 {
    public static String sha256_mac(String message,String key){
        String outPut= null;
        try{
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(),"HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            outPut = byteArrayToHexString(bytes);
        }catch (Exception e){
            System.out.println("Error HmacSHA256========"+e.getMessage());
        }
        return outPut;
    }
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                sb.append('0');
            sb.append(stmp);
        }
        return sb.toString().toLowerCase();
    }

}
