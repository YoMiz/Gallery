package com.example.app.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelData {
	 private List<Event> eventList;
	    private List<Event> events;
	    private int page;
	    private int totalPages;
	    private User admin;
	    private UserReservation reservation;
	    private String errorMessage;
}
