package com.flight_reservation_app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight_reservation_app.dto.ReservationRequest;
import com.flight_reservation_app.entities.Flight;
import com.flight_reservation_app.entities.Passenger;
import com.flight_reservation_app.entities.Reservation;
import com.flight_reservation_app.repository.FlightRepository;
import com.flight_reservation_app.repository.PassengerRepository;
import com.flight_reservation_app.repository.ReservationRepository;
import com.flight_reservation_app.utilities.EmailGenerator;
import com.flight_reservation_app.utilities.PdfGenerator;
@Service
public class ReservationServiceImpl implements ReservationService {
	@Autowired
	private PassengerRepository passengerRepo;
	
	@Autowired
	private FlightRepository flightRepo;
	
	@Autowired
	private ReservationRepository reservationRepo;
	
	@Autowired
	private PdfGenerator pdfGenerator;
	
	@Autowired
	private EmailGenerator emailGenerator;

	@Override
	public Reservation bookFlight(ReservationRequest request) {
	
		Passenger passenger = new Passenger();
		
		passenger.setFirstName(request.getFirstName());
		passenger.setLastName(request.getLastName());
		passenger.setMiddleName(request.getMiddleName());
		passenger.setEmail(request.getEmail());
		passenger.setPhone(request.getPhone());
		passengerRepo.save(passenger);
		
		long flightId = request.getFlightId();
		Optional<Flight> findById = flightRepo.findById(flightId);
		Flight flight = findById.get();
		
		Reservation res = new Reservation();
		res.setFlight(flight);
		res.setPassenger(passenger);
		res.setCheckedIn(false);
		res.setNumberOfBags(0);
		reservationRepo.save(res);
		
		String filepath="C:\\My-SpringBoot\\flight_reservation_app\\target\\tickets"+res.getId()+".pdf";
		pdfGenerator.generatePDF(filepath,request.getFirstName(),request.getEmail(), request.getPhone(), flight.getOperatingAirlines(), flight.getDateOfDeparture(), flight.getDepartureCity(), flight.getArrivalCity());
		emailGenerator.sendItinerary(passenger.getEmail(), filepath);
		
		return res;
	}

}
