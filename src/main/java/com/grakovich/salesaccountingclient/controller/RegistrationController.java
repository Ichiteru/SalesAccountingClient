package com.grakovich.salesaccountingclient.controller;

import com.grakovich.salesaccountingclient.model.Role;
import com.grakovich.salesaccountingclient.model.User;
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
@RequiredArgsConstructor
@FxmlView("registration.fxml")
public class RegistrationController extends Controller{

    public static final String TITLE = "Регистрация";
    @FXML
    private TextField textFieldLogin;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private PasswordField textFieldRepeatPassword;

    private final ValidationService validationService;
    private final AlertService alertService;


    public void registration() {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        String repeatPassword = textFieldRepeatPassword.getText();

        if (!validationService.validateLoginAndPassword(login, password)){
            alertService.showAlert(AlertService.AlertType.PASSWORD_REGEX_WARNING);
        }
        else if (!password.equals(repeatPassword)){
            alertService.showAlert(AlertService.AlertType.PASSWORDS_NOT_MATCHES);
        }
        else{
            User user = new User(login, password, Role.USER);
//            HttpEntity<User> requestBody = new HttpEntity<>(user, getJsonHttpHeaders());
            HttpEntity<User> requestBody = new HttpEntity<>(user);
            Boolean result = restClient.postForObject(
                    SERVER_URL + "register", requestBody, Boolean.class);
            if (result){
                showCurrentStageWindow(LoginController.class, LoginController.TITLE);
            } else {
                alertService.showAlert(AlertService.AlertType.USER_ALREADY_EXISTS);
            }
        }
    }

    public void cancel() {
        showCurrentStageWindow(LoginController.class, LoginController.TITLE);
    }
}
