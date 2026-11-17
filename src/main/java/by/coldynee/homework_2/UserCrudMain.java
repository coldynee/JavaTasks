package by.coldynee.homework_2;

import by.coldynee.homework_2.dao.UserDao;
import by.coldynee.homework_2.dao.UserDaoProcessor;
import by.coldynee.homework_2.entity.User;
import by.coldynee.homework_2.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class UserCrudMain {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoProcessor();
        UserServiceInterface userService = new UserService(userDao);
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        System.out.println("User-service");
        while(isRunning) {
            System.out.println("Меню");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Найти пользователя по ID");
            System.out.println("3. Показать всех пользователей");
            System.out.println("4. Обновить возраст пользователя");
            System.out.println("5. Удалить пользователя по ID");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1":
                        System.out.print("Введите имя: ");
                        String name = scanner.nextLine();
                        System.out.print("Введите email: ");
                        String email = scanner.nextLine();
                        System.out.print("Введите возраст: ");
                        int age = Integer.parseInt(scanner.nextLine());

                        User newUser = new User(name, email, age);
                        User savedUser = userService.registerUser(newUser);
                        System.out.println("Пользователь создан, ID: " + savedUser.getId());
                        break;

                    case "2":
                        System.out.print("Введите ID для поиска: ");
                        Long searchId = Long.parseLong(scanner.nextLine());
                        try {
                            User foundUser = userService.getUserById(searchId);
                            System.out.println("Пользователь найден: " + foundUser);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case "3":
                        System.out.println("Список всех пользователей:");
                        List<User> users = userService.getAllUsers();
                        if (users.isEmpty()) {
                            System.out.println("Список пуст");
                        } else {
                            users.forEach(System.out::println);
                        }
                        break;

                    case "4":
                        System.out.print("Введите ID пользователя для обновления: ");
                        Long updateId = Long.parseLong(scanner.nextLine());
                        System.out.print("Введите новый возраст: ");
                        int newAge = Integer.parseInt(scanner.nextLine());
                        userService.updateAge(updateId, newAge);
                        System.out.println("Возраст обновлен");
                        break;

                    case "5":
                        System.out.print("Введите ID пользователя для удаления: ");
                        Long deleteId = Long.parseLong(scanner.nextLine());
                        userService.deleteUser(deleteId);
                        System.out.println("Пользователь удалён");
                        break;

                    case "0":
                        System.out.println("Завершение работы");
                        isRunning = false;
                        break;

                    default:
                        System.out.println("Ожидается число 0-5, попробуйте снова");
                }

            } catch (NumberFormatException e){
                System.out.println("Введено некорректное число");
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный ввод, ошибка " + e.getLocalizedMessage());
            } catch (Exception e) {
                System.out.println("Ошибка при работе с базой данных: " + e.getLocalizedMessage());
            }
        }
        HibernateUtil.shutdown();
        scanner.close();
    }
}
