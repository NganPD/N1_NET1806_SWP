//package online.be;
//
//import online.be.entity.TimeSlot;
//import online.be.entity.Venue;
//import online.be.exception.BadRequestException;
//import online.be.model.Request.RegisterRequest;
//import online.be.model.Request.TimeSlotRequest;
//import online.be.repository.AuthenticationRepository;
//import online.be.repository.TimeSlotRepository;
//import online.be.repository.VenueRepository;
//import online.be.service.AccountService;
//import online.be.service.EmailService;
//import online.be.service.TimeSlotService;
//import org.junit.Assert;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.sql.Time;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TimeSlotSerivceTests {
//
//    @Mock
//    private TimeSlotRepository timeSlotRepository;
//
//    @Mock
//    private VenueRepository venueRepository;
//
//    @InjectMocks
//    private TimeSlotService timeSlotService;
//
//    private TimeSlotRequest timeSlotRequest;
//    private Venue venue;
//
//    @BeforeEach
//    public void setUp(){
//        MockitoAnnotations.openMocks(this);
//    }
//
//    //successful
//    @Test
//    void testCreateTimeSlot_Success(){
//        TimeSlotRequest request = new TimeSlotRequest();
//        request.setStartTime(LocalTime.of(10,0));
//        request.setEndTime(LocalTime.of(11,0));
//        request.setPrice(50.0);
//        request.setStatus(true);
//        request.setVenueId(1L);
//
//        Venue venue = new Venue();
//        venue.setVenueId(1L);
//        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));
//
//        TimeSlot timeSlot = new TimeSlot();
//        when(timeSlotRepository.save(any(TimeSlot.class))).thenReturn(timeSlot);
//
//        TimeSlot result = timeSlotService.createTimeSlot(request);
//
//        Assert.assertNotNull(result);
//        verify(timeSlotRepository, times(1)).save(any(TimeSlot.class));
//    }
//
//    //invalid time range
//    @Test
//    void testCreateTimeSlot_InvalidTimeRange(){
//        TimeSlotRequest request = new TimeSlotRequest();
//        request.setStartTime(LocalTime.of(12,0));
//        request.setEndTime(LocalTime.of(11,0));
//        request.setVenueId(1L);
//
//        Exception exception = assertThrows(BadRequestException.class,
//                ()->{
//            timeSlotService.createTimeSlot(request);
//                });
//        assertEquals("Start time must be before end time", exception.getMessage());
//    }
//
//    //missing start time or end time
//    @Test
//    void testCreateTimeSlot_MissingStartTime(){
//        TimeSlotRequest request = new TimeSlotRequest();
//        request.setEndTime(LocalTime.of(11,0));
//
//        Exception exception = assertThrows(BadRequestException.class,
//                ()->{
//            timeSlotService.createTimeSlot(request);
//                });
//        assertEquals("Start time and end time are required", exception.getMessage());
//    }
//    //nonexistvenue
//    @Test
//    void testCreateTimeSlot_VenueNotFound(){
//        TimeSlotRequest request = new TimeSlotRequest();
//        request.setStartTime(LocalTime.of(10,0));
//        request.setEndTime(LocalTime.of(11,0));
//        request.setVenueId(1L);
//
//        //Mock the venue repository to return an empty Optional
//        when(venueRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(BadRequestException.class,
//                ()-> timeSlotService.createTimeSlot(request));
//        assertEquals("The venue cannot be found by ID1", exception.getMessage());
//    }
//    //overlapping time slot
//    @Test
//    void testCreateTimeSlot_OverlappingTimeSlots(){
//        TimeSlotRequest request = new TimeSlotRequest();
//        request.setStartTime(LocalTime.of(10,0));
//        request.setEndTime(LocalTime.of(11,0));
//        request.setVenueId(1L);
//
//        //Mock the venue repository to returen a valid venue
//        Venue venue = new Venue();
//        venue.setVenueId(1L);
//
//        //Mock the time slot repository to return a list with an overlapping time slot
//        TimeSlot existingSlot = new TimeSlot();
//        existingSlot.setStartTime(LocalTime.of(10,30));
//        existingSlot.setEndTime(LocalTime.of(11,30));
//
//        //Mock the repository behavior
//        when(timeSlotRepository.findByVenueVenueId(1L)).thenReturn(List.of(existingSlot));
//
//        Exception exception = assertThrows(BadRequestException.class,
//                ()-> timeSlotService.createTimeSlot(request));
//        assertEquals("The time slot overlaps with an existing time slot", exception.getMessage());
//    }
//}
