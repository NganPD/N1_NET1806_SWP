package online.be.service;

import online.be.entity.Payment;
import online.be.model.Request.PaymentRequest;
import online.be.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PaymentService {
    @Autowired
    PaymentRepository paymentRepo;

    //create a payment
    public Payment createPayment(PaymentRequest paymentRequest){
        //payment request: thong tin can de thuc hien thanh toan

        Payment payment = new Payment();
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentDate(paymentRequest.getPaymentDate());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setBooking(paymentRequest.getBooking());

        return paymentRepo.save(payment);
    }

    public Payment getPaymentById(Long paymentId){
        Payment payment = paymentRepo.findById(paymentId).get();
        if(payment == null){
            throw new RuntimeException("Does not exist this paymentId: " + paymentId);
        }
        return payment;
    }


    public List<Payment> getAllPayments(){
        return paymentRepo.findAll();
    }

    //update payment
    public Payment updatePayment(PaymentRequest paymentRequest, Long paymentId){
        Payment payment = getPaymentById(paymentId);

        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setPaymentDate(paymentRequest.getPaymentDate());
        payment.setAmount(paymentRequest.getAmount());
        payment.setStatus(paymentRequest.getPaymentStatus());
        payment.setBooking(paymentRequest.getBooking());

        return paymentRepo.save(payment);

    }

    //Delete payment
    public void deletePayment(Long paymentId){
        paymentRepo.deleteById(paymentId);
    }
}
