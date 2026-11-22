package by.coldynee.homework_2.dao;

import by.coldynee.homework_2.entity.User;
import by.coldynee.homework_2.util.HibernateUtil;
import jakarta.validation.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация {@link UserDao} на основе Hibernate.
 * <p>
 * Каждый метод открывает собственную сессию и управляет транзакциями.
 * В случае ошибки выполняется автоматический rollback.
 * </p>
 */
public class UserDaoProcessor implements UserDao{

    private static final Validator validator;
    private final SessionFactory sessionFactory;

    public UserDaoProcessor() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public UserDaoProcessor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()){
            validator = factory.getValidator();
        }
    }

    private void validate(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            String errors = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            throw new IllegalArgumentException("Ошибка валидации: " + errors);
        }
    }

    @Override
    public User save(User user) {
        validate(user);
        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try(Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        }
    }

    @Override
    public List<User> findAll() {
        try(Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).getResultList();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try(Session session = sessionFactory.openSession()) {
            List<User> users = session.createQuery(
                            "FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getResultList();
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        }
    }

    @Override
    public void update(User user) {
        validate(user);
        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to update user " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to delete user " + e.getMessage(), e);
        }
    }
}