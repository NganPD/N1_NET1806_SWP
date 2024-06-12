package online.be.service;

import online.be.repository.BookingDetailRepostiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingDetailService {

    @Autowired
    BookingDetailRepostiory bookingDetailRepostiory;



}
