package com.example.app.domain;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
	private Integer id;
	@NotBlank
	@Size(max=30)
	private String loginId;

	@Size(max=20)
	private String userName;
	
	@NotBlank
	@Size(max=80)
	private String password;
	
	private Integer point;
	private Date created;
	private Date deleted;
}
