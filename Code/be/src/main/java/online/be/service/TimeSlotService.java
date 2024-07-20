package online.be.service;

import online.be.entity.Court;
import online.be.entity.TimeSlot;
import online.be.entity.Venue;
import online.be.exception.BadRequestException;
import online.be.exception.ResourceNotFoundException;
import online.be.model.Request.TimeSlotRequest;
import online.be.model.Response.TimeSlotResponse;
import online.be.model.SlotIdCountDTO;
import online.be.repository.CourtRepository;
import online.be.repository.TimeSlotRepository;
import online.be.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeSlotService {

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    CourtRepository courtRepo;


    // Lưu một TimeSlot mới hoặc cập nhật một TimeSlot đã tồn tại
    public TimeSlot createTimeSlot(TimeSlotRequest timeSlotRequest) {
        LocalTime startTime = LocalTime.parse(timeSlotRequest.getStartTime());
        LocalTime endTime = LocalTime.parse(timeSlotRequest.getEndTime());
        //check the overlapping time slots
        List<TimeSlot> existingSlots = timeSlotRepository.findByVenueId(timeSlotRequest.getVenueId());
        for (TimeSlot slot : existingSlots) {
            if (startTime.isBefore(slot.getStartTime())
                    && endTime.isAfter(slot.getStartTime())) {
                throw new BadRequestException("The time slot overlaps with an existing time slot");
            }
        }
        //calculate duration
        long duration = Duration.between(startTime, endTime).toHours();
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(LocalTime.parse(timeSlotRequest.getStartTime()));
        timeSlot.setEndTime(LocalTime.parse(timeSlotRequest.getEndTime()));
        timeSlot.setDuration(duration);

        //Set venue
        //check whether the venue exist or not
        Venue venue = venueRepository.findById(timeSlotRequest.getVenueId())
                .orElseThrow(() -> new BadRequestException("The venue cannot be found by ID" + timeSlotRequest.getVenueId()));
        timeSlot.setVenue(venue);
        return timeSlotRepository.save(timeSlot);
    }    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    // Cập nhật thông tin TimeSlot
    public TimeSlot updateTimeSlot(long timeSlotId, TimeSlotRequest timeSlotRequest) {
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new ResourceNotFoundException("Time slot cannot be found by ID: " + timeSlotId));

//        timeSlot.setDuration(timeSlotRequest.getDuration());
        timeSlot.setStartTime(LocalTime.parse(timeSlotRequest.getStartTime()));
        timeSlot.setEndTime(LocalTime.parse(timeSlotRequest.getEndTime()));
        long duration = Duration.between(timeSlot.getStartTime(), timeSlot.getEndTime()).toMinutes();
        timeSlot.setDuration(duration);
        venueRepository.findById(timeSlotRequest.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("The venue cannot be found by ID: " + timeSlotRequest.getVenueId()));
        return timeSlotRepository.save(timeSlot);
    }    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    // Tìm các TimeSlot theo thời gian bắt đầu nằm trong một khoảng thời gian
    public List<TimeSlot> findByStartTimeBetween(String start, String end) {
        return timeSlotRepository.findByStartTimeBetween(LocalTime.parse(start, DateTimeFormatter.ofPattern("HH:mm")), LocalTime.parse(end, DateTimeFormatter.ofPattern("HH:mm")));
    }//Tự tạo hiển thị không có slot

    // Read TimeSlot by ID
    public TimeSlot getTimeSlotById(long timeSlotId) {
        return timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new ResourceNotFoundException("TimeSlot cannot found by ID: " + timeSlotId));
    }

    public List<TimeSlot> getAllTimeSlots() {
        try {
            List<TimeSlot> timeSlots = timeSlotRepository.findAll();
            if (timeSlots.isEmpty()) {
                throw new ResourceNotFoundException("No time slots found");
            }
            return timeSlots;
        } catch (Exception e) {
            // Log the exception if necessary
            throw new RuntimeException("An error occurred while fetching time slots: " + e.getMessage(), e);
        }
    }

    public TimeSlotResponse mapperSlot(TimeSlot slot) {
        TimeSlotResponse slotResponse = new TimeSlotResponse();
        slotResponse.setId(slot.getId());
        slotResponse.setDuration(slot.getDuration());
        slotResponse.setStartTime(String.valueOf(slot.getStartTime()));
        slotResponse.setEndTime(String.valueOf(slot.getEndTime()));
        slotResponse.setAvailable(true);
        return slotResponse;
    }

    public List<TimeSlotResponse> getAvailableSlots(Long courtId, String date, long venueId) {
        List<TimeSlot> slots = getAllSlotByVenue(venueId);
        List<TimeSlotResponse> slotResponses = new ArrayList<>();
        LocalDate checkInDate = null;

        if (date != null && !date.isEmpty()) {
            checkInDate = LocalDate.parse(date);
        }

        if (courtId == null) {
            for (TimeSlot slot : slots) {
                TimeSlotResponse slotResponse = mapperSlot(slot);
                if (checkInDate != null) {
                    if (checkInDate.equals(LocalDate.now()) || checkInDate.isBefore(LocalDate.now())) {
                        slotResponse.setAvailable(isSlotExpired(checkInDate, slot.getStartTime()));
                    }
                    slotResponses.add(slotResponse);
                } else {
                    slotResponses.add(slotResponse);
                }
            }
        } else {
            List<SlotIdCountDTO> list = getSlotIdCounts(courtId, checkInDate);

            for (TimeSlot slot : slots) {
                TimeSlotResponse slotResponse = mapperSlot(slot);

                if (checkInDate != null && (checkInDate.equals(LocalDate.now()) || checkInDate.isBefore(LocalDate.now()))) {
                    slotResponse.setAvailable(isSlotExpired(checkInDate, slot.getStartTime()));
                }

                for (SlotIdCountDTO slotIdCountDTO : list) {
                    if (slot.getId() == slotIdCountDTO.getSlotId() && slotIdCountDTO.getCount() == 1) {
                        slotResponse.setAvailable(false);
                        break;
                    }
                }
                slotResponses.add(slotResponse);
            }
        }
        return slotResponses;
    }

    public List<TimeSlotResponse> getAvailableSlotByDayOfWeek(String date, Integer durationMonths, List<String> dayOfWeeks, Long courtId) {
        List<TimeSlotResponse> allSlots = new ArrayList<>();

        // Check if all parameters are null
        if (date == null || durationMonths == null || dayOfWeeks == null || courtId == null) {
            // Get all slots if no parameters are provided
            List<TimeSlot> slots = getAllSlots();
            for (TimeSlot slot : slots) {
                TimeSlotResponse slotResponse = mapperSlot(slot);
                slotResponse.setAvailable(true); // Assume all slots are available
                allSlots.add(slotResponse);
            }
            return allSlots;
        }

        // Validate and parse date
        LocalDate applicationDate;
        try {
            applicationDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format: " + date);
        }
        // Calculate all matching dates for the given dayOfWeeks and durationMonths
        List<LocalDate> allMatchingDates = new ArrayList<>();
        for (int month = 0; month < durationMonths; month++) {
            for (String dayOfWeekStr : dayOfWeeks) {
                DayOfWeek dayOfWeek;
                try {
                    dayOfWeek = DayOfWeek.valueOf(dayOfWeekStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new BadRequestException("Invalid day of week: " + dayOfWeekStr);
                }
                LocalDate firstMatchingDate = applicationDate.with(TemporalAdjusters.nextOrSame(dayOfWeek));
                for (LocalDate dateMatch = firstMatchingDate; !dateMatch.isAfter(applicationDate.plusMonths(1).minusDays(1)); dateMatch = dateMatch.plusWeeks(1)) {
                    allMatchingDates.add(dateMatch);
                }
            }
            applicationDate = applicationDate.plusMonths(1);
        }

        // Get all time slots for the court
        Court court = courtRepo.findById(courtId).orElseThrow(() ->
                new BadRequestException("Court not found with id: " + courtId));
        List<TimeSlot> slots = getAllSlotByVenue(court.getVenue().getId());

        // Check availability for each time slot
        for (TimeSlot slot : slots) {
            TimeSlotResponse slotResponse = mapperSlot(slot);
            boolean isAvailable = true;

            for (LocalDate dateMatch : allMatchingDates) {
                if (dateMatch.isBefore(LocalDate.now())) {
                    continue; // Skip past dates
                }

                List<SlotIdCountDTO> slotIdCounts = getSlotIdCounts(courtId, dateMatch);
                boolean slotOccupied = slotIdCounts.stream()
                        .anyMatch(slotIdCount -> slot.getId() == slotIdCount.getSlotId() && slotIdCount.getCount() == 1);

                if (slotOccupied) {
                    isAvailable = false;
                    break;
                }
            }

            // Set availability for the slot
            slotResponse.setAvailable(isAvailable);
            allSlots.add(slotResponse);
        }

        return allSlots;
    }


    // Method to get all slots of a venue
    private List<TimeSlot> getAllSlots() {
        return timeSlotRepository.findAll(); // Assumes timeSlotRepository is the repository for TimeSlot
    }


    // New method to get all slots by venue
    private List<TimeSlot> getAllSlotByVenue(long venueId) {
        return timeSlotRepository.findByVenueId(venueId);
    }

    // Helper method to retrieve SlotIdCountDTOs
    private List<SlotIdCountDTO> getSlotIdCounts(long courtId, LocalDate date) {
        List<Object[]> results = timeSlotRepository.countTimeSlotsByCourtIdAndDate(courtId, date);
        List<SlotIdCountDTO> slotIdCounts = new ArrayList<>();
        for (Object[] result : results) {
            slotIdCounts.add(new SlotIdCountDTO((Long) result[0], (Long) result[1]));
        }
        return slotIdCounts;
    }

    // Check if the slot date and time has already passed
    private boolean isSlotExpired(LocalDate slotDate, LocalTime slotStartTime) {
        try {
            LocalDateTime slotDateTime = LocalDateTime.of(slotDate, slotStartTime);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiryTime = slotDateTime.minusMinutes(30);
            return !now.isAfter(expiryTime);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
