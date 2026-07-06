package service;

import java.util.List;

import entity.Reminder;

public interface ReminderService {
	void addRecord(Reminder record);
	List<Reminder> findByPetId(int petId);
	void deleteRecord(int id);

	void finishReminder(int id);
}
