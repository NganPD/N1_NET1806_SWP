package online.be.api;

import online.be.entity.PriceSlot;
import online.be.entity.Venue;
import online.be.service.PriceSlotService;
import online.be.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/priceslots")
public class PriceSlotManagementAPI {

    @Autowired
    private PriceSlotService priceSlotService;

    @Autowired
    private VenueService venueService;

    // Lấy tất cả các PriceSlot
    @GetMapping
    public ResponseEntity<List<PriceSlot>> getAllPriceSlots() {

        return ResponseEntity.ok(priceSlotService.findAll());
    }

    // Lấy PriceSlot theo ID
    @GetMapping("/{id}")
    public ResponseEntity<PriceSlot> getPriceSlotById(@PathVariable Long id) {
        Optional<PriceSlot> priceSlot = priceSlotService.findById(id);
        return priceSlot.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Tạo mới một PriceSlot
    @PostMapping
    public ResponseEntity<PriceSlot> createPriceSlot(@RequestBody PriceSlot priceSlot) {
        return ResponseEntity.ok(priceSlotService.save(priceSlot));
    }

    // Cập nhật thông tin một PriceSlot
    @PutMapping("/{id}")
    public ResponseEntity<PriceSlot> updatePriceSlot(@PathVariable Long id, @RequestParam Long venueId, @RequestBody PriceSlot priceSlotDetails) {
        Optional<PriceSlot> priceSlot = priceSlotService.findById(id);
        if (priceSlot.isPresent()) {
            priceSlot.get().setPrice(priceSlotDetails.getPrice());
            Venue venue = venueService.getVenueById(venueId); // Lấy Venue từ venueId
            if (venue != null) {
                priceSlot.get().setVenue(venue); // Gán Venue vào PriceSlot
                return ResponseEntity.ok(priceSlotService.save(priceSlot.orElse(null)));
            } else {
                // Xử lý trường hợp không tìm thấy Venue
                return ResponseEntity.badRequest().build(); // Hoặc thông báo lỗi khác tùy vào yêu cầu của bạn
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    // Xóa một PriceSlot
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriceSlot(@PathVariable Long id) {
        if (priceSlotService.findById(id).isPresent()) {
            priceSlotService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Lấy danh sách PriceSlot theo giá
    @GetMapping("/price/{price}")
    public ResponseEntity<List<PriceSlot>> getPriceSlotsByPrice(@PathVariable double price) {
        return ResponseEntity.ok(priceSlotService.findByPrice(price));
    }

    // Lấy danh sách PriceSlot theo Venue
    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<PriceSlot>> getPriceSlotsByVenue(@PathVariable Long venueId) {
        Venue venue = venueService.getVenueById(venueId);
        if (venue != null) {
            List<PriceSlot> priceSlots = priceSlotService.findByVenue(venue);
            return ResponseEntity.ok(priceSlots);
        } else {
            // Xử lý trường hợp không tìm thấy Venue
            // Trả về một phản hồi HTTP có trạng thái lỗi
            return ResponseEntity.notFound().build();
        }
    }
}
