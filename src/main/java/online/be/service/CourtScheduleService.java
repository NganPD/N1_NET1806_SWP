package online.be.service;

import online.be.entity.CourtSchedule;
import online.be.repository.CourtScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CourtScheduleService {

    @Autowired
    private CourtScheduleRepository courtScheduleRepository;

    // Lấy tất cả các CourtSchedule
    public List<CourtSchedule> findAll() {
        return courtScheduleRepository.findAll();
    }

    // Tìm CourtSchedule theo ID
    public Optional<CourtSchedule> findById(Long id) {
        return courtScheduleRepository.findById(id);
    }

    // Lưu một CourtSchedule mới hoặc cập nhật một CourtSchedule đã tồn tại
    public CourtSchedule save(CourtSchedule courtSchedule) {
        return courtScheduleRepository.save(courtSchedule);
    }

    // Xóa một CourtSchedule theo ID
    public void deleteById(Long id) {
        courtScheduleRepository.deleteById(id);
    }

    // Tìm các CourtSchedule theo trạng thái
    public List<CourtSchedule> findByStatus(String status) {
        return courtScheduleRepository.findByStatus(status);
    }

    // Tìm các CourtSchedule theo ngày
    public List<CourtSchedule> findByDate(LocalDate date) {
        return courtScheduleRepository.findByDate(date);
    }
}
