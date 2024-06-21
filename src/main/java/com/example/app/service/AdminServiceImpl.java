package com.example.app.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.User;
import com.example.app.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserMapper mapper;

    @Override
    public boolean isCorrectIdAndPassword(String loginId, String loginPass) throws Exception {
        User user = mapper.selectByLoginId(loginId);
        // ログイン ID が正しいかチェック
        // ⇒ ログイン ID が正しくなければ、管理者データは取得されない
        if (user == null) {
            return false;
        }
        // パスワードが正しいかチェック
        if (!BCrypt.checkpw(loginPass, user.getPassword())) {
            return false;
        }
        return true;
    }


	@Override
	public List<User> selectAll() throws Exception {
		return mapper.selectAll();
	}

	@Override
	public User selectById(Integer id) throws Exception {
		return mapper.selectById(id);
	}

	@Override
	public void insert(User user) throws Exception {
		mapper.insert(user);
	}

	@Override
	public void updatePoint(User user) throws Exception {
		mapper.updatePoint(user);
	}

	@Override
	public void delete(Integer id) throws Exception {
		mapper.delete(id);
	}

	@Override
	public User selectByLoginId(String loginId){
		return mapper.selectByLoginId(loginId);
	}


	@Override
	public void updateUser(User user) throws Exception {
		mapper.updateUser(user);
		
	}
}
