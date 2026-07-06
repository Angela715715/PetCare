package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.MemberDao;
import entity.Member;
import util.DbConnection;

public class MemberDaoImpl implements MemberDao {

	private Connection conn = DbConnection.getDb();

	@Override
	public Member selectByUsernameAndPassword(String username, String password) {
		Member member = null;
		String sql = "SELECT * FROM member WHERE username = ? AND password = ?";

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				member = buildMember(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return member;
	}

	@Override
	public Member selectByUsername(String username) {
		Member member = null;
		String sql = "SELECT * FROM member WHERE username = ?";

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				member = buildMember(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return member;
	}

	@Override
	public void add(Member member) {
		String sql = "INSERT INTO member(username, password, name, role) VALUES(?, ?, ?, ?)";

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, member.getUsername());
			ps.setString(2, member.getPassword());
			ps.setString(3, member.getName());
			ps.setString(4, member.getRole());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Member buildMember(ResultSet rs) throws SQLException {
		Member member = new Member();
		member.setId(rs.getInt("id"));
		member.setUsername(rs.getString("username"));
		member.setPassword(rs.getString("password"));
		member.setName(rs.getString("name"));
		member.setRole(rs.getString("role"));
		return member;
	}
}
