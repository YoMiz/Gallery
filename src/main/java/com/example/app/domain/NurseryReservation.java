package com.example.app.domain;

import java.sql.Date;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NurseryReservation {
	@NotNull
	private Integer id;
	@NotNull
	private Integer userId;
	@NotNull
	private String userName;
	@NotNull
	private Date reservationDate;
	@NotNull
	private Integer reservationNumber;
	@NotNull
	private Integer price;
	
	
}
