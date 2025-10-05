package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {
    public static void main(String[] args) {
        try (UserService userService = new UserServiceImpl()) {
            userService.createUsersTable();

            userService.saveUser("Petr", "Ivanov", (byte) 28);
            userService.saveUser("Olga", "Smirnova", (byte) 35);
            userService.saveUser("Anna", "Sidorova", (byte) 30);
            userService.saveUser("Ivan", "Petrov", (byte) 25);

            for (User user : userService.getAllUsers()) {
                System.out.println(user);
            }

            userService.cleanUsersTable();
            userService.dropUsersTable();
        } catch (Exception e) {
            System.err.println("Произошла ошибка при работе с сервисом: " + e.getMessage());
        } finally {
            Util.closeSessionFactory();
        }

    }
}