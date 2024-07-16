//package online.be.api;
//
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import online.be.entity.BookingDetail;
////import online.be.service.BookingDetailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("api/booking-details")
//@SecurityRequirement(name = "api")
//public class BookingDetailAPI {
//    @Autowired
//    BookingDetailService bookingDetailService;
//
//    @GetMapping("/{bookingId}")
//    public ResponseEntity getByBookingId(long bookingId){
//        List<BookingDetail> details = bookingDetailService.getBookingDetailByBookingId(bookingId);
//        return ResponseEntity.ok(details);
//    }
//
//    @GetMapping("/{bookingDetailId}")
//    public ResponseEntity getByDetailId(long bookingDetailId){
//        return ResponseEntity.ok(bookingDetailService.getBookingDetailById(bookingDetailId));
//    }
//}
