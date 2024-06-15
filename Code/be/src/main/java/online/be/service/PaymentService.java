package online.be.service;

import online.be.entity.Payment;
import online.be.model.Request.PaymentRequest;
import online.be.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepo;

    //create a payment
    public Payment createPayment(PaymentRequest paymentRequest){
        //payment request: thong tin can de thuc hien thanh toan
        LocalDate requestedDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            requestedDate = LocalDate.parse(paymentRequest.getPaymentDate(), formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd'.");
        }
        Payment payment = new Payment();
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentDate(requestedDate);
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
        LocalDate requestedDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            requestedDate = LocalDate.parse(paymentRequest.getPaymentDate(), formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd'.");
        }
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setPaymentDate(requestedDate);
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
//Huỷ payment service