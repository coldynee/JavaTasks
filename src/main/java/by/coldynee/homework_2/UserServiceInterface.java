package by.coldynee.homework_2;

import by.coldynee.homework_2.entity.User;

import java.util.List;

/**
 * Сервисный слой для бизнес-логики работы с пользователями.
 */
public interface UserServiceInterface {
    /**
     * Регистрирует нового пользователя.
     * Проверяет уникальность email.
     *
     * @param user пользователь для регистрации
     * @return сохраненный пользователь с ID
     * @throws IllegalArgumentException если email уже занят
     */
    User registerUser(User user);

    /**
     * Находит пользователя по ID.
     *
     * @param id идентификатор пользователя
     * @return пользователь, если найден
     * @throws IllegalArgumentException если пользователь не найден
     */
    User getUserById(Long id);

    /**
     * Возвращает список всех пользователей.
     *
     * @return список пользователей
     */
    List<User> getAllUsers();

    /**
     * Обновляет возраст пользователя.
     *
     * @param id идентификатор пользователя
     * @param newAge новый возраст
     * @throws IllegalArgumentException если пользователь не найден
     */
    void updateAge(Long id, int newAge);

    /**
     * Удаляет пользователя по ID.
     *
     * @param id идентификатор пользователя
     */
    void deleteUser(Long id);
}