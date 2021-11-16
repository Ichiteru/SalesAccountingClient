package com.grakovich.salesaccountingclient.controller;

import com.grakovich.salesaccountingclient.model.Client;
import com.grakovich.salesaccountingclient.repository.*;
import com.grakovich.salesaccountingclient.utils.AlertService;
import com.grakovich.salesaccountingclient.utils.EmptyFieldValidationService;
import com.grakovich.salesaccountingclient.utils.InitialsService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FxmlView("main-page.fxml")
public class MainPageController extends Controller{

    protected final static String TITLE = "Главная страница";


    public StackPane contentPane;
    public Pane paneClients;
    public TextField tfClientName;
    public TextField tfClientEmail;
    public TextField tfClientSurname;
    public TextField tfClientSearch;
    public TableView<Client> tableClients;
    public TableColumn<Client,String> colClientName;
    public TableColumn<Client,String> colClientSurname;
    public TableColumn<Client,String> colClientEmail;
    public TableColumn<Client, Long> colClientID;
    private Client selectedClient = new Client();
    public Pane paneProducts;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductTypeRepo productNameRepo;
    @Autowired
    private SupplierRepo supplierRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private InitialsService initialsService;
    @Autowired
    private EmptyFieldValidationService fieldValidationService;
    @Autowired
    private AlertService alertService;

    public void initialize(){
        initRepo();
        System.out.println(clientRepo.getClientData());
        tableClients.setItems(getClientFilteredList(clientRepo.getClientData(), tfClientSearch));
        setClientTableRowEventListener();
        colClientEmail.setCellValueFactory(new PropertyValueFactory<Client,String >("email"));
        colClientName.setCellValueFactory(new PropertyValueFactory<Client,String >("name"));
        colClientSurname.setCellValueFactory(new PropertyValueFactory<Client,String >("surname"));
        colClientID.setCellValueFactory(new PropertyValueFactory<Client,Long >("id"));
    }

    private void initRepo() {
        productRepo.init();
        productNameRepo.init();
        supplierRepo.init();
        userRepo.init();
        clientRepo.init();
    }

    public void switchToOrderPane(ActionEvent actionEvent) {

    }

    public void switchToClientPane(ActionEvent actionEvent) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(paneClients);
    }

    public void switchToProductPane(ActionEvent actionEvent) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(paneProducts);
    }

    public void switchToSupplierPane(ActionEvent actionEvent) {
    }

    public void switchToUserPane(ActionEvent actionEvent) {
    }

    public void editClient(ActionEvent actionEvent) {
        String name = tfClientName.getText();
        String surname = tfClientSurname.getText();
        String email = tfClientEmail.getText();
        if (fieldValidationService.isEmpty(tfClientName) || fieldValidationService.isEmpty(tfClientEmail)
                || fieldValidationService.isEmpty(tfClientSurname)){
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        } else if (!initialsService.isValidName(tfClientName.getText())
                || !initialsService.isValidName(tfClientSurname.getText())
                || !initialsService.isValidEmail(tfClientEmail.getText())) {
            alertService.showAlert(AlertService.AlertType.WRONG_VALUE);
        } else{
            Client client = new Client(selectedClient.getId(), email, name, surname);
            HttpEntity<Client> req = new HttpEntity<>(client);
            ResponseEntity<Boolean> result = restClient.exchange(SERVER_URL + "client", HttpMethod.PUT,
                    req,
                    Boolean.class);
            if (!result.getBody()) alertService.showAlert(AlertService.AlertType.EMAIL_ALREADY_EXISTS);
            else refreshClientsTable();
        }
    }

    public void deleteClient(ActionEvent actionEvent) {
        if (tableClients.getSelectionModel().getSelectedItem() == null)
            alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
        else {
            Map<String, String> param = new HashMap<>();
            param.put("id", String.valueOf(selectedClient.getId()));
            restClient.delete(SERVER_URL + "/client/{id}", param);
            refreshClientsTable();
        }
    }

    public void addClient() {
        String name = tfClientName.getText();
        String surname = tfClientSurname.getText();
        String email = tfClientEmail.getText();
        Client client = new Client(email, name, surname);
        if (fieldValidationService.isEmpty(tfClientName) || fieldValidationService.isEmpty(tfClientEmail)
        || fieldValidationService.isEmpty(tfClientSurname)){
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        } else if (!initialsService.isValidName(tfClientName.getText())
                || !initialsService.isValidName(tfClientSurname.getText())
        || !initialsService.isValidEmail(tfClientEmail.getText())) {
            alertService.showAlert(AlertService.AlertType.WRONG_VALUE);
        } else{
            Boolean result = restClient.postForObject(SERVER_URL + "client", client, Boolean.class);
            if (!result) alertService.showAlert(AlertService.AlertType.EMAIL_ALREADY_EXISTS);
             else refreshClientsTable();
        }

    }

    public void refreshClientsTable(){
        clientRepo.init();
        tableClients.setItems(getClientFilteredList(clientRepo.getClientData(), tfClientSearch));
        tableClients.refresh();
    }

    public void setClientTableRowEventListener(){
        tableClients.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Client>() {
            @Override
            public void changed(ObservableValue<? extends Client> observableValue, Client user, Client newValue) {
                if (newValue != null){
                    selectedClient = newValue;
                    System.out.println(selectedClient);
                    tfClientEmail.setText(newValue.getEmail());
                    tfClientName.setText(newValue.getName());
                    tfClientSurname.setText(newValue.getSurname());
                }
            }
        });
    }

    private FilteredList<Client> getClientFilteredList(ObservableList<Client> Clients, TextField textField) {
        FilteredList<Client> filteredData = getClientFilteredData(Clients, textField);
        return filteredData;
    }

    private FilteredList<Client> getClientFilteredData(ObservableList<Client> Clients, TextField textField) {
        FilteredList<Client> filteredData = new FilteredList<>(Clients, p -> true);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Client -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (Client.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (Client.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (Client.getSurname().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        return filteredData;
    }
}
