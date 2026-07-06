package dao;

import java.util.List;

import entity.Reminder;

public interface ReminderDao {
	void add(Reminder record);
	List<Reminder> selectByPetId(int petId);
	void delete(int id);

	void updateStatus(int id, String status);
}
