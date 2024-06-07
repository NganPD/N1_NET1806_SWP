package online.be.service;

import online.be.entity.PriceSlot;
import online.be.entity.Venue;
import online.be.repository.PriceSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceSlotService {

    @Autowired
    private PriceSlotRepository priceSlotRepository;

    // Lấy tất cả các PriceSlot
    public List<PriceSlot> findAll() {
        return priceSlotRepository.findAll();
    }

    // Tìm PriceSlot theo ID
    public Optional<PriceSlot> findById(long id) {
        return priceSlotRepository.findById(id);
    }

    // Lưu một PriceSlot mới hoặc cập nhật một PriceSlot đã tồn tại
    public PriceSlot save(PriceSlot priceSlot) {
        return priceSlotRepository.save(priceSlot);
    }

    // Xóa một PriceSlot theo ID
    public void deleteById(long id) {
        priceSlotRepository.deleteById(id);
    }

    // Tìm các PriceSlot theo giá
    public List<PriceSlot> findByPrice(double price) {
        return priceSlotRepository.findByPrice(price);
    }

    // Tìm các PriceSlot theo Venue
    public List<PriceSlot> findByVenue(Venue venue) {
        return priceSlotRepository.findByVenue(venue);
    }
}
