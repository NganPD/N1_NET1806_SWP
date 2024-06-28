package online.be;

import online.be.entity.Court;
import online.be.entity.Venue;
import online.be.enums.VenueStatus;
import online.be.exception.BadRequestException;
import online.be.exception.VenueException;
import online.be.model.Request.CreateVenueRequest;
import online.be.model.Request.UpdateVenueRequest;
import online.be.repository.CourtRepository;
import online.be.repository.VenueRepository;
import online.be.service.VenueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueServiceTests {

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private CourtRepository courtRepository;

    @InjectMocks
    private VenueService venueService;

    private CreateVenueRequest createVenueRequest;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        createVenueRequest = new CreateVenueRequest();
        createVenueRequest.setVenueName("Test Venue");
        createVenueRequest.setAddress("123 Test Street");
        createVenueRequest.setDescription("A test venue");
        createVenueRequest.setOperatingHours(LocalTime.of(8,0));
        createVenueRequest.setClosingHours(LocalTime.of(18,0));
        createVenueRequest.setVenueStatus(VenueStatus.ACTIVE);
        createVenueRequest.setServices("Test services");
        createVenueRequest.setManagerId(1L);
    }

    @Test
    void testCreateVenue_Success(){
        Venue mockVenue = new Venue();
        mockVenue.setVenueId(1L);
        mockVenue.setName(createVenueRequest.getVenueName());
        mockVenue.setAddress(createVenueRequest.getAddress());
        mockVenue.setDescription(createVenueRequest.getDescription());
        mockVenue.setOperatingHours(createVenueRequest.getOperatingHours());
        mockVenue.setClosingHours(createVenueRequest.getClosingHours());

        when(venueRepository.save(any(Venue.class))).thenReturn(mockVenue);

        //Act
        Venue createdVenue = venueService.createVenue(createVenueRequest);

        //Assert
        assertNotNull(createdVenue);
        assertEquals(createVenueRequest.getVenueName(), createdVenue.getName());
        assertEquals(createVenueRequest.getDescription(), createdVenue.getDescription());
        assertEquals(createVenueRequest.getAddress(), createdVenue.getAddress());
        assertEquals(createVenueRequest.getOperatingHours(), createdVenue.getOperatingHours());
        assertEquals(createVenueRequest.getClosingHours(), createdVenue.getClosingHours());

        verify(venueRepository, times(1)).save(any(Venue.class));
        System.out.println("Created Successfully");
    }

    @Test
    void testCreateVenue_DuplicateVenueName() {
        // Arrange
        Venue existingVenue = new Venue();
        existingVenue.setName("Test Venue");
        when(venueRepository.findByName("Test Venue")).thenReturn(existingVenue);

        // Act and Assert
        VenueException exception = assertThrows(VenueException.class, () ->
                venueService.createVenue(createVenueRequest));

        assertEquals("Duplicate venue", exception.getMessage());

        verify(venueRepository, times(1)).findByName("Test Venue");
        verify(venueRepository, never()).save(any(Venue.class));
        System.out.println("Duplicate Venue Name");
    }

    @Test
    void testCreateVenue_InvalidOperatingHours() {
        CreateVenueRequest request = new CreateVenueRequest();
        request.setVenueName("Valid Name");
        request.setAddress("123 Street");
        request.setDescription("Nice place");
        request.setOperatingHours(LocalTime.of(0, 0)); // Invalid hour
        request.setClosingHours(LocalTime.of(22, 0));
        request.setVenueStatus(VenueStatus.ACTIVE);

        DateTimeException exception = assertThrows(DateTimeException.class, () -> venueService.createVenue(request));
        assertEquals("Invalid value for HourOfDay (valid values 0 - 23):  ", exception.getMessage());
    }
    @Test
    void testCreateVenue_InvalidClosingHours() {
        // Arrange
        createVenueRequest.setClosingHours(LocalTime.of(24, 0)); // Invalid time

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            venueService.createVenue(createVenueRequest);
        });
        assertEquals("Closing hours must be between 00:00 and 23:59.", exception.getMessage());
    }

    @Test
    void testCreateVenue_ClosingBeforeOperating() {
        // Arrange
        createVenueRequest.setClosingHours(LocalTime.of(7, 0)); // Closing before operating

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            venueService.createVenue(createVenueRequest);
        });
        assertEquals("Closing hours cannot be before operating hours.", exception.getMessage());
    }

    @Test
    void testCreateVenue_ClosingSameAsOperating() {
        // Arrange
        createVenueRequest.setClosingHours(LocalTime.of(8, 0)); // Closing same as operating

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            venueService.createVenue(createVenueRequest);
        });
        assertEquals("Closing hours cannot be the same as operating hours.", exception.getMessage());
    }

//    @ParameterizedTest
//    @CsvFileSource(resources = "/updateVenueTestCases.csv", numLinesToSkip = 1)
//    @DisplayName("Parameterized test for updateVenue method")
//    void testUpdateVenue_Parameterized(long venueId, String venueName, String address, String description,
//                                       LocalTime operatingHours, LocalTime closingHours, VenueStatus venueStatus,
//                                       String assignedCourts, String expectedException, String expectedMessage) {
//        UpdateVenueRequest updateVenueRequest = new UpdateVenueRequest();
//        updateVenueRequest.setVenueName(venueName);
//        updateVenueRequest.setAddress(address);
//        updateVenueRequest.setDescription(description);
//        updateVenueRequest.setOperatingHours(operatingHours);
//        updateVenueRequest.setClosingHours(closingHours);
//        updateVenueRequest.setVenueStatus(venueStatus);
//
//        if (assignedCourts != null && !assignedCourts.isEmpty()) {
//            updateVenueRequest.setAssignedCourts(Arrays.asList(assignedCourts.split(";")).stream().map(Long::valueOf).toList());
//        }
//
//        Venue existingVenue = new Venue();
//        existingVenue.setVenueId(venueId);
//        existingVenue.setName("Existing Venue");
//        existingVenue.setAddress("123 Test Street");
//        existingVenue.setDescription("An existing test venue");
//        existingVenue.setOperatingHours("8:00");
//        existingVenue.setClosingHours("18:00");
//        existingVenue.setVenueStatus(VenueStatus.INACTIVE);
//
//        if (expectedException.equals("None")) {
//            when(venueRepository.findById(venueId)).thenReturn(Optional.of(existingVenue));
//            when(venueRepository.save(any(Venue.class))).thenReturn(existingVenue);
//
//            if (updateVenueRequest.getAssignedCourts() != null) {
//                List<Court> courts = Arrays.asList(new Court(), new Court());
//                when(courtRepository.findAllById(updateVenueRequest.getAssignedCourts())).thenReturn(courts);
//            }
//
//            Venue updatedVenue = venueService.updateVenue(venueId, updateVenueRequest);
//
//            assertNotNull(updatedVenue);
//            assertEquals(updateVenueRequest.getVenueName(), updatedVenue.getName());
//            assertEquals(updateVenueRequest.getAddress(), updatedVenue.getAddress());
//            assertEquals(updateVenueRequest.getDescription(), updatedVenue.getDescription());
//            assertEquals(updateVenueRequest.getOperatingHours(), updatedVenue.getOperatingHours());
//            assertEquals(updateVenueRequest.getClosingHours(), updatedVenue.getClosingHours());
//            assertEquals(updateVenueRequest.getVenueStatus(), updatedVenue.getVenueStatus());
//
//            if (updateVenueRequest.getAssignedCourts() != null) {
//                assertEquals(updateVenueRequest.getAssignedCourts().size(), updatedVenue.getAssignedCourts().size());
//            }
//
//            verify(venueRepository, times(1)).findById(venueId);
//            verify(venueRepository, times(1)).save(any(Venue.class));
//        } else {
//            when(venueRepository.findById(venueId)).thenReturn(Optional.of(existingVenue));
//
//            if (expectedException.equals("VenueException")) {
//                if (venueName.equals("Updated Venue")) {
//                    when(venueRepository.save(any(Venue.class))).thenThrow(new DataIntegrityViolationException("Error"));
//
//                    VenueException exception = assertThrows(VenueException.class, () ->
//                            venueService.updateVenue(venueId, updateVenueRequest));
//                    assertEquals(expectedMessage, exception.getMessage());
//                }
//            } else if (expectedException.equals("BadRequestException")) {
//                when(venueRepository.findById(venueId)).thenReturn(Optional.empty());
//
//                BadRequestException exception = assertThrows(BadRequestException.class, () ->
//                        venueService.updateVenue(venueId, updateVenueRequest));
//                assertEquals(expectedMessage, exception.getMessage());
//            }
//
//            verify(venueRepository, times(1)).findById(venueId);
//            verify(venueRepository, never()).save(any(Venue.class));
//        }
//    }
}