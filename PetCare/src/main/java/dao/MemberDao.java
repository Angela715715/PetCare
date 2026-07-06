package dao;

import entity.Member;

public interface MemberDao {
	// 用帳密查會員
	Member selectByUsernameAndPassword(String username, String password);

	// 用帳號查會員，註冊時檢查帳號是否重複
	Member selectByUsername(String username);

	// 新增會員
	void add(Member member);
}
