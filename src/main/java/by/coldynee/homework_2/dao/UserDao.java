package by.coldynee.homework_2.dao;

import by.coldynee.homework_2.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для работы с сущностью {@link User} в базе данных.
 * <p>
 * Определяет контракт CRUD-операций
 * </p>
 */
public interface UserDao {
    /**
     * Сохраняет нового пользователя в базу данных.
     *
     * @param user объект пользователя для сохранения (не должен иметь установленный id)
     * @return сохранённый пользователь с сгенерированным базой данных id
     * @throws RuntimeException если сохранение не удалось (например, дубликат email)
     */
    User save(User user);
    /**
     * Ищет пользователя по его уникальному идентификатору.
     *
     * @param id идентификатор пользователя
     * @return {@link Optional} с пользователем, если он найден, или пустой {@link Optional}
     */
    Optional<User> findById(Long id);
    /**
     * Возвращает список всех пользователей из базы данных.
     *
     * @return список пользователей; пустой список, если пользователей нет
     */
    List<User> findAll();
    /**
     * Обновляет данные существующего пользователя.
     *
     * @param user пользователь с обновлёнными данными (id должен быть установлен)
     * @throws RuntimeException если обновление не удалось
     */
    void update(User user);
    /**
     * Удаляет пользователя по его идентификатору.
     * <p>
     * Если пользователя с таким id не существует, метод завершается без ошибки.
     * </p>
     *
     * @param id идентификатор пользователя для удаления
     * @throws RuntimeException если удаление не удалось
     */
    void delete(Long id);
}
