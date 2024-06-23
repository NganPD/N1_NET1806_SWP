package online.be;

import online.be.entity.Venue;
import online.be.model.Request.CreateVenueRequest;
import online.be.repository.VenueRepository;
import online.be.service.VenueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueServiceTests {

	@Mock
	private VenueRepository venueRepository;

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
		createVenueRequest.setOperatingHours("8:00");
		createVenueRequest.setClosingHours("18:00");
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
	void testCreateVenue_DataIntegrityViolation() {
		// Arrange
		Venue existingVenue = new Venue();
		existingVenue.setName("Test Venue");
		when(venueRepository.findByName("Test Venue")).thenReturn(existingVenue);

		// Act and Assert
		DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () ->
				venueService.createVenue(createVenueRequest));

		assertEquals("Duplicate venue", exception.getMessage());

		verify(venueRepository, times(1)).findByName("Test Venue");
		verify(venueRepository, never()).save(any(Venue.class));
		System.out.println("Duplicate Venue Name");
	}
}
