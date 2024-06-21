package com.example.app.service;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.ArtInfo;
import com.example.app.domain.Event;
import com.example.app.domain.EventData;
import com.example.app.domain.ModelData;
import com.example.app.domain.User;
import com.example.app.domain.UserReservation;
import com.example.app.mapper.ArtInfoMapper;
import com.example.app.mapper.EventMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService {
	private final EventMapper eventMapper;
	private final ArtInfoMapper artInfoMapper;
	private Integer NUM_PER_PAGE = 5;

	@Override
	public List<Event> getEventListByPage(int page, int numPerPage) throws Exception {
		int offset = numPerPage * (page - 1);
		return eventMapper.selectLimited(offset, numPerPage);
	}

	@Override
	public int getTotalPages(int numPerPage) throws Exception {
		double totalNum = (double) eventMapper.count();
		return (int) Math.ceil(totalNum / numPerPage);
	}

	@Override
	public List<Event> getEventList() throws Exception {
		return eventMapper.selectAll();
	}

	@Override
	public Event getEventById(Integer id) throws Exception {
		return eventMapper.selectById(id);
	}

	@Override
	public List<Event> selectLimited(int offset, int limit) throws Exception {
		return eventMapper.selectLimited(offset, limit);
	}

	@Override
	public List<Event> selectAll() throws Exception {
		return eventMapper.selectAll();
	}

	@Override
	public Event selectById(Integer id) throws Exception {
		return eventMapper.selectById(id);
	}

	@Override
	public Integer getLastEventId() throws Exception {
		return eventMapper.selectMaxId();
	}

	@Override
	public List<ArtInfo> getArtInfoByEventId(Integer eventId) throws Exception {
		return artInfoMapper.selectByEventId(eventId);
	}

	@Override
	public Event findEventByDate(Date selectedDate) throws Exception {
		int id = eventMapper.findEventIdByDate(selectedDate);
		return eventMapper.selectById(id);
	}

	@Override
	public ModelData prepareModelData(Integer page) throws Exception {
		ModelData modelData = new ModelData();
		int offset = NUM_PER_PAGE * (page - 1);
		modelData.setEventList(eventMapper.selectLimited(offset, NUM_PER_PAGE));
		modelData.setEvents(getEventListByPage(page, NUM_PER_PAGE));
		modelData.setPage(page);
		modelData.setTotalPages(getTotalPages(NUM_PER_PAGE));
		modelData.setAdmin(new User());
		modelData.setReservation(new UserReservation());
		// errorMessageは必要に応じて設定します
		return modelData;
	}

	@Override
	public EventData prepareEventData(Integer eventId) throws Exception {
		EventData eventData = new EventData();
		List<ArtInfo> artInfoList = getArtInfoByEventId(eventId);
		Event event = getEventById(eventId);
		eventData.setEventId(eventId);
		eventData.setCurrentEvent(event);
		eventData.setArtInfoList(artInfoList);
		if (!artInfoList.isEmpty()) {
			eventData.setTitleImg(artInfoList.get(0).getFileName());
		}
		return eventData;
	}

}
