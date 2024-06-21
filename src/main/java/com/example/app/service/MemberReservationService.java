package com.example.app.service;

import com.example.app.domain.User;
import com.example.app.domain.UserReservation;

public interface MemberReservationService {
	void makeReservation(User user, UserReservation reservation) throws Exception;
	void usePoints(User user, Integer drinkAmt) throws Exception;
	boolean checkPoints(User user, Integer drinkAmt) throws Exception;
}
