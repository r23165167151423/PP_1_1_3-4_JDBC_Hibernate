package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection = Util.getConnection();


    private static final String CREATE_TABLE_SQL = """
            
                CREATE TABLE IF NOT EXISTS users (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(50),
                lastName VARCHAR(50),
                age TINYINT
                        )
            """;

    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS users";
    private static final String INSERT_USER_SQL = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM users";
    private static final String CLEAN_TABLE_SQL = "TRUNCATE TABLE users";


    @Override
    public void createUsersTable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE_SQL);
            System.out.println("Таблица пользователей создана.");
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(DROP_TABLE_SQL);
            System.out.println("Таблица пользователей удалена.");
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении таблицы: " + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        executeInTransaction(() -> {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL)) {
                ps.setString(1, name);
                ps.setString(2, lastName);
                ps.setByte(3, age);
                ps.executeUpdate();
                System.out.println("Пользователь " + name + " добавлен.");
            } catch (SQLException e) {
                System.err.println("Ошибка при добавлении пользователя: " + e.getMessage());
            }
        });
    }

    @Override
    public void removeUserById(long id) {
        executeInTransaction(() -> {
            try (PreparedStatement ps = connection.prepareStatement(DELETE_USER_SQL)) {
                ps.setLong(1, id);
                ps.executeUpdate();
                System.out.println("Пользователь с id " + id + " удален.");
            } catch (SQLException e) {
                System.err.println("Ошибка при удалении пользователя: " + e.getMessage());
            }
        });
    }


    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                User user = new User(
                        rs.getString("name"),
                        rs.getString("lastName"),
                        rs.getByte("age")
                );
                user.setId(rs.getLong("id"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении пользователей: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CLEAN_TABLE_SQL);
            System.out.println("Таблица очищена.");
        } catch (SQLException e) {
            System.err.println("Ошибка при очистке таблицы: " + e.getMessage());
        }
    }

    private void executeInTransaction(TransactionalOperation op) {
        if (connection == null) {
            System.err.println("Соединение не установлено.");
            return;
        }
        try {
            connection.setAutoCommit(false);
            op.run();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.err.println("Ошибка в транзакции, выполнен rollback: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                System.err.println("Не удалось сделать rollback: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Не удалось вернуть авто-коммит: " + e.getMessage());
            }
        }
    }

    @FunctionalInterface
    private interface TransactionalOperation {
        void run() throws SQLException;
    }
}

