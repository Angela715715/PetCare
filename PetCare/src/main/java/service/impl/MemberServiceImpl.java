package service.impl;

import dao.MemberDao;
import dao.impl.MemberDaoImpl;
import entity.Member;
import service.MemberService;

public class MemberServiceImpl implements MemberService {

	private MemberDao memberDao = new MemberDaoImpl();

	@Override
	public Member login(String username, String password) {
		return memberDao.selectByUsernameAndPassword(username, password);
	}

	@Override
	public boolean register(Member member) {
		if (member == null || member.getUsername() == null) {
			return false;
		}

		if (usernameExists(member.getUsername())) {
			return false;
		}

		memberDao.add(member);
		return true;
	}

	@Override
	public boolean usernameExists(String username) {
		return memberDao.selectByUsername(username) != null;
	}
}
