package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.UserReservation;

@Mapper
public interface UserReservationMapper {
	void createUserReservation(UserReservation reservation) throws Exception;
	UserReservation getReservationByUserId(Integer userId) throws Exception;
	List<UserReservation> selectAll() throws Exception;
	void updateReservation(UserReservation userReservation) throws Exception;
	void deleteReservation(Integer reservationId) throws Exception;
}
