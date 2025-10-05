package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import java.util.Properties;


public class Util {

    private static final String URL = "jdbc:mysql://localhost:3306/pp1";
    private static final String USER = "root";
    private static final String PASSWORD = "236Sfafa!!";

    private static SessionFactory sessionFactory;

    private Util() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            try {
                Configuration configuration = getConfiguration();

                ServiceRegistry serviceRegistry =
                        new StandardServiceRegistryBuilder()
                                .applySettings(configuration.getProperties())
                                .build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                System.out.println("Hibernate SessionFactory создан.");
            } catch (HibernateException e) {
                throw new IllegalStateException("Не удалось инициализировать Hibernate SessionFactory", e);
            }

        }
        return sessionFactory;
    }

    private static Configuration getConfiguration() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("hibernate.connection.url", URL);
        properties.setProperty("hibernate.connection.username", USER);
        properties.setProperty("hibernate.connection.password", PASSWORD);
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");

        Configuration configuration = new Configuration();
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(User.class);
        return configuration;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("SessionFactory закрыта.");
        }
    }
}