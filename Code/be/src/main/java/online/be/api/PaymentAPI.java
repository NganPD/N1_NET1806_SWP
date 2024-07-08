//
//package online.be.api;
//
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import jakarta.servlet.http.HttpServletRequest;
<<<<<<< HEAD
//import online.be.model.Response.PaymentResponse;
//import online.be.service.WalletService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
=======
//import online.be.entity.Payment;
//import online.be.model.Response.PaymentResponse;
//import online.be.service.PaymentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
>>>>>>> fbae02a37c77a5ede20d08c2ac77357976a03be8
//@RestController
//@RequestMapping("api/payment")
//@SecurityRequirement(name = "api")
//public class PaymentAPI {
//
//    @Autowired
<<<<<<< HEAD
//    private WalletService paymentService;
=======
//    private PaymentService paymentService;
>>>>>>> fbae02a37c77a5ede20d08c2ac77357976a03be8
//
//    @GetMapping("/create")
//    public PaymentResponse createPayment(HttpServletRequest request){
//        return paymentService.createVnPayPayment(request);
//    }
//
//}
