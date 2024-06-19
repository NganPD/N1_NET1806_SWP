package online.be.service;

import online.be.entity.Court;
import online.be.entity.CourtSchedule;
import online.be.entity.TimeSlot;
import online.be.exception.BadRequestException;
import online.be.model.Request.CourtScheduleRequest;
import online.be.repository.CourtRepository;
import online.be.repository.CourtScheduleRepository;
import online.be.repository.TimeSlotRepository;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourtScheduleService {

    @Autowired
    private CourtScheduleRepository courtScheduleRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private CourtRepository courtRepository;

    public List<CourtSchedule> getAllCourtSchedules(){//Tự tạo hiển thị không có Booking
        return courtScheduleRepository.findAll();
    }

    public CourtSchedule getCourtScheduleById(long id){//Tự tạo hiển thị không có Booking
        CourtSchedule schedule = courtScheduleRepository.findById(id).get();
        if(schedule == null){
            throw new RuntimeException("ScheduleId is not existed: " + id);//Tự handle lỗi để front end nhận được
        }
        return schedule;
    }

    // Lưu một CourtSchedule mới hoặc cập nhật một CourtSchedule đã tồn tại
    public CourtSchedule createSchedule(CourtScheduleRequest courtScheduleRequest) {
        Court court = courtRepository.findById(courtScheduleRequest.getCourtId()).get();
        if(court == null){
            throw new BadRequestException("Court not found");
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate requestedDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");//Tự handle lỗi để front end nhận được
            requestedDate = LocalDate.parse(courtScheduleRequest.getDate(), formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd'.");//Tự handle lỗi để front end nhận được
        }
        if (requestedDate.isBefore(currentDate)){
            throw new IllegalArgumentException("Cannot create schedule in the past.");//Tự handle lỗi để front end nhận được
        }
        List<Long> idList = courtScheduleRequest.getTimeSlotsId();
        List<TimeSlot> timeSlots = new ArrayList<>();
        for(Long slotId: idList){
            TimeSlot timeSlot = timeSlotRepository.findById(slotId).get();
            if (timeSlot == null){
                throw new IllegalArgumentException("TimeSlot is not exist.");//Tự handle lỗi để front end nhận được
            }
            timeSlots.add(timeSlot);
        }

        CourtSchedule schedule = new CourtSchedule();
        schedule.setStatus(courtScheduleRequest.getStatus());
        schedule.setDate(requestedDate);
        schedule.setTimeSlots(timeSlots);
        schedule.setCourt(court);

        return courtScheduleRepository.save(schedule);
    }
    //Sửa lại createSchedule theo luồng yêu cầu của FunctionalRequirement
    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi


    // Lưu một CourtSchedule mới hoặc cập nhật một CourtSchedule đã tồn tại
    public CourtSchedule updateCourtSchedule(long id, CourtScheduleRequest courtScheduleRequest) {
        CourtSchedule schedule = courtScheduleRepository.findById(id).get();
        if(schedule == null){
            throw new BadRequestException("ScheduleId is not existed: " + id);//Tự handle lỗi để front end nhận được
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate requestedDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");//Tự handle lỗi để front end nhận được
            requestedDate = LocalDate.parse(courtScheduleRequest.getDate(), formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd'.");//Tự handle lỗi để front end nhận được
        }
        List<Long> idList = courtScheduleRequest.getTimeSlotsId();
        List<TimeSlot> timeSlots = new ArrayList<>();
        for(Long slotId: idList){
            TimeSlot timeSlot = timeSlotRepository.findById(slotId).get();
            if (timeSlot == null){
                throw new IllegalArgumentException("TimeSlot is not exist.");//Tự handle lỗi để front end nhận được
            }
            timeSlots.add(timeSlot);
        }
        if (requestedDate.isBefore(currentDate)){
            throw new IllegalArgumentException("Cannot create schedule in the past.");//Tự handle lỗi để front end nhận được
        }
        schedule.setStatus(courtScheduleRequest.getStatus());
        schedule.setDate(requestedDate);
        schedule.setTimeSlots(timeSlots);
        return courtScheduleRepository.save(schedule);
    }
    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    // Xóa một CourtSchedule theo ID
    public void deleteById(long id) {
        courtScheduleRepository.deleteById(id);//Tự tạo hiển thị không có schedule
    }

    // Tìm các CourtSchedule theo trạng thái
    public List<CourtSchedule> findByStatus(String status) {
        return courtScheduleRepository.findByStatus(status);////Tự tạo hiển thị không có schdule
    }

    // Tìm các CourtSchedule theo ngày
    public List<CourtSchedule> findByDate(LocalDate date) {
        return courtScheduleRepository.findByDate(date);//Tự tạo hiển thị không có schedule
    }
}
