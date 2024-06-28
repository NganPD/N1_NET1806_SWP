
package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import online.be.entity.Payment;
import online.be.model.Response.PaymentResponse;
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
    private PaymentService paymentService;

    @GetMapping("/create")
    public PaymentResponse createPayment(HttpServletRequest request){
        return paymentService.createVnPayPayment(request);
    }

}
