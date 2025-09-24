package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDaoJDBCImpl userDao;
    private final Connection connection;

    public UserServiceImpl() {
        Connection conn = null;
        UserDaoJDBCImpl dao = null;
        try {
            conn = Util.getConnection();
            dao = new UserDaoJDBCImpl(conn);
        } catch (SQLException e) {
            System.err.println("Ошибка при создании подключения или DAO: " + e.getMessage());
        }
        this.connection = conn;
        this.userDao = dao;
    }

    private void executeInTransaction(TransactionalOperation op, String successMessage) {
        if (connection == null) return;
        try {
            connection.setAutoCommit(false);
            op.run();
            connection.commit();
            if (successMessage != null) System.out.println(successMessage);
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.err.println("Откат транзакции: " + e.getMessage());
            } catch (SQLException ex) {
                System.err.println("Ошибка при откате: " + ex.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Ошибка при восстановлении авто-коммита: " + e.getMessage());
            }
        }
    }

    @FunctionalInterface
    private interface TransactionalOperation {
        void run() throws SQLException;
    }

    public void createUsersTable() {
        try {
            userDao.createUsersTable();
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    public void dropUsersTable() {
        try {
            userDao.dropUsersTable();
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении таблицы: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        try {
            return userDao.getAllUsers();
        } catch (SQLException e) {
            System.err.println("Ошибка при получении пользователей: " + e.getMessage());
            return List.of();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        executeInTransaction(
                () -> userDao.saveUser(name, lastName, age),
                "Пользователь сохранён: " + name + " " + lastName
        );
    }

    public void removeUserById(long id) {
        executeInTransaction(
                () -> userDao.removeUserById(id),
                "Пользователь с ID " + id + " удалён."
        );
    }

    public void cleanUsersTable() {
        executeInTransaction(
                userDao::cleanUsersTable,
                "Таблица пользователей очищена."
        );
    }

    public void close() {
        try {
            if (userDao != null) userDao.close();
        } catch (SQLException e) {
            System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
        }
    }
}
