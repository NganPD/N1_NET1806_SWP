package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.Transaction;
import online.be.entity.Wallet;
import online.be.model.Request.RechargeRequest;
import online.be.model.Response.TransactionResponse;
import online.be.model.Response.WalletResponse;
import online.be.service.WalletService;
import online.be.model.Request.WithDrawRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class WalletAPI {

    @Autowired
    WalletService walletService;

    @PostMapping("/request-recharge-vnpay")
    public ResponseEntity createUrl(@RequestBody RechargeRequest request)
            throws NoSuchAlgorithmException, InvalidKeyException, Exception {
        String url = walletService.createUrl(request);
        return ResponseEntity.ok(url);
    }

    @PostMapping("/recharge/{id}")
    public ResponseEntity recharge(@PathVariable UUID id) throws Exception{
        Wallet wallet = walletService.recharge(id);
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/withDraw")
    public ResponseEntity withDraw(@RequestBody WithDrawRequest withDrawRequest) throws Exception{
        Transaction transaction = walletService.withDraw(withDrawRequest);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/requestWithDraw")
    public ResponseEntity requestWithDraw() throws Exception{
        List<TransactionResponse> list = walletService.requestWithDraw();
        return ResponseEntity.ok(list);
    }

//    @PutMapping("/acceptWithDraw")
//    public ResponseEntity acpWithDraw(@RequestParam("TransactionId") UUID id) throws Exception{
//        Transaction transaction = walletService.acpWithDraw(id);
//        return ResponseEntity.ok(transaction);
//    }
//
//    @PutMapping("/rejectWithDraw")
//    public ResponseEntity rejectWithDraw(@RequestParam("TransactionId") UUID id,
//                                         @RequestParam("reason") String reason){
//        Transaction transaction = walletService.rejectWithDraw(id, reason);
//        return ResponseEntity.ok(transaction);
//    }

    @GetMapping("/walletDetail/{id}")
    public ResponseEntity walletDetail(@PathVariable long id) throws Exception{
        Wallet wallet = walletService.walletDetail(id);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/balance")
    public ResponseEntity getBalance(){
        float balance = walletService.getBalance();
        WalletResponse response = new WalletResponse(balance);
        return ResponseEntity.ok(response);
    }
}