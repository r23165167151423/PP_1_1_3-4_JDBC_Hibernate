package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Util.getConnection();
        UserDao userDao = new UserDaoJDBCImpl();

        userDao.createUsersTable();

        userDao.saveUser("Petr", "Ivanov", (byte) 28);
        userDao.saveUser("Olga", "Smirnova", (byte) 35);
        userDao.saveUser("Anna", "Sidorova", (byte) 30);
        userDao.saveUser("Ivan", "Petrov", (byte) 25);

        for (User user : userDao.getAllUsers()) {
            System.out.println(user);
        }


        userDao.cleanUsersTable();

        userDao.dropUsersTable();
    }
}
