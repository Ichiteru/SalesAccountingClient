package com.grakovich.salesaccountingclient.utils;

import javafx.scene.control.Alert;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    public void showAlert(AlertType alertType) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(alertType.title);
        alert.setHeaderText(alertType.header);
        alert.setContentText(alertType.message);
        alert.show();
    }


    public enum AlertType {
        PASSWORD_REGEX_WARNING(
                "Некорректно задан пароль",
                "Обратите внимание на требования",
                "• Не менее 8 символов\n" +
                "• Содержит как минимум одну цифру\n" +
                "• Содержит по крайней мере один символ нижнего и один символ верхнего алфавита\n" +
                "• Содержит хотя бы один символ из набора специальных символов (@#%$^ и т.д.)\n" +
                "• Не содержит пробела, табуляции и т.д."),
        PASSWORD_OR_LOGIN_IS_EMPTY(
                "Данные не заполнены",
                "Обратите внимание на требование",
                "Поле ввода логина или пароля не должно быть пустым."),
        USER_NOT_FOUND(
                "Ошибка при входе",
                "Повторите попытку",
                "Неверный логин или пароль."),
        PASSWORDS_NOT_MATCHES(
                "Пароли не совпадают",
                        "Повторите попытку",
                        "Пароли не совпадают."
        ),
        USER_ALREADY_EXISTS(
                "Ошибка регистрации",
                "Повторите попытку",
                "Пользователь с указанным именем уже существует."
        ),
        SOME_FIELD_IS_EMPTY(
                "Ошибка при отправке",
                        "Повторите попытку",
                        "Заполните все необходимые поля."
        ),
        WRONG_VALUE(
                "Ошибка при отправке",
                "Повторите попытку",
                "Введите корректное значение."
        ),
        MODEL_ALREADY_EXISTS(
                "Ошибка при добавлении",
                "Повторите попытку",
                "Продукт с таким названием модели уже существует."
        ),
        EMAIL_ALREADY_EXISTS(
                "Ошибка при добавлении",
                "Повторите попытку",
                "Клиент с такой почтой уже существует."
        ),
        SUPPLIER_ALREADY_EXISTS(
                "Ошибка при добавлении",
                "Повторите попытку",
                "Клиент с такой почтой уже существует."
        ),
        PRODUCT_NAME_ALREADY_EXISTS(
                "Ошибка при добавлении",
                "Повторите попытку",
                "Данный продукт уже существует."
        ),
        VALUE_NOT_SELECTED(
                "Ошибка",
                "Повторите попытку",
                "Выберите значение."
        ),
        SUPPLIER_HAS_SUPPLIES(
                "Ошибка",
                "Невозможно выполнить операцию",
                "Поставщик связан с поставками товара на склад."
        ),
        PRODUCT_WITH_THIS_NAME_THERE_IS_IN_WAREHOUSE(
                "Ошибка",
                "Невозможно выполнить операцию",
                "Продукт с данным именем записаны на складе."
        ),
        SALE_PRODUCT_COUNT_MORE_THAN_PRODUCTS_ON_WAREHOUSE(
                "Ошибка",
                "Невозможно выполнить операцию",
                "Указанное на продажу количество продуктов превышает продукты на складе."
        ),
        NO_ACCESS_USER(
                "Ошибка",
                "Невозможно перейти на страницу",
                "У Вас нет прав доступа."
        );

        private final String title;
        private final String message;
        private final String header;

        AlertType(String title, String header, String message) {
            this.title = title;
            this.header = header;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getTitle() {
            return title;
        }
    }
}
