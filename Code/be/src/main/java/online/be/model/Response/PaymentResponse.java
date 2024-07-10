package online.be.model.Response;

import lombok.Data;

@Data
public class PaymentResponse {
    String code;
    String message;
    String paymentUrl;
}
