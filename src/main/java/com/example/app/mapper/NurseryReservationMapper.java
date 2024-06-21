package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.NurseryReservation;
import com.example.app.domain.UserNurseryReservation;

@Mapper
public interface NurseryReservationMapper {
	void makeNurseryReservation(NurseryReservation nurseryReservation) throws Exception;
	List<UserNurseryReservation> showNurseryReservationList();
	void updateNurseryReservation(NurseryReservation nurseryReservation) throws Exception;
}
