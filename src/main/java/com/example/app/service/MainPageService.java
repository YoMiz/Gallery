package com.example.app.service;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.app.domain.ArtInfo;
import com.example.app.domain.Event;
import com.example.app.domain.EventData;
import com.example.app.domain.ModelData;


public interface MainPageService {
		//Event
		List<Event> getEventList() throws Exception;
		Event getEventById(Integer id) throws Exception;
		List<Event> getEventListByPage(int page, int numPerPage) throws Exception;
		int getTotalPages(int numPerPage) throws Exception;
		List<Event> selectLimited(@Param("offset") int offset, @Param("limit") int limit) throws Exception;
		List<Event> selectAll() throws Exception;
		Event selectById(Integer id) throws Exception;
		Event findEventByDate(Date selectedDate) throws Exception;
		
		//Page表示用
		Integer getLastEventId() throws Exception;
		List<ArtInfo>getArtInfoByEventId(Integer eventId) throws Exception;
		
	    ModelData prepareModelData(Integer page) throws Exception;
	    EventData prepareEventData(Integer eventId) throws Exception;
}
