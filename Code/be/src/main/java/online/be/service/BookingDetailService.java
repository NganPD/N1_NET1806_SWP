package online.be.service;

import online.be.entity.*;
import online.be.enums.BookingStatus;
import online.be.enums.BookingType;

import online.be.enums.SlotStatus;
import online.be.exception.BadRequestException;
import online.be.model.Request.BookingDetailRequest;
import online.be.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BookingDetailService {

    @Autowired
    BookingDetailRepostiory bookingDetailRepostiory;

    @Autowired
    CourtRepository courtRepository;

    @Autowired
    TimeSlotPriceRepository timeSlotPriceRepository;

    @Autowired
    BookingRepository bookingRepository;



}
