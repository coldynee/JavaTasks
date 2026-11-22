package by.coldynee.homework_2.dao;

import by.coldynee.homework_2.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
public class UserDaoProcessorIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static SessionFactory sessionFactory;
    private UserDao dao;

    @BeforeAll
    static void setUp() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    @BeforeEach
    void setUpDao() {
        dao = new UserDaoProcessor(sessionFactory);
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY").executeUpdate();
            tx.commit();
        }
    }

    @Nested
    class SaveTests {
        @Test
        @DisplayName("Сохранение валидных данных")
        void saveValidData() {
            User user = new User("Nikita", "morknight0@gmail.com", 23);
            dao.save(user);

            Optional<User> foundUser = dao.findById(user.getId());
            assertTrue(foundUser.isPresent());
            assertEquals("Nikita", foundUser.get().getName());
        }

        @Test
        @DisplayName("Сохранение данных с невалидным именем")
        void shouldThrow_whenNameInvalid() {
            User user = new User("N", "morknight0@gmail.com", 23);

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                dao.save(user);
            });

            assertTrue(exception.getMessage().contains("Имя должно быть от 2 до 100 символов"));
        }

        @Test
        @DisplayName("Сохранение данных с невалидной почтой")
        void shouldThrow_whenEmailInvalid() {
            User user = new User("Nikita", "m@", 23);

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                dao.save(user);
            });

            assertTrue(exception.getMessage().contains("Некорректный формат email"));
        }

        @Test
        @DisplayName("Сохранение данных с невалидным возрастом")
        void shouldThrow_whenAgeInvalid() {
            User user = new User("Nikita", "morknight@gmail.com", -1);

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                dao.save(user);
            });

            assertTrue(exception.getMessage().contains("Возраст не может быть отрицательным"));
        }

        @Test
        @DisplayName("Сохранение пустых данных")
        void shouldThrow_whenEmptyData() {
            User user = new User();

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                dao.save(user);
            });

            assertTrue(exception.getMessage().contains("Ошибка валидации"));
        }
    }
    @Nested
    class FindByIdTests {
        @Test
        @DisplayName("Поиск по существующему ID")
        void findByExistsId() {
            User user = new User("Nikita", "morknight0@gmail.com", 23);
            dao.save(user);

            Optional<User> foundUser = dao.findById(user.getId());
            assertTrue(foundUser.isPresent());
            assertEquals("Nikita", foundUser.get().getName());
            assertEquals(23, foundUser.get().getAge());
        }

        @Test
        @DisplayName("Поиск по несуществующему ID")
        void shouldReturnEmptyOptional_whenIdNotExists() {
            Optional<User> foundUser = dao.findById(1L);
            assertTrue(foundUser.isEmpty());
        }
    }

    @Nested
    class FindAllTests {
        @Test
        @DisplayName("Поиск по наполненной базой данных")
        void findInFilledDatabase() {
            User user1 = new User("Nikita", "morknight0@gmail.com", 23);
            User user2 = new User("Sasha", "sasha123@gmail.com", 21);
            User user3 = new User("Test", "Test@test.com", 50);
            dao.save(user1);
            dao.save(user2);
            dao.save(user3);

            List<User> foundUsers = dao.findAll();
            assertEquals(3, foundUsers.size());
        }

        @Test
        @DisplayName("Поиск по пустой базе данных")
        void shouldReturnEmptyList_whenFindInEmptyDatabase() {
            List<User> foundUsers = dao.findAll();
            assertTrue(foundUsers.isEmpty());
        }
    }

    @Nested
    class FindByEmailTests {
        @Test
        @DisplayName("Поиск существующей почты")
        void findExistsEmail() {
            User user = new User("Nikita", "morknight0@gmail.com", 23);
            dao.save(user);

            Optional<User> foundUser = dao.findByEmail("morknight0@gmail.com");
            assertTrue(foundUser.isPresent());
        }

        @Test
        @DisplayName("Поиск несуществующей почты")
        void shouldReturnEmptyOptional_whenFindNotExistsEmail() {
            Optional<User> foundUser = dao.findByEmail("not_exists@gmail.com");
            assertTrue(foundUser.isEmpty());
        }
    }

    @Nested
    class UpdateTests {
        @Test
        @DisplayName("Обновление данных для существующего пользователя")
        void updateData_whenUserExists() {
            User user = new User("Nikita", "morknight0@gmail.com", 23);
            dao.save(user);
            Integer expectedAge = 24;

            user.setAge(expectedAge);
            dao.update(user);

            Optional<User> foundUser = dao.findById(user.getId());
            assertEquals(expectedAge, foundUser.get().getAge());
        }

        @Test
        @DisplayName("Обновление данных для несуществующего пользователя")
        void shouldAddUser_whenUpdateNotExistsData() {
            User user = new User("Nikita", "morknight0@gmail.com", 23);
            dao.update(user);

            assertEquals(1, dao.findAll().size());

            User savedUser = dao.findAll().get(0);
            assertNotNull(savedUser.getId());
            assertEquals("Nikita", savedUser.getName());
        }

        @Test
        @DisplayName("Обновление данных с невалидным именем")
        void shouldThrow_whenInvalidNameUpdate() {
            User user = new User("Nikita", "morknight0@gmail.com", 23);
            dao.save(user);

            user.setName("N");
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                dao.update(user);
            });

            assertTrue(exception.getMessage().contains("Имя должно быть от 2 до 100 символов"));
        }

        @Test
        @DisplayName("Обновление данных с невалидной почтой")
        void shouldThrow_whenInvalidEmailUpdate() {
            User user = new User("Nikita", "morknight0@gmail.com", 23);
            dao.save(user);

            user.setEmail("m@");
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                dao.update(user);
            });

            assertTrue(exception.getMessage().contains("Некорректный формат email"));
        }

        @Test
        @DisplayName("Обновление данных с невалидным возрастом")
        void shouldThrow_whenInvalidAgeUpdate() {
            User user = new User("Nikita", "morknight0@gmail.com", 23);
            dao.save(user);

            user.setAge(-1);
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                dao.update(user);
            });

            assertTrue(exception.getMessage().contains("Возраст не может быть отрицательным"));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        @DisplayName("Удаление существующего пользователя")
        void deleteValidUser() {
            User user = new User("Nikita", "morknight0@gmail.com", 23);
            dao.save(user);

            dao.delete(user.getId());
            Optional<User> foundUser = dao.findById(user.getId());
            assertTrue(foundUser.isEmpty());
        }

        @Test
        @DisplayName("Удаление несуществующего пользователя")
        void shouldNotThrow_whenDeleteInvalidUser() {
            assertDoesNotThrow(() -> dao.delete(1L));
        }
    }

    @AfterAll
    static void tearDown() { if (sessionFactory != null) sessionFactory.close(); }
}