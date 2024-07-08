package online.be.service;

import online.be.entity.Account;
import online.be.entity.Booking;
import online.be.entity.Transaction;
import online.be.entity.Wallet;
import online.be.enums.BookingStatus;
import online.be.enums.Role;
import online.be.enums.TransactionEnum;
import online.be.exception.BadRequestException;
import online.be.model.Response.PaymentResponse;
import online.be.model.Response.TransactionResponse;
import online.be.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepostory accountRepostory;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    VenueRepository venueRepository;

    public List<TransactionResponse> getTransactionById() {
        List<TransactionResponse> listTransactionResponse = new ArrayList<>();
        Account user = authenticationService.getCurrentAccount();
        Wallet wallet = walletRepository.findWalletByAccount_Id(user.getId());
        List<Transaction> transactions = transactionRepository.findTransactionByFrom_IdOrTo_Id(wallet.getWalletID());

        for (Transaction transaction : transactions) {
            TransactionResponse transactionResponseDTO = new TransactionResponse();
            transactionResponseDTO.setTransactionID(transaction.getTransactionID());
            transactionResponseDTO.setTransactionType(transaction.getTransactionType());
            transactionResponseDTO.setAmount(transaction.getAmount());
            transactionResponseDTO.setDescription(transaction.getDescription());
            transactionResponseDTO.setTransactionDate(transaction.getTransactionDate());
            if(transaction.getBooking() != null){
                transactionResponseDTO.setBooking(bookingRepository.findBookingById((long)transaction.getBooking().getId()));
            }
            transactionResponseDTO.setFrom(transaction.getFrom());
            transactionResponseDTO.setTo(transaction.getTo());

            if(transaction.getFrom() != null){
                transactionResponseDTO.setUserFrom(transaction.getFrom().getAccount());
            }

            if(transaction.getTo() != null){
                transactionResponseDTO.setUserTo(transaction.getTo().getAccount());
            }

            listTransactionResponse.add(transactionResponseDTO);
        }
        listTransactionResponse = listTransactionResponse.stream()
                .sorted(Comparator.comparing(TransactionResponse::getTransactionDate).reversed())
                .collect(Collectors.toList());

        return listTransactionResponse;
    }

    public List<TransactionResponse> allTransactions() {
        List<TransactionResponse> listTransactionResponseDTO = new ArrayList<>();
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction : transactions) {
            TransactionResponse transactionResponseDTO = new TransactionResponse();
            transactionResponseDTO.setTransactionID(transaction.getTransactionID());
            transactionResponseDTO.setTransactionType(transaction.getTransactionType());
            transactionResponseDTO.setAmount(transaction.getAmount());
            transactionResponseDTO.setDescription(transaction.getDescription());
            transactionResponseDTO.setTransactionDate(transaction.getTransactionDate());
            if(transaction.getBooking() != null){
                transactionResponseDTO.setBooking(bookingRepository.findBookingById((long)transaction.getBooking().getId()));
            }

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
        listTransactionResponseDTO = listTransactionResponseDTO.stream()
                .sorted(Comparator.comparing(TransactionResponse::getTransactionDate).reversed())
                .collect(Collectors.toList());

        return listTransactionResponseDTO;
    }



    public List<TransactionResponse> getTransactionHistory() {
        List<TransactionResponse> listTransactionResponseDTO = new ArrayList<>();
        Account user = authenticationService.getCurrentAccount();
        Wallet wallet = walletRepository.findWalletByAccount_Id(user.getId());
        List<Transaction> transactions = transactionRepository.findTransactionByFrom_IdOrTo_Id(wallet.getWalletID());

        for (Transaction transaction : transactions) {
            if(transaction.getBooking() != null) {
                TransactionResponse transactionResponseDTO = new TransactionResponse();
                transactionResponseDTO.setTransactionID(transaction.getTransactionID());
                transactionResponseDTO.setTransactionType(transaction.getTransactionType());
                transactionResponseDTO.setAmount(transaction.getAmount());
                transactionResponseDTO.setDescription(transaction.getDescription());
                transactionResponseDTO.setTransactionDate(transaction.getTransactionDate());
                transactionResponseDTO.setFrom(transaction.getFrom());
                transactionResponseDTO.setTo(transaction.getTo());

                if (transaction.getFrom() != null) {
                    transactionResponseDTO.setUserFrom(transaction.getFrom().getAccount());
                }
                if (transaction.getTo() != null) {
                    transactionResponseDTO.setUserTo(transaction.getTo().getAccount());
                }

                listTransactionResponseDTO.add(transactionResponseDTO);
            }
        }
        listTransactionResponseDTO = listTransactionResponseDTO.stream()
                .sorted(Comparator.comparing(TransactionResponse::getTransactionDate).reversed())
                .collect(Collectors.toList());

        return listTransactionResponseDTO;
    }

    public Transaction processPayment(long bookingId){
        Account user = authenticationService.getCurrentAccount();
        Wallet userWallet = walletRepository.findWalletByAccount_Id(user.getId());
        Wallet adminWallet = walletRepository.findWalletByAccountRole(Role.ADMIN);
        Booking booking = bookingRepository.findBookingById(bookingId);
        float amount = (float)booking.getTotalPrice();

        if(userWallet.getBalance() < amount){
            throw new BadRequestException("Insufficient balance");
        }

        //Deduct the amount from user's wallet
        userWallet.setBalance(userWallet.getBalance() - amount);
        walletRepository.save(userWallet);

        //Add the amount to admin's wallet
        adminWallet.setBalance(adminWallet.getBalance() + amount);
        walletRepository.save(adminWallet);

        //Create a new transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionEnum.COMPLETED);
        transaction.setFrom(userWallet);
        transaction.setTo(adminWallet);
        transaction.setBooking(booking);
        transaction.setDescription("Payment for booking");
        transaction.setTransactionDate(LocalDateTime.now().toString());
        Transaction savedTransaction = transactionRepository.save(transaction);

        //update booking status
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        return savedTransaction;
    }


}
