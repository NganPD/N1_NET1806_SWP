package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.Payment;
import online.be.model.Request.PaymentRequest;
import online.be.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/payment")
@SecurityRequirement(name = "api")
public class PaymentAPI {

    @Autowired
    PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentRequest paymentRequest){
        Payment createdPayment = paymentService.createPayment(paymentRequest);
        return ResponseEntity.ok().body(createdPayment);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable long paymentId){
        Payment payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok().body(payment);
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<Payment> updatePayment(@PathVariable long paymentId, @RequestBody PaymentRequest paymentRequest){
        Payment updatedPayment = paymentService.updatePayment(paymentRequest, paymentId);
        return ResponseEntity.ok().body(updatedPayment);
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable long paymentId){
        paymentService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments(){
        List<Payment> Payments = paymentService.getAllPayments();
        return ResponseEntity.ok().body(Payments);
    }
}
