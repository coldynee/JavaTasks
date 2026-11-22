package by.coldynee.homework_2.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Утилита для управления {@link SessionFactory} Hibernate.
 * <p>
 * Реализует паттерн Singleton: фабрика сессий создаётся один раз при первом обращении
 * и переиспользуется на протяжении всего жизненного цикла приложения.
 * Конфигурация читается из файла {@code hibernate.cfg.xml}.
 * </p>
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable e) {
                throw new ExceptionInInitializerError("SessionFactory creation failed: " + e);
        }
    }

    /**
     * Возвращает единственный экземпляр {@link SessionFactory}.
     *
     * @return фабрика сессий Hibernate
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Освобождает ресурсы фабрики сессий. Должен вызываться при завершении приложения.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}
