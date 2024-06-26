package online.be.service;

import online.be.entity.Court;
import online.be.entity.CourtSchedule;
import online.be.entity.TimeSlot;
import online.be.exception.BadRequestException;
import online.be.exception.ResourceNotFoundException;
import online.be.model.Request.CreateScheduleRequest;
import online.be.model.Request.UpdateScheduleRequest;
import online.be.model.Response.UpdateScheduleResponse;
import online.be.repository.CourtRepository;
import online.be.repository.CourtScheduleRepository;
import online.be.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    CourtScheduleRepository scheduleRepo;
    @Autowired
    CourtRepository courtRepo;
    @Autowired
    TimeSlotRepository slotRepo;

    public CourtSchedule createSchedule(CreateScheduleRequest scheduleDetail) {
        TimeSlot slot = slotRepo.findById(scheduleDetail.getTimeSlotId())
                .orElseThrow(() -> new BadRequestException("TimeSlot not found"));
        Court court = courtRepo.findById(scheduleDetail.getCourtId())
                .orElseThrow(() -> new BadRequestException("Court not found"));

        CourtSchedule schedule = new CourtSchedule();
        try {
            schedule.setDate(scheduleDetail.getDate());
            schedule.setAvailable(scheduleDetail.isAvailable());
            schedule.setTimeSlot(slot);
            schedule.setCourt(court);
            scheduleRepo.save(schedule);
        } catch (Exception e) {
            // Use a proper logging framework instead of System.out.println
            throw new RuntimeException("Exception occurred while creating schedule: " + e.getMessage(), e);
        }
        return schedule;
    }

    public List<CourtSchedule> getSchedulesByTimeSlot(long id) {
        TimeSlot slot = slotRepo.findById(id)
                .orElseThrow(() -> new BadRequestException("TimeSlot not found"));

        List<CourtSchedule> schedules = slot.getCourtSchedules();
        if (schedules == null || schedules.isEmpty()) {
            throw new BadRequestException("Schedule list is empty or null");
        }
        return schedules;
    }

    public List<CourtSchedule> getSchedulesByCourt(long id) {
        Court court = courtRepo.findById(id)
                .orElseThrow(() -> new BadRequestException("Court not found"));

        List<CourtSchedule> schedules = court.getCourtSchedules();
        if (schedules == null || schedules.isEmpty()) {
            throw new BadRequestException("Schedule list is empty or null");
        }
        return schedules;
    }

    public UpdateScheduleResponse updateScheduleAvailable(long id, UpdateScheduleRequest scheduleRequest) {
        try {
            Optional<CourtSchedule> optionalSchedule = scheduleRepo.findById(id);
            if (!optionalSchedule.isPresent()) {
                throw new ResourceNotFoundException("Schedule with id " + id + " not found");
            }
            CourtSchedule schedule = optionalSchedule.get();
            schedule.setAvailable(scheduleRequest.isAvailable());
            scheduleRepo.save(schedule); // Save the updated schedule
            return new UpdateScheduleResponse(true, "Schedule updated successfully");
        } catch (ResourceNotFoundException e) {
            return new UpdateScheduleResponse(false, e.getMessage());
        } catch (Exception e) {
            return new UpdateScheduleResponse(false, "An error occurred while updating the schedule");
        }
    }

}
