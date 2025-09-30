package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDaoJDBCImpl userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoJDBCImpl();
    }

    @Override
    public void createUsersTable() {
        userDao.createUsersTable();
        System.out.println("Сервис: таблица пользователей создана.");
    }

    @Override
    public void dropUsersTable() {
        userDao.dropUsersTable();
        System.out.println("Таблица пользователей удалена.");
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userDao.getAllUsers();
        System.out.println("Получено " + users.size() + " пользователей.");
        return users;
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        userDao.saveUser(name, lastName, age);
        System.out.println("Пользователь " + name + " сохранён.");
    }

    @Override
    public void removeUserById(long id) {
        userDao.removeUserById(id);
        System.out.println("Пользователь с id " + id + " удалён.");
    }

    @Override
    public void cleanUsersTable() {
        userDao.cleanUsersTable();
        System.out.println("Таблица пользователей очищена.");
    }

    @Override
    public void close() {
        Util.closeConnection();
    }
}
