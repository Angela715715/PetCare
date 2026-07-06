package service;

import entity.Member;

public interface MemberService {
	// 登入
	Member login(String username, String password);

	// 註冊一般使用者帳號
	boolean register(Member member);

	// 檢查帳號是否已存在
	boolean usernameExists(String username);
}
