package com.example.app.service;

import java.util.List;

import com.example.app.domain.User;

public interface AdminService {
	boolean isCorrectIdAndPassword(String loginId, String password) throws Exception;
	List<User> selectAll() throws Exception;
	User selectById(Integer id) throws Exception;
	void insert(User member) throws Exception;
	void updatePoint(User member) throws Exception;
	void delete(Integer id) throws Exception;
	User selectByLoginId(String loginId);
	void updateUser(User member)throws Exception;
}
