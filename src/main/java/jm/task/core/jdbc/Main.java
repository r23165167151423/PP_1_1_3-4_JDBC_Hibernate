package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

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
            throw new RuntimeException(e);
        }
    }
}



