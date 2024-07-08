//
//package online.be.api;
//
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import jakarta.servlet.http.HttpServletRequest;
//import online.be.model.Response.PaymentResponse;
//import online.be.service.WalletService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("api/payment")
//@SecurityRequirement(name = "api")
//public class PaymentAPI {
//
//    @Autowired
//    private WalletService paymentService;
//
//    @GetMapping("/create")
//    public PaymentResponse createPayment(HttpServletRequest request){
//        return paymentService.createVnPayPayment(request);
//    }
//
//}
