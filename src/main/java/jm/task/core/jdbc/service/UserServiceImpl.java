package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoHibernateImpl();
    }

    @Override
    public void createUsersTable() {
        userDao.createUsersTable();
        System.out.println("Сервис: таблица пользователей создана.");
    }

    @Override
    public void dropUsersTable() {
        userDao.dropUsersTable();
        System.out.println("Сервис: таблица пользователей удалена.");
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userDao.getAllUsers();
        System.out.println("Сервис: получено " + users.size() + " пользователей.");
        return users;
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        userDao.saveUser(name, lastName, age);
        System.out.println("Сервис: пользователь " + name + " сохранён.");
    }

    @Override
    public void removeUserById(long id) {
        userDao.removeUserById(id);
        System.out.println("Сервис: пользователь с id " + id + " удалён.");
    }

    @Override
    public void cleanUsersTable() {
        userDao.cleanUsersTable();
        System.out.println("Сервис: таблица пользователей очищена.");
    }

    @Override
    public void close() {
        // Для Hibernate закрывать SessionFactory вручную, если нужно:
        // Util.getSessionFactory().close();
        System.out.println("Сервис закрыт.");
    }
}
