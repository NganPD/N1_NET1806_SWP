package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.BookingDetail;
import online.be.model.Request.BookingDetailRequest;
import online.be.service.BookingDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/bookingDetail")
@SecurityRequirement(name = "api")
public class BookingDetailAPI {

    @Autowired
    BookingDetailService bookingDetailService;

//    @GetMapping("/{bookingDetailId}")
//    public ResponseEntity getDetailById(@PathVariable long bookingDetailId){
//        BookingDetail bookingDetail = bookingDetailService.getById(bookingDetailId);
//        return ResponseEntity.ok().body(bookingDetail);
//    }

    @GetMapping("/time-prices/{id}")
    public ResponseEntity getByTimeSlotPricesId(@PathVariable long id) {
        return ResponseEntity.ok(bookingDetailService.getByTimeSlotPricesId(id));
    }

    @PostMapping
    public ResponseEntity createFlexibleBookingDetail(@RequestBody BookingDetailRequest bookingDetailRequest){
        BookingDetail bookingDetail = bookingDetailService.createFlexibleBookingDetail(bookingDetailRequest);
        return ResponseEntity.ok().body(bookingDetail);
    }

//    @PutMapping("/{bookingDetailId}")
//    public ResponseEntity updateBookingDetail(@RequestBody BookingDetailRequest bookingDetailRequest, @PathVariable long id){
//        BookingDetail bookingDetail = bookingDetailService.updateBookingDetail(bookingDetailRequest, id);
//        return ResponseEntity.ok().body(bookingDetail);
//    }
//
//    @DeleteMapping("/{bookingDetailId}")
//    public void deleteBookingDetail(@PathVariable long id){
//        bookingDetailService.deleteBookingDetail(id);
//    }
//
//    @GetMapping
//    public ResponseEntity getAllDetails(){
//        return ResponseEntity.ok(bookingDetailService.getAll());
//    }

}
