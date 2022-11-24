package com.flight_reservation_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.flight_reservation_app.dto.ReservationRequest;
import com.flight_reservation_app.entities.Reservation;
import com.flight_reservation_app.service.ReservationService;

@Controller
public class ReservationController {
	@Autowired
	private ReservationService reservationService;
	
	@RequestMapping("/completeReservation")
	public String completeReservation(ReservationRequest reservation,Model model) {
		Reservation reservationId = reservationService.bookFlight(reservation);
		model.addAttribute("reservationId", reservationId.getId());
		return "confirmReservation";
	}
}
