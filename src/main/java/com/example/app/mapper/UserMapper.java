package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.User;

@Mapper
public interface UserMapper {
	
	List<User> selectAll() throws Exception;
	User selectById(Integer id) throws Exception;
	void insert(User user) throws Exception;
	void updatePoint(User user) throws Exception;
	void delete(Integer id) throws Exception;
	User selectByLoginId(String loginId);
	void updateUser(User user) throws Exception;
	
	
}
