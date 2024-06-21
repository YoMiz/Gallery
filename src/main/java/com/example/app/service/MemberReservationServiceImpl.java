package com.example.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.User;
import com.example.app.domain.UserReservation;
import com.example.app.mapper.UserMapper;
import com.example.app.mapper.UserReservationMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MemberReservationServiceImpl implements MemberReservationService {
	private final UserMapper userMapper;
	private final UserReservationMapper reservationMapper;

	@Override
	public void makeReservation(User user, UserReservation reservation) throws Exception {
		// DB:userに対する加算ポイントの計算
		Integer additionalPoint = reservation.getPrice() / 100;
		Integer currentPoint = user.getPoint();
		currentPoint += additionalPoint;
		user.setPoint(currentPoint);
		userMapper.updatePoint(user);

		reservationMapper.createUserReservation(reservation);
	}

	@Override
	public void usePoints(User user, Integer drinkAmt) throws Exception {
		Integer pointUsed = drinkAmt * 500;
		Integer lastPoint = user.getPoint() - pointUsed;
		user.setPoint(lastPoint);
		userMapper.updatePoint(user);
	}

	@Override
	public boolean checkPoints(User user, Integer drinkAmt) throws Exception {
		Integer pointUsed = drinkAmt * 500;
		Integer lastPoint = user.getPoint() - pointUsed;
		if (lastPoint < 0) {
			return false;
		} else {
			return true;
		}
	}
}
