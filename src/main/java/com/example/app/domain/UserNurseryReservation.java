package com.example.app.domain;

import java.sql.Date;

import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserNurseryReservation {
	Integer id;
	String userName;
	Integer reservationNumber;
	@Future
	Date reservationDate;
	Integer price;
}
