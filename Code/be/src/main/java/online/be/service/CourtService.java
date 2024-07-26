package online.be.service;

import online.be.entity.Court;
import online.be.entity.CourtTimeSlot;
import online.be.entity.TimeSlot;
import online.be.entity.Venue;
import online.be.enums.CourtStatus;
import online.be.enums.SlotStatus;
import online.be.exception.BadRequestException;
import online.be.model.Request.CreateCourtRequest;
import online.be.model.Request.UpdateCourtRequest;
import online.be.model.Response.CourtResponse;
import online.be.model.SlotIdCountDTO;
import online.be.repository.CourtRepository;
import online.be.repository.CourtTimeSlotRepository;
import online.be.repository.TimeSlotRepository;
import online.be.repository.VenueRepository;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourtService {

    @Autowired
    CourtRepository courtRepository;
    @Autowired
    VenueRepository venueRepository;
    @Autowired
    TimeSlotRepository timeSlotRepository;
    @Autowired
    CourtTimeSlotRepository courtTimeSlotRepository;


    //tạo court
    public Court createCourt(CreateCourtRequest courtRequest) {
        //Find the venue ỏ throw exception if not found
        Venue existingVenue = venueRepository.findById(courtRequest.getVenueId())
                .orElseThrow(()-> new BadRequestException("The venue does not exist"));

        //Create a new Court
        Court court = new Court();
        court.setCourtName(courtRequest.getCourtName());
        court.setStatus(courtRequest.getStatus());
        court.setDescription(courtRequest.getDescription());
        court.setVenue(existingVenue);
        courtRepository.save(court);
        //lưu số lượng san
        venueRepository.save(existingVenue);
        return court;
    }
    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    //lấy court dựa trên id
    public Court getCourtById(long courtId) {
        Court court = courtRepository.findById(courtId).orElseThrow(() -> new BadRequestException("Cpurt not found with Id: "+ courtId));
        if (court == null) {
            throw new BadRequestException("CourtId is not existed: " + courtId);
        }
        return court;
    }


    //show toàn bộ court
    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    //update Court
    public Court updateCourt(UpdateCourtRequest courtRequest, long courtId) {
        Venue existingVenue = venueRepository.findById(courtRequest.getVenueId())
                .orElseThrow(()-> new BadRequestException("Venue not found"));
        Court existingCourt = courtRepository.findById(courtId)
                .orElseThrow(()-> new BadRequestException("Court not found "));

        existingCourt.setCourtName(courtRequest.getCourtName());
        existingCourt.setStatus(courtRequest.getStatus());
        existingCourt.setDescription(courtRequest.getDescription());

        existingCourt = courtRepository.save(existingCourt);
        //lưu số lượng san
        venueRepository.save(existingVenue);
        return existingCourt;
    }
    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    //delete Court
    public void deActiveCourt(long courtId) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(()->new BadRequestException("Court not found with Id: " + courtId));
        court.setStatus(CourtStatus.INACTIVE);
        courtRepository.save(court);
    }

    public Court activeCourt(long courtId){
        Court court = courtRepository.findById(courtId)
                .orElseThrow(()->new BadRequestException("Court not found with Id: " + courtId));
        court.setStatus(CourtStatus.AVAILABLE);
        return courtRepository.save(court);
    }

    public List<CourtResponse> getAvailableCourts(String date, long venueId, List<Long> timeSlots){
        LocalDate checkInDate = LocalDate.parse(date);

        // Tìm các court_id bị BOOKED cho thời gian đã chọn
        List<Long> bookedCourtIds = courtRepository.findCourtIdsByTimeSlotsAndDate(timeSlots, checkInDate);

        // Nếu không có court nào bị BOOKED, lấy tất cả courts của venue
        List<Court> courts = courtRepository.findAllCourtsByVenue(venueId);
        //danh sacch court response
        List<CourtResponse> courtResponses = new ArrayList<>();
        for (Court court : courts){
            boolean isAvailable = !bookedCourtIds.contains(court.getId());
            CourtResponse response = new CourtResponse(court.getId(), isAvailable);
            courtResponses.add(response);
        }
        return courtResponses;
    }

    public List<CourtResponse> getFixedAvailableCourts(String startDateStr, Integer durationMonths, List<String> dayOfWeeks, List<Long> timeSlotIds, Long venueId) {
        List<CourtResponse> availableCourtsResponse = new ArrayList<>();

        // Kiểm tra các tham số đầu vào
        if (startDateStr == null || durationMonths == null || dayOfWeeks == null || timeSlotIds == null || venueId == null) {
            throw new BadRequestException("Invalid input parameters");
        }

        // Xử lý và phân tích ngày
        LocalDate startDate;
        try {
            startDate = LocalDate.parse(startDateStr);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format: " + startDateStr);
        }

        // Tính toán ngày kết thúc
        LocalDate endDate = startDate.plusDays(29L * durationMonths);

        // Lấy tất cả courts của venue
        List<Court> allCourts = courtRepository.findAllCourtsByVenue(venueId);

        // Duyệt qua từng court
        for (Court court : allCourts) {
            boolean isAvailable = true;

            // Duyệt qua các ngày trong khoảng thời gian chỉ định
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                if (dayOfWeeks.contains(date.getDayOfWeek().name())) {
                    // Kiểm tra nếu court này đã được đặt cho bất kỳ timeslot nào vào ngày này
                    List<CourtTimeSlot> courtTimeSlots = courtTimeSlotRepository.findByCourtIdAndTimeSlotIdsAndDate(court.getId(), timeSlotIds, date);

                    if (!courtTimeSlots.isEmpty()) {
                        isAvailable = false;
                        break;
                    }
                }
            }

            // Tạo response và thêm vào danh sách
            CourtResponse response = new CourtResponse(court.getId(), isAvailable);
            availableCourtsResponse.add(response);
        }

        return availableCourtsResponse;
    }
}