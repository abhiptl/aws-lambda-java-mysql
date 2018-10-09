package com.abhishek.core;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        System.out.println("getSessionFactory() is called");
        if (null != sessionFactory) {
            System.out.println("SessionFactory is already created");
            return sessionFactory;

        }


        Configuration configuration = new Configuration();

        String jdbcUrl = "jdbc:mysql://"
                + System.getenv(Constant.RDS_HOSTNAME)
                + "/"
                + System.getenv(Constant.RDS_DB_NAME);

        configuration.setProperty("hibernate.connection.url", jdbcUrl);
        configuration.setProperty("hibernate.connection.username", System.getenv(Constant.RDS_USERNAME));
        configuration.setProperty("hibernate.connection.password", System.getenv(Constant.RDS_PASSWORD));

        configuration.configure("hibernate.cfg.xml");
        //ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        try {
            //sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            sessionFactory = configuration.buildSessionFactory();
        } catch (HibernateException e) {
            System.err.println("Initial SessionFactory creation failed." + e);
            throw new ExceptionInInitializerError(e);
        }
        return sessionFactory;
    }
}
