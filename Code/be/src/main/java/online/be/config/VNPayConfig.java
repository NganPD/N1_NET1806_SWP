package online.be.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Configuration
public class VNPayConfig {

    public static String vnp_PayUrl =  "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_ReturnUrl = "http://localhost:3000/payment";
    public static String vnp_TmnCode = "MHMAFI72";
    public static String vnp_HashSecret = "Y3CVT3KVKHWQQHXP8E1WBFLHZ46G9UDY";
    public static String vnp_apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";


    public static String md5(String message){
        String digest= null;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2*hash.length);
            for(byte b : hash){
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        }catch(NoSuchAlgorithmException ex){
            ex.printStackTrace();
        }
        return "";
    }


    public static String sha256(String message){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(message.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2*hash.length);
            for (byte b:hash){
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        }catch(NoSuchAlgorithmException ex){
            ex.printStackTrace();
        }
        return "";
    }

    public static String hashAllFields(Map fields){
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while(itr.hasNext()){
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if((fieldValue != null) && (fieldValue.length() > 0)){
                sb.append(fieldName);
                sb.append("=");
                sb.append(fieldValue);
            }
            if(itr.hasNext()){
                sb.append(("&"));
            }
        }
        return hmacSHA512(vnp_HashSecret, sb.toString());
    }

    public static String hmacSHA512(final String key,final String data){
        try{
            if(key == null || data == null){
                throw new NullPointerException();
            }
            final Mac hamc512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hamc512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hamc512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2*result.length);
            for (byte b : result){
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAddress = "Invalid IP: " + e.getMessage();
        }
        return ipAddress;
    }

    public static String getRandomNumber(int len) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        String chars = "0123456789";
        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            char randomChar = chars.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

}

