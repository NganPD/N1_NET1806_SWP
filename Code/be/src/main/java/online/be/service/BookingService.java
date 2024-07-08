package online.be.service;
import online.be.entity.*;
import online.be.enums.*;
import online.be.exception.BadRequestException;
import online.be.exception.BookingException;
import online.be.model.Request.BookingDetailRequest;
import online.be.model.Request.FlexibleBookingRequest;
import online.be.model.Request.UpdateBookingRequest;
import online.be.model.Response.BookingResponse;
import online.be.model.Response.PaymentResponse;
import online.be.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepo;

    @Autowired
   AuthenticationService authenticationService;

    @Autowired
    BookingDetailService detailService;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionRepository transactionRepository;

    public static final int MAX_FLEXIBLE_BOOKING_HOURS = 20;


///    public BookingResponse bookDailySchedule(DailyScheduleBookingRequest dailyScheduleBookingRequest){
//        //check whether user login or not
//        Account customer = authenticationRepository.findById(dailyScheduleBookingRequest.getAccountId())
//                .orElseThrow(()-> new BadRequestException("User has not authenticated yet"));
//        //check venue availability
//        List<Court> availableCourts = courtRepository.findAvailableCourtsForTimeSlot(dailyScheduleBookingRequest.getVenueId(),
//                dailyScheduleBookingRequest.getTimeSlotId());
//        if(availableCourts.isEmpty()){
//            throw new BookingException("No available courts for the selected timeslot");
//        }
//        //reference price
//        TimeSlotPrice timeSlotPrice = timeSlotPriceRepository.findByTimeSlotAndBookingType(dailyScheduleBookingRequest.getTimeSlotId(),
//                BookingType.ONE_DAY);
//        if (timeSlotPrice == null) {
//            throw new RuntimeException("No price found for the selected time slot and booking type.");
//        }
//        //Logic for booking daily schedule
//        //create a new booking entity
//        Booking booking = new Booking();
//        //set court
//        booking.setBookingDate(dailyScheduleBookingRequest.getBookingDate());
//        booking.setBookingType(BookingType.ONE_DAY);
//        booking.setStatus(BookingStatus.PENDING);
//        booking.setAccount(customer);
//        booking = bookingRepo.save(booking);
//        //create booking detail entity
//        BookingDetail bookingDetail = new BookingDetail();
//        bookingDetail.setBooking(booking);
//        bookingDetail.setDate(LocalDate.now());
//        bookingDetail.setPrice(timeSlotPrice.getPrice());
//        bookingDetail.setTimeSlot();
//    }

    public Booking createFlexibleBooking(FlexibleBookingRequest request){
        Account currentAccount = authenticationService.getCurrentAccount();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate bookingDate = LocalDate.parse(request.getBookingDate(), formatter);
        //create booking
        Booking booking = new Booking();
        List<BookingDetail> details = new ArrayList<>();
        int totalHours = 0;
        double totalPrice = 0.0;
        for (BookingDetailRequest detailRequest : request.getBookingDetailRequests()){
            BookingDetail detail = detailService.createFlexibleBookingDetail(detailRequest);
            detail.setBooking(booking);
            details.add(detail);
            totalPrice += detail.getPrice();
            totalHours += (int) detail.getDuration();
        }
        try{
            booking.setAccount(currentAccount);
            booking.setBookingDate(bookingDate);
            booking.setTotalPrice(totalPrice);
            booking.setTotalTimes(totalHours);
            booking.setRemainingTimes(totalHours);
            booking.setBookingDetailList(details);
            booking.setBookingType(BookingType.FLEXIBLE);
            booking.setStatus(BookingStatus.PENDING);
           return bookingRepo.save(booking);

        }catch (Exception ex){
            throw new RuntimeException("Something went wrong, please try again");
        }
    }

    public Booking getBookingById(long bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new BookingException("This booking ID does not exist"));
        return booking;
    }

    public List<Booking> getAllBooking() {
        return bookingRepo.findAll();
    }


    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }//Tự tạo hiển thị khi không có Booking

    public Booking updateBooking(UpdateBookingRequest updateBookingRequest, Long bookingId) {
        Booking booking = getBookingById(bookingId);


        return bookingRepo.save(booking);
    }
    //Chưa try-catch và xử lý lỗi không tồn tại

    public void deleteBooking(Long bookingId) {
        bookingRepo.deleteById(bookingId);
    }
    //Tự tạo hiển thị không có Booking

    //cancel booking
    public Transaction cancelBooking(long bookingId){
        Booking booking = bookingRepo.findBookingById(bookingId);
        LocalDate now = LocalDate.now();
        LocalDate bookingDate = booking.getBookingDate();

        boolean canCancel = false;

        switch (booking.getBookingType()){
            case FIXED:
            case FLEXIBLE:
                canCancel = now.isBefore(bookingDate.minusDays(7));
                break;
            case DAILY:
                canCancel = now.isBefore(bookingDate);
                break;
        }

        //if canCancel is true
        if(canCancel){
            //get adminWallet and userwallet
            Wallet adminWallet = walletRepository.findWalletByAccountRole(Role.ADMIN);
            Wallet userWallet = booking.getAccount().getWallet();

            float refundAmount = (float)booking.getTotalPrice();

            //update wallet balances
            adminWallet.setBalance(adminWallet.getBalance()-refundAmount);
            userWallet.setBalance(userWallet.getBalance() + refundAmount);
            walletRepository.save(adminWallet);
            walletRepository.save(userWallet);

            //create refund transaction
            Transaction refundTransaction = new Transaction();
            refundTransaction.setAmount(refundAmount);
            refundTransaction.setTransactionType(TransactionEnum.REFUND);
            refundTransaction.setFrom(adminWallet);
            refundTransaction.setTo(userWallet);
            refundTransaction.setBooking(booking);
            refundTransaction.setDescription("Refund for booking cancellation");
            refundTransaction.setTransactionDate(LocalDateTime.now().toString());
            transactionRepository.save(refundTransaction);

            //update booking status to cancel
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepo.save(booking);

            return refundTransaction;
        }else{
            //set the msg to customer that we cannot refund user's money
            throw new RuntimeException("Cancellation aborted by customer");
        }
    }

    public Transaction processBookingComission(long bookingId){
        Booking booking = bookingRepo.findBookingById(bookingId);
        CourtTimeSlot courtTimeSlot = booking.getBookingDetailList().get(0).getCourtTimeSlot();
        Venue venue = courtTimeSlot.getCourt().getVenue();
        Account manager = venue.getManager();

        Wallet managerWallet = walletRepository.findWalletByAccount_Id(manager.getId());
        Wallet adminWallet = walletRepository.findWalletByAccountRole(Role.ADMIN);

        float commissionRate = 0.1f;
        float commisssion = (float) booking.getTotalPrice()*commissionRate;
        float netAmount = (float) booking.getTotalPrice() - commisssion;

        if(adminWallet.getBalance() < booking.getTotalPrice()){
            throw new BadRequestException("Admin does not have enought balance for the payment");
        }

        //Deduct from admin wallet
        adminWallet.setBalance(adminWallet.getBalance() - netAmount);
        walletRepository.save(adminWallet);

        //add net amount to court manager wallet
        managerWallet.setBalance(managerWallet.getBalance() + netAmount);
        walletRepository.save(managerWallet);

        //Create Transaction record for the transfer
        Transaction transferTransaction  = new Transaction();
        transferTransaction.setAmount(netAmount);
        transferTransaction.setTransactionType(TransactionEnum.COMPLETED);
        transferTransaction.setFrom(adminWallet);
        transferTransaction.setTo(managerWallet);
        transferTransaction.setBooking(booking);
        transferTransaction.setDescription("Payment for court manager after commission");
        transferTransaction.setTransactionDate(LocalDateTime.now().toString());
        transactionRepository.save(transferTransaction);

        return transferTransaction;
    }

}
