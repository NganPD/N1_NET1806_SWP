//package online.be.service;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import online.be.config.VNPayConfig;
//import online.be.model.Response.PaymentResponse;
//import org.springframework.stereotype.Service;
//
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Service
//@RequiredArgsConstructor
//public class PaymentService {
//
//    private final VNPayConfig vnPayConfig;
//
//    public PaymentResponse createVnPayPayment(HttpServletRequest req) {
//        String vnp_Version = "2.1.0";
//        String vnp_Command = "pay";
//        String vnp_OrderInfo = req.getParameter("vnp_OrderInfo");
//        String orderType = req.getParameter("ordertype");
//        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
//        String vnp_IpAddr = VNPayConfig.getIpAddress(req);
//        String vnp_TmnCode = VNPayConfig.vnp_TmpCode;
//        int amount = 100000;
////                Integer.parseInt(req.getParameter("amount")) * 100;
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
//
//
//
//    public String createUrl(RechargeRequestDTO rechargeRequestDTO) throws NoSuchAlgorithmException, InvalidKeyException, Exception{
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        LocalDateTime createDate = LocalDateTime.now();
//        String formattedCreateDate = createDate.format(formatter);
//
//
//
//        User user = accountUtils.getCurrentUser();
//
//        String orderId = UUID.randomUUID().toString().substring(0,6);
//
//        Wallet wallet = walletRepository.findWalletByUser_Id(user.getId());
//
//        Transaction transaction = new Transaction();
//
//        transaction.setAmount(Float.parseFloat(rechargeRequestDTO.getAmount()));
//        transaction.setTransactionType(TransactionEnum.PENDING);
//        transaction.setTo(wallet);
//        transaction.setTransactionDate(formattedCreateDate);
//        transaction.setDescription("Recharge");
//        Transaction transactionReturn = transactionRepository.save(transaction);
//
//        String tmnCode = "EDXJUBE1";
//        String secretKey = "AYBLLQTVRSRCZCGJGXJQCMNMLIYKKILF";
//        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
//        String returnUrl = "http://mycremo.art/profile/wallet?id="+transactionReturn.getTransactionID();
//
//        String currCode = "VND";
//        Map<String, String> vnpParams = new TreeMap<>();
//        vnpParams.put("vnp_Version", "2.1.0");
//        vnpParams.put("vnp_Command", "pay");
//        vnpParams.put("vnp_TmnCode", tmnCode);
//        vnpParams.put("vnp_Locale", "vn");
//        vnpParams.put("vnp_CurrCode", currCode);
//        vnpParams.put("vnp_TxnRef", orderId);
//        vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD: " + orderId);
//        vnpParams.put("vnp_OrderType", "other");
//        vnpParams.put("vnp_Amount", rechargeRequestDTO.getAmount() +"00");
//        vnpParams.put("vnp_ReturnUrl", returnUrl);
//        vnpParams.put("vnp_CreateDate", formattedCreateDate);
//        vnpParams.put("vnp_IpAddr", "128.199.178.23");
//
//        StringBuilder signDataBuilder = new StringBuilder();
//        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
//            signDataBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
//            signDataBuilder.append("=");
//            signDataBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
//            signDataBuilder.append("&");
//        }
//        signDataBuilder.deleteCharAt(signDataBuilder.length() - 1); // Remove last '&'
//
//        String signData = signDataBuilder.toString();
//        String signed = generateHMAC(secretKey, signData);
//
//        vnpParams.put("vnp_SecureHash", signed);
//
//        StringBuilder urlBuilder = new StringBuilder(vnpUrl);
//        urlBuilder.append("?");
//        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
//            urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
//            urlBuilder.append("=");
//            urlBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
//            urlBuilder.append("&");
//        }
//        urlBuilder.deleteCharAt(urlBuilder.length() - 1); // Remove last '&'
//
//        return urlBuilder.toString();
//    }
//
//
//
//
//
//    private String generateHMAC(String secretKey, String signData) throws NoSuchAlgorithmException, InvalidKeyException {
//        Mac hmacSha512 = Mac.getInstance("HmacSHA512");
//        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
//        hmacSha512.init(keySpec);
//        byte[] hmacBytes = hmacSha512.doFinal(signData.getBytes(StandardCharsets.UTF_8));
//
//        StringBuilder result = new StringBuilder();
//        for (byte b : hmacBytes) {
//            result.append(String.format("%02x", b));
//        }
//        return result.toString();
//    }
//
//}
