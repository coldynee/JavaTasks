package by.coldynee.homework_2;

import by.coldynee.homework_2.dao.UserDao;
import by.coldynee.homework_2.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceUnitTests {
    @Mock
    private UserDao dao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class RegisterUserTests {
        @Test
        @DisplayName("Регистрация валидного пользователя")
        void registerValidUser() {
            User user = new User("Nikita", "morknight0@gmail.com", 23);
            when(dao.findByEmail("morknight0@gmail.com")).thenReturn(Optional.empty());

            userService.registerUser(user);
            verify(dao, times(1)).save(user);
        }

        @Test
        @DisplayName("Регистрация пользователя с существующей почтой")
        void shouldThrow_whenEmailExists() {
            User user = new User("Nikita", "exists@gmail.com", 23);
            when(dao.findByEmail("exists@gmail.com")).thenReturn(Optional.of(user));

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                userService.registerUser(user);
            });

            verify(dao, never()).save(user);
            assertTrue(exception.getMessage().contains("Пользователь с email"));
        }
    }

    @Nested
    class GetUserByIdTests {
        @Test
        @DisplayName("Поиск существующего пользователя")
        void getExistsUser() {
            User user = new User("Nikita", "morknight0@gmail.com", 23);
            when(dao.findById(1L)).thenReturn(Optional.of(user));

            User actualUser = userService.getUserById(1L);
            assertEquals(user, actualUser);
        }

        @Test
        @DisplayName("Поиск несуществующего пользователя")
        void shouldThrow_whenUserNotExists() {
            when(dao.findById(1L)).thenReturn(Optional.empty());

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                userService.getUserById(1L);
            });

            assertTrue(exception.getMessage().contains("Пользователь с ID"));
        }
    }

    @Nested
    class GetAllUsersTests {
        @Test
        @DisplayName("Поиск с существующими пользователями")
        void get_whenUsersExists() {
            User user1 = new User("Nikita", "morknight0@gmail.com", 23);
            User user2 = new User("Sasha", "sasha@gmail.com", 21);
            when(dao.findAll()).thenReturn(List.of(user1, user2));

            List<User> users = userService.getAllUsers();
            assertEquals(2, users.size());
        }

        @Test
        @DisplayName("Поиск с несуществующими пользователями")
        void shouldReturnEmptyList_whenGetUsersNotExists() {
            when(dao.findAll()).thenReturn(Collections.emptyList());
            List<User> users = userService.getAllUsers();
            assertTrue(users.isEmpty());
        }
    }

    @Nested
    class UpdateAgeTests {
        @Test
        @DisplayName("Обновление возраста у существующего пользователя")
        void updateExitsUser() {
            User user = new User("Nikita", "morknight0@gmail.com", 23);
            when(dao.findById(1L)).thenReturn(Optional.of(user));

            userService.updateAge(1L, 2);
            verify(dao, times(1)).update(user);
        }

        @Test
        @DisplayName("Обновление возраста у несуществующего пользователя")
        void shouldNotUpdate_whenUserNotExists() {
            when(dao.findById(0L)).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () -> {
                userService.updateAge(0L, 2);
            });
            verify(dao, never()).update(any(User.class));
        }
    }

    @Nested
    class DeleteUserTests {
        @Test
        @DisplayName("Удаление существующего пользователя")
        void deleteExistsUser() {
            userService.deleteUser(1L);

            verify(dao, times(1)).delete(1L);
        }

        @Test
        @DisplayName("Удаление несуществующего пользователя")
        void shouldNotThrow_whenDeleteNotExistsUser() {
            assertDoesNotThrow(() -> userService.deleteUser(1L));
        }
    }
}

