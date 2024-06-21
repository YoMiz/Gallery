package com.example.app.mapper;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Event;

@Mapper
public interface EventMapper {
	Long count() throws Exception;

	List<Event> selectLimited(@Param("offset") int offset, @Param("limit") int limit) throws Exception;
	List<Event> selectAll() throws Exception;
	Event selectById(Integer id) throws Exception;
	Integer selectMaxId() throws Exception;
	Integer findEventIdByDate(Date selectedDate) throws Exception;

}