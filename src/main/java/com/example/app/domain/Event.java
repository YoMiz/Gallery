package com.example.app.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
	private Integer id;
	private String eventName;
	private Integer pageStyle;
	@DateTimeFormat(pattern="yyyy/MM/dd")
	private Date eventStart;
	@DateTimeFormat(pattern="yyyy/MM/dd")
	private Date eventEnd;
}
