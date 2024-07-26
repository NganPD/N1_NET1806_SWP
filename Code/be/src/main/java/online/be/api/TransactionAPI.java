package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.model.Response.TransactionResponse;
import online.be.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "api")
@RequestMapping("/api/transaction")
@CrossOrigin("*")
public class TransactionAPI {

    @Autowired
    TransactionService transactionService;

    @GetMapping("/transactionById")
    public ResponseEntity getTransactionById(){
        List<TransactionResponse> transactionResponseList = transactionService.getTransactionById();
        return ResponseEntity.ok(transactionResponseList);
    }

    @GetMapping("/allTransaction")
    public ResponseEntity transactions(){
        List<TransactionResponse> transactions = transactionService.allTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactionHistory")
    public ResponseEntity getTransactionHistory(){
        List<TransactionResponse> transactionHistory = transactionService.getTransactionHistory();
        return ResponseEntity.ok(transactionHistory);
    }

}