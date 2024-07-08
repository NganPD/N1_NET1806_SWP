//package online.be.service;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import online.be.config.VNPayConfig;
//import online.be.model.Response.PaymentResponse;
//import org.springframework.stereotype.Service;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.TimeZone;
//
//@Service
//@RequiredArgsConstructor
//public class VnPayService {
//
//    public PaymentResponse createVnPayPayment(HttpServletRequest req) {
//        String vnp_Version = "2.1.0";
//        String vnp_Command = "pay";
//        String vnp_OrderInfo = req.getParameter("vnp_OrderInfo");
//        String orderType = req.getParameter("ordertype");
//        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
//        String vnp_IpAddr = VNPayConfig.getIpAddress(req);
//        String vnp_TmnCode = VNPayConfig.vnp_TmpCode;
//        int amount = Integer.parseInt(req.getParameter("amount")) * 100;
//        Map<String, String> vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_Version", vnp_Version);
//        vnp_Params.put("vnp_Command", vnp_Command);
//        vnp_Params.put("vnp_Amount", String.valueOf(amount));
//        vnp_Params.put("vnp_CurrCode", "VND");
//        String bank_code = req.getParameter("bankcode");
//        if (bank_code != null && !bank_code.isEmpty()) {
//            vnp_Params.put("vnp_BankCode", bank_code);
//        }
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
//        vnp_Params.put("vnp_OrderType", orderType);
//
//        String locate = req.getParameter("language");
//        if (locate != null && !locate.isEmpty()) {
//            vnp_Params.put("vnp_Locale", locate);
//        } else {
//            vnp_Params.put("vnp_Locale", "vn");
//        }
//        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnp_CreateDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//
//        cld.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//
//        String vnp_SecureHash = VNPayConfig.hashAllFields(vnp_Params);
//        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);
//
//        StringBuilder query = new StringBuilder();
//        for (Map.Entry<String, String> param : vnp_Params.entrySet()) {
//            if (query.length() > 0) {
//                query.append("&");
//            }
//            query.append(param.getKey()).append("=").append(param.getValue());
//        }
//        String payUrl = VNPayConfig.vnp_PayUrl + "?" + query.toString();
//
//        PaymentResponse response = new PaymentResponse();
//        response.setCode("00"); // Assuming "00" means success
//        response.setMessage("Successfully created payment URL");
//        response.setPaymentUrl(payUrl);
//
//        return response;
//    }
//}
