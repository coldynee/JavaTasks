package by.coldynee.homework_2;

import by.coldynee.homework_2.dao.UserDao;
import by.coldynee.homework_2.entity.User;

import java.util.List;

/**
 * Реализация {@link UserServiceInterface}.
 * Содержит бизнес-логику и делегирует операции с БД в {@link UserDao}.
 */
public class UserService implements UserServiceInterface {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User registerUser(User user) {
        if (userDao.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с email " + user.getEmail() + " уже существует");
        }
        return userDao.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + id + " не найден"));
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public void updateAge(Long id, int newAge) {
        User user = getUserById(id);
        user.setAge(newAge);
        userDao.update(user);
    }

    @Override
    public void deleteUser(Long id) {
        userDao.delete(id);
    }
}