package com.grakovich.salesaccountingclient.controller;

import com.grakovich.salesaccountingclient.model.Role;
import com.grakovich.salesaccountingclient.model.User;
import com.grakovich.salesaccountingclient.model.UserStatus;
import com.grakovich.salesaccountingclient.utils.AlertService;
import com.grakovich.salesaccountingclient.utils.ValidationService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
@FxmlView("login.fxml")
@RequiredArgsConstructor
public class LoginController extends Controller{

    protected final static String TITLE = "Вход";

    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private TextField textFieldLogin;

    private final ValidationService validationService;
    private final AlertService alertService;

    public void login() {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();

        if (!validationService.validateEmptyLines(login, password)) {
            alertService.showAlert(AlertService.AlertType.PASSWORD_OR_LOGIN_IS_EMPTY);
        } else {
            User user = new User(login, password, Role.USER);
            HttpEntity<User> requestBody = new HttpEntity<>(user);
//            HttpEntity<User> requestBody = new HttpEntity<>(user, getJsonHttpHeaders());
            UserStatus result = restClient.postForObject(SERVER_URL + "/login", requestBody, UserStatus.class);
            if (UserStatus.UNKNOWER.equals(result)) {
                alertService.showAlert(AlertService.AlertType.USER_NOT_FOUND);
            } else {
                showCurrentStageWindow (MainPageController.class, MainPageController.TITLE);
            }
        }
    }

    public void registration() {
        showCurrentStageWindow(RegistrationController.class, RegistrationController.TITLE);
    }
}
