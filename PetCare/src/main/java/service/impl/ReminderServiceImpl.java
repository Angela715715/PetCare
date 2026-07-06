package service.impl;

import java.util.List;

import dao.ReminderDao;
import dao.impl.ReminderDaoImpl;
import entity.Reminder;
import service.ReminderService;

public class ReminderServiceImpl implements ReminderService {
	private ReminderDao dao = new ReminderDaoImpl();

	@Override
	public void addRecord(Reminder record) {
		dao.add(record);
	}

	@Override
	public List<Reminder> findByPetId(int petId) {
		return dao.selectByPetId(petId);
	}

	@Override
	public void deleteRecord(int id) {
		dao.delete(id);
	}

	@Override
	public void finishReminder(int id) {
		dao.updateStatus(id, "已完成");
	}
}
