package online.be.service;

import lombok.RequiredArgsConstructor;
import online.be.config.VNPayConfig;
import online.be.entity.Account;
import online.be.entity.Transaction;
import online.be.entity.Wallet;
import online.be.enums.Role;
import online.be.enums.TransactionEnum;
import online.be.exception.BookingException;
import online.be.model.Request.RechargeRequest;
import online.be.model.Request.WithDrawRequest;
import online.be.model.Response.PaymentResponse;
import online.be.model.Response.TransactionResponse;
import online.be.repository.TransactionRepository;
import online.be.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final VNPayConfig vnPayConfig;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    WalletRepository walletRepository;


    public String createUrl(RechargeRequest rechargeRequest)
            throws NoSuchAlgorithmException, InvalidKeyException,
            Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);

        Account user = authenticationService.getCurrentAccount();
        String orderId = UUID.randomUUID().toString().substring(0,6);
        Wallet wallet = walletRepository.findWalletByAccount_Id(user.getId());

        Transaction transaction = new Transaction();
        transaction.setAmount(Float.parseFloat(rechargeRequest.getAmount()));
        transaction.setTransactionType(TransactionEnum.PENDING);
        transaction.setTo(wallet);
        transaction.setTransactionDate(formattedCreateDate);
        transaction.setDescription("Recharge");
        Transaction transactionReturn = transactionRepository.save(transaction);

        String tmnCode = VNPayConfig.vnp_TmpCode;
        String secretKey = VNPayConfig.vnp_HashSecret;
        String vnpUrl = VNPayConfig.vnp_PayUrl;
        String returnUrl = "http://goodminton.online/myProfile/wallet?id="+transactionReturn.getTransactionID();

        String currCode = "VND";
        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_TxnRef", orderId);
        vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD: " + orderId);
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Amount", rechargeRequest.getAmount() +"00");
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_CreateDate", formattedCreateDate);
        vnpParams.put("vnp_IpAddr", "104.248.224.6");

        StringBuilder signDataBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            signDataBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("=");
            signDataBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("&");
        }
        signDataBuilder.deleteCharAt(signDataBuilder.length() - 1); // Remove last '&'

        String signData = signDataBuilder.toString();
        String signed = generateHMAC(secretKey, signData);

        vnpParams.put("vnp_SecureHash", signed);

        StringBuilder urlBuilder = new StringBuilder(vnpUrl);
        urlBuilder.append("?");
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("=");
            urlBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1); // Remove last '&'

        return urlBuilder.toString();
    }


    private String generateHMAC(String secretKey, String signData)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmacSha512.init(keySpec);
        byte[] hmacBytes = hmacSha512.doFinal(signData.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();
        for (byte b : hmacBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

//    public PaymentResponse createVnPayPayment(RechargeRequest request)
//    throws NoSuchAlgorithmException, InvalidKeyException, Exception{
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        LocalDateTime createDate = LocalDateTime.now();
//        String formattedCreateDate = createDate.format(formatter);
//
//        Account user = authenticationService.getCurrentAccount();
//
//        Wallet wallet = walletRepository.findWalletByAccount_Id(user.getId());
//
//        Transaction transaction = new Transaction();
//        transaction.setAmount(Float.parseFloat(request.getAmount()));
//        transaction.setTransactionType(TransactionEnum.PENDING);
//        transaction.setTo(wallet);
//        transaction.setDescription("Recharge");
//        transaction.setTransactionDate(formattedCreateDate);
//        Transaction transactionReturn = transactionRepository.save(transaction);
//
//        int amount = Integer.parseInt(request.getAmount());
//        String orderInfo = "Recharge";
//        String orderType = "other";
//        String bankCode = null; // Set the bank code if required
//        String locale = "vn";
//
//        String returnUrl = "";
//        if (user.getRole().equals(Role.CUSTOMER)) {
//            returnUrl = "http://mycremo.art/profile/wallet?id=" + transactionReturn.getTransactionID(); //ví của customer
//        } else if (user.getRole().equals(Role.ADMIN)) {
//            returnUrl = "http://mycremo.art/creator-manage/wallet?id=" + transactionReturn.getTransactionID(); //ví của admin
//        }
//
//        PaymentResponse paymentResponse = new PaymentResponse();
//        paymentResponse.setPaymentUrl(createUrl(request));
//
//        return paymentResponse;
//    }


public Wallet recharge(UUID id) {
    Account user = authenticationService.getCurrentAccount();
    Transaction transaction = transactionRepository.findByTransactionID(id);
    Wallet wallet = walletRepository.findWalletByAccount_Id(user.getId());
    if(transaction.getTransactionType().equals(TransactionEnum.PENDING)) {
        if(wallet.getWalletID() == transaction.getTo().getWalletID()){
            wallet.setBalance(wallet.getBalance()+transaction.getAmount());
        }
    }
    else{
        throw new RuntimeException("Reload");
    }
    transaction.setTransactionType(TransactionEnum.RECHARGE);

    transactionRepository.save(transaction);
    return walletRepository.save(wallet);
}

public  Wallet walletDetail(long id) {
    return  walletRepository.findWalletByAccount_Id(id);
}

public Transaction withDraw(WithDrawRequest withDrawRequest) {
    Account user = authenticationService.getCurrentAccount();
    Wallet wallet = walletRepository.findWalletByAccount_Id(user.getId());
    if (wallet.getBalance() >= (withDrawRequest.getAmount())) {
        Transaction transaction = new Transaction();
        transaction.setAmount((withDrawRequest.getAmount()));
        transaction.setTransactionType(TransactionEnum.WITHDRAW_PENDING);
        transaction.setFrom(wallet);
        transaction.setDescription("WITHDRAW");
        transaction.setAccountName(withDrawRequest.getAccountName());
        transaction.setBankName(withDrawRequest.getBankName());
        transaction.setAccountNumber(withDrawRequest.getAccountNumber());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        transaction.setTransactionDate(now.format(formatter));
        wallet.setBalance(wallet.getBalance()-(withDrawRequest.getAmount()));
        walletRepository.save(wallet);
        return transactionRepository.save(transaction);
    } else {
        throw new RuntimeException("Insufficient balance in wallet for withdrawal.");
    }
}

public List<TransactionResponse> requestWithDraw() {
    List<TransactionResponse> listTransactionResponseDTO = new ArrayList<>();
    List<Transaction> transactions = transactionRepository.findTransactionByTransactionType(TransactionEnum.WITHDRAW_PENDING);
    for (Transaction transaction : transactions) {
        TransactionResponse transactionResponseDTO = new TransactionResponse();
        transactionResponseDTO.setTransactionID(transaction.getTransactionID());
        transactionResponseDTO.setTransactionType(transaction.getTransactionType());
        transactionResponseDTO.setAmount(transaction.getAmount());
        transactionResponseDTO.setDescription(transaction.getDescription());
        transactionResponseDTO.setTransactionDate(transaction.getTransactionDate());
        transactionResponseDTO.setFrom(transaction.getFrom());
        transactionResponseDTO.setTo(transaction.getTo());
        if(transaction.getFrom() != null){
            transactionResponseDTO.setUserFrom(transaction.getFrom().getAccount());
        }
        if(transaction.getTo() != null){
            transactionResponseDTO.setUserTo(transaction.getTo().getAccount());
        }
        listTransactionResponseDTO.add(transactionResponseDTO);
    }
    return  listTransactionResponseDTO;
}

public Transaction acpWithDraw(UUID id) {
    Transaction transaction = transactionRepository.findByTransactionID(id);
    if (transaction != null) {
        transaction.setTransactionType(TransactionEnum.WITHDRAW_SUCCESS);
        threadSendMail(transaction.getFrom().getAccount(), "Withdrawal Successfully", "Thank you for trusting and using Cremo");
        transactionRepository.save(transaction);
        return  transaction;
    }
    else{
        return null;
    }

}


public Transaction rejectWithDraw(UUID id, String reason) {
    Transaction transaction = transactionRepository.findByTransactionID(id);
    if (transaction != null) {
        Wallet wallet = transaction.getFrom();
        wallet.setBalance(wallet.getBalance()+ transaction.getAmount());
        transaction.setTransactionType(TransactionEnum.WITHDRAW_REJECT);
        transaction.setReasonWithdrawReject(reason);
        threadSendMail(transaction.getFrom().getAccount(), "Withdrawal failed", "You Cannot Withdraw Because: " + reason);
        return transactionRepository.save(transaction);
    } else {
        return null;
    }
}

public void threadSendMail(Account user,String subject, String description){
    Runnable r = new Runnable() {
        @Override
        public void run() {
            emailService.sendMail(user,subject,description);
        }

    };
    new Thread(r).start();
}

public float getBalance(){
        Account user = authenticationService.getCurrentAccount();
        Wallet wallet = walletRepository.findWalletByAccount_Id(user.getId());
        if(wallet != null){
            return wallet.getBalance();
        }else{
            throw new BookingException("Wallet not found for user ID: " + user.getId());
    }
}

public Wallet getWalletByTransactionId(UUID transactionId){
        Transaction transaction = transactionRepository.findByTransactionID(transactionId);
        Wallet wallet = walletRepository.findWalletByWalletID(transaction.getTo().getWalletID());
        return wallet;
}

}