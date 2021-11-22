package com.grakovich.salesaccountingclient.controller;

import com.grakovich.salesaccountingclient.model.*;
import com.grakovich.salesaccountingclient.repository.*;
import com.grakovich.salesaccountingclient.utils.AlertService;
import com.grakovich.salesaccountingclient.utils.EmptyFieldValidationService;
import com.grakovich.salesaccountingclient.utils.InitialsService;
import com.grakovich.salesaccountingclient.utils.NumberValidationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.List;
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
    public Pane paneSuppliers;
    public TableView<Supplier> tableSuppliers;
    public TableColumn<Supplier, Long> colSupplierID;
    public TableColumn<Supplier, String> colSupplierName;
    public TextField tfSupplierName;
    public TableView<ProductType> tableProductTypes;
    public TableColumn<ProductType, Long> colProductTypeID;
    public TableColumn<ProductType, String> colProductTypeName;
    public TextField tfProductTypeName;

    public TextField tfProductModel;
    public TextField tfProductPrice;
    public TextField tfProductAmount;
    public ComboBox<ProductType> cbProductType;
    public ComboBox<Supplier> cbProductSupplier;
    public TableView<Product> tableProducts;
    public TableColumn<Product, Long> colProductID;
    public TableColumn<Product, String> colProductType;
    public TableColumn<Product, String> colProductModel;
    public TableColumn<Product, Integer> colProductAmount;
    public TableColumn<Product, Double> colProductPrice;
    public TableColumn<Product, String> colProductSupplier;
    public TextField tfProductSearch;
    public Pane paneOrder;
    public TableView<Order> tableOrders;
    public TableColumn<Order, Long> colOrderID;
    public TableColumn<Order, String> colOrderClient;
    public TableColumn<Order, String> colOrderProduct;
    public TableColumn<Order, Integer> colProductOrderAmount;
    public TableColumn<Order, Double> colProductOrderPrice;
    public ComboBox<Product> cbProductToOrder;
    public ComboBox<Client> cbClientToOrder;
    public TextField tfProductToOrderAmount;
    public TextField tfOrderSearch;
    public TableColumn<Order, String> colOrderDealer;
    public Pane paneUsers;
    public TableView<User> tableUsers;
    public TableColumn<User, Long> colUserID;
    public TableColumn<User, String> colUserLogin;
    public TableColumn<User, String> colUserPassword;
    public TableColumn<User, Role> colUserRole;
    public TextField tfUserLogin;
    public TextField tfUserPassword;
    public ComboBox<Role> cbUserRole;

    private Client selectedClient = new Client();
    private Product selectedProduct = new Product();
    private User selectedUser = new User();
    public Pane paneProducts;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductTypeRepo productTypeRepo;
    @Autowired
    private SupplierRepo supplierRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired private OrderRepo orderRepo;
    @Autowired
    private InitialsService initialsService;
    @Autowired
    private EmptyFieldValidationService fieldValidationService;
    @Autowired
    private AlertService alertService;
    @Autowired
    private NumberValidationService numberValidationService;

    public void initialize(){
        initRepo();
        System.out.println(clientRepo.getClientData());
        tableClients.setItems(getClientFilteredList(clientRepo.getClientData(), tfClientSearch));
        tableSuppliers.setItems(supplierRepo.getSupplierDataProd());
        tableUsers.setItems(userRepo.getUserDataProd());
        tableProductTypes.setItems(productTypeRepo.getProductTypeDataProd());
        tableProducts.setItems(getProductFilteredList(productRepo.getProductData(), tfProductSearch));
        tableOrders.setItems(getOrderFilteredList(orderRepo.getOrderDataProd(), tfOrderSearch));
        setClientTableRowEventListener();
        setProductTableRowEventListener();
        setUserTableRowEventListener();
        loadDataToProductTypeCB();
        loadDataToSupplierCB();
        loadDataToOrdersCB();
        cbUserRole.setItems(FXCollections.observableArrayList(List.of(Role.USER, Role.ADMIN)));
        cbProductType.setConverter(new StringConverter<ProductType>() {
            @Override
            public String toString(ProductType productType) {
                return productType.getId() + " | " + productType.getName();
            }

            @Override
            public ProductType fromString(String s) {
                return null;
            }
        });
        cbProductSupplier.setConverter(new StringConverter<Supplier>() {
            @Override
            public String toString(Supplier supplier) {
                return supplier.getId() + " | " + supplier.getName();
            }

            @Override
            public Supplier fromString(String s) {
                return null;
            }
        });
        cbClientToOrder.setConverter(new StringConverter<Client>() {
            @Override
            public String toString(Client client) {
                return client.getEmail();
            }

            @Override
            public Client fromString(String s) {
                return null;
            }
        });
        cbProductToOrder.setConverter(new StringConverter<Product>() {
            @Override
            public String toString(Product product) {
                return product.getProductType().getName() + " | " + product.getModel() + " | " + product.getSupplier().getName();
            }

            @Override
            public Product fromString(String s) {
                return null;
            }
        });
        colClientEmail.setCellValueFactory(new PropertyValueFactory<Client,String >("email"));
        colClientName.setCellValueFactory(new PropertyValueFactory<Client,String >("name"));
        colClientSurname.setCellValueFactory(new PropertyValueFactory<Client,String >("surname"));
        colClientID.setCellValueFactory(new PropertyValueFactory<Client,Long >("id"));

        colProductTypeName.setCellValueFactory(new PropertyValueFactory<ProductType,String >("name"));
        colProductTypeID.setCellValueFactory(new PropertyValueFactory<ProductType,Long >("id"));

        colSupplierName.setCellValueFactory(new PropertyValueFactory<Supplier,String >("name"));
        colSupplierID.setCellValueFactory(new PropertyValueFactory<Supplier,Long >("id"));

        colProductID.setCellValueFactory(new PropertyValueFactory<Product, Long>("id"));
        colProductModel.setCellValueFactory(new PropertyValueFactory<Product, String>("model"));
        colProductAmount.setCellValueFactory(new PropertyValueFactory<Product, Integer>("amount"));
        colProductPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("unitCost"));
        colProductType.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getProductType().getName()));
        colProductSupplier.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSupplier().getName()));

        colOrderID.setCellValueFactory(new PropertyValueFactory<Order, Long>("id"));
        colProductOrderAmount.setCellValueFactory(new PropertyValueFactory<Order, Integer>("amount"));
        colProductOrderPrice.setCellValueFactory(new PropertyValueFactory<Order, Double>("price"));
        colOrderClient.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getClient().getEmail()));
        colOrderProduct.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getProduct().getProductType().getName()
                + " | " + p.getValue().getProduct().getModel()
                + " | " + p.getValue().getProduct().getSupplier().getName()));
        colOrderDealer.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getUser().getUsername()));

        colUserID.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        colUserLogin.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        colUserPassword.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<User, Role>("role"));
    }

    private void initRepo() {
        productRepo.init();
        productTypeRepo.init();
        supplierRepo.init();
        userRepo.init();
        clientRepo.init();
        orderRepo.init();
    }

    public void switchToOrderPane(ActionEvent actionEvent) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(paneOrder);
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
        contentPane.getChildren().clear();
        contentPane.getChildren().add(paneSuppliers);
    }

    public void switchToUserPane(ActionEvent actionEvent) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(paneUsers);
    }

    public void loadDataToProductTypeCB(){
        productRepo.init();
        cbProductType.setItems(productTypeRepo.getProductTypeDataProd());
    }

    public void loadDataToSupplierCB(){
        supplierRepo.init();
        cbProductSupplier.setItems(supplierRepo.getSupplierDataProd());
    }

    public void loadDataToOrdersCB(){
        productRepo.init();
        clientRepo.init();
        cbProductToOrder.setItems(productRepo.getProductData());
        cbClientToOrder.setItems(clientRepo.getClientData());
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

    public void refreshProductTypesTable(){
        productTypeRepo.init();
        tableProductTypes.setItems(productTypeRepo.getProductTypeDataProd());
        tableProductTypes.refresh();
    }

    public void refreshSuppliersTable(){
        supplierRepo.init();
        tableSuppliers.setItems(supplierRepo.getSupplierDataProd());
        tableSuppliers.refresh();
    }
    public void refreshProductsTable(){
        productRepo.init();
        tableProducts.setItems(productRepo.getProductData());
        tableProducts.refresh();
    }

    public void refreshOrdersTable(){
        orderRepo.init();
        tableOrders.setItems(getOrderFilteredList(orderRepo.getOrderDataProd(), tfOrderSearch));
        tableOrders.refresh();
    }

    private void refreshUsersTable() {
        if (getMyUser().getRole().equals(Role.USER)){
            alertService.showAlert(AlertService.AlertType.NO_ACCESS_USER);
        } else {
            userRepo.init();
            tableUsers.setItems(userRepo.getUserDataProd());
            tableUsers.refresh();
        }
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

    public void setUserTableRowEventListener(){
        tableUsers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observableValue, User user, User newValue) {
                if (newValue != null){
                    selectedUser = newValue;
                    tfUserLogin.setText(newValue.getUsername());
                    tfUserPassword.setText(newValue.getPassword());
                    cbUserRole.setValue(newValue.getRole());
                }
            }
        });
    }

    public void setProductTableRowEventListener(){
        tableProducts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue<? extends Product> observableValue, Product user, Product newValue) {
                if (newValue != null){
                    selectedProduct = newValue;
                    System.out.println(selectedProduct);
                    tfProductAmount.setText(String.valueOf(newValue.getAmount()));
                    tfProductModel.setText(newValue.getModel());
                    tfProductPrice.setText(String.valueOf(newValue.getUnitCost()));
                    cbProductType.setValue(newValue.getProductType());
                    cbProductSupplier.setValue(newValue.getSupplier());
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

    private FilteredList<Product> getProductFilteredList(ObservableList<Product> products, TextField textField) {
        FilteredList<Product> filteredData = getProductFilteredData(products, textField);
        return filteredData;
    }

    private FilteredList<Product> getProductFilteredData(ObservableList<Product> products, TextField textField) {
        FilteredList<Product> filteredData = new FilteredList<>(products, p -> true);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (product.getModel().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (product.getId().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (product.getUnitCost().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (product.getProductType().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        return filteredData;
    }

    private FilteredList<Order> getOrderFilteredList(ObservableList<Order> orders, TextField textField) {
        FilteredList<Order> filteredData = getOrderFilteredData(orders, textField);
        return filteredData;
    }

    private FilteredList<Order> getOrderFilteredData(ObservableList<Order> orders, TextField textField) {
        FilteredList<Order> filteredData = new FilteredList<>(orders, p -> true);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(order -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (order.getClient().getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (order.getProduct().getModel().toLowerCase().contains(lowerCaseFilter) ||
                order.getProduct().getSupplier().getName().toLowerCase().contains(lowerCaseFilter) ||
                order.getProduct().getProductType().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (order.getAmount().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (order.getPrice().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        return filteredData;
    }

    public void addProductType(ActionEvent actionEvent) {
        String name = tfProductTypeName.getText();
        ProductType productType = new ProductType(name);
        if (fieldValidationService.isEmpty(tfProductTypeName)){
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        } else{
            Boolean result = restClient.postForObject(SERVER_URL + "productType", productType, Boolean.class);
            if (!result) alertService.showAlert(AlertService.AlertType.PRODUCT_NAME_ALREADY_EXISTS);
            else {
                refreshProductTypesTable();
                loadDataToProductTypeCB();
            }
        }
    }

    public void deleteProductType(ActionEvent actionEvent) {
        if (tableProductTypes.getSelectionModel().getSelectedItem() == null)
            alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
        else {
            try{
            Map<String, String> param = new HashMap<>();
            param.put("id", String.valueOf(tableProductTypes.getSelectionModel().getSelectedItem().getId()));
            restClient.delete(SERVER_URL + "/productType/{id}", param);
            refreshProductTypesTable();
            } catch (HttpServerErrorException ex){
                alertService.showAlert(AlertService.AlertType.PRODUCT_WITH_THIS_NAME_THERE_IS_IN_WAREHOUSE);
            }
        }
    }

    public void deleteSupplier(ActionEvent actionEvent) {
        if (tableSuppliers.getSelectionModel().getSelectedItem() == null)
            alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
        else {
            try{
            Map<String, String> param = new HashMap<>();
            param.put("id", String.valueOf(tableSuppliers.getSelectionModel().getSelectedItem().getId()));
            restClient.delete(SERVER_URL + "/supplier/{id}", param);
            refreshSuppliersTable();
            } catch (HttpServerErrorException ex){
                alertService.showAlert(AlertService.AlertType.SUPPLIER_HAS_SUPPLIES);
            }
        }
    }

    public void addSupplier(ActionEvent actionEvent) {
        String name = tfSupplierName.getText();
        System.out.println(tfSupplierName.getText());
        Supplier supplier = new Supplier(name);
        if (fieldValidationService.isEmpty(tfSupplierName)){
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        } else{
            Boolean result = restClient.postForObject(SERVER_URL + "supplier", supplier, Boolean.class);
            if (!result) alertService.showAlert(AlertService.AlertType.SUPPLIER_ALREADY_EXISTS);
            else {
                refreshSuppliersTable();
                loadDataToSupplierCB();
            }
        }
    }

    public void deleteProduct(ActionEvent actionEvent) {
        if (tableProducts.getSelectionModel().getSelectedItem() == null)
            alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
        else {
            Map<String, String> param = new HashMap<>();
            param.put("id", String.valueOf(tableProducts.getSelectionModel().getSelectedItem().getId()));
            restClient.delete(SERVER_URL + "/product/{id}", param);
            refreshProductsTable();
            refreshSuppliersTable();
            refreshProductTypesTable();
        }
    }

    public void editProduct(ActionEvent actionEvent) {
        String model = tfProductModel.getText();
        Integer amount = Integer.valueOf(tfProductAmount.getText());
        Double price = Double.valueOf(tfProductPrice.getText());
        ProductType productType = cbProductType.getSelectionModel().getSelectedItem();
        Supplier supplier = cbProductSupplier.getSelectionModel().getSelectedItem();
        Product product = new Product(amount, price, model, productType, supplier);
        if (fieldValidationService.isEmpty(tfProductModel) || fieldValidationService.isEmpty(tfProductAmount)
                || fieldValidationService.isEmpty(tfProductPrice) || fieldValidationService.isEmpty(cbProductType)
                || fieldValidationService.isEmpty(cbProductSupplier)){
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        } else if(!numberValidationService.isValidDouble(tfProductPrice.getText())
                || !numberValidationService.isValidInteger(tfProductAmount.getText())){
            alertService.showAlert(AlertService.AlertType.WRONG_VALUE);
        }
        else{
            ResponseEntity<Boolean> result = restClient.exchange(SERVER_URL + "product", HttpMethod.PUT,
                    new HttpEntity<>(product),
                    Boolean.class);
            if (!result.getBody().booleanValue()) alertService.showAlert(AlertService.AlertType.PRODUCT_WITH_THIS_NAME_THERE_IS_IN_WAREHOUSE);
            else refreshProductsTable();
        }
    }

    public void addProduct(ActionEvent actionEvent) {
        String model = tfProductModel.getText();
        Integer amount = Integer.valueOf(tfProductAmount.getText());
        Double price = Double.valueOf(tfProductPrice.getText());
        ProductType productType = cbProductType.getSelectionModel().getSelectedItem();
        Supplier supplier = cbProductSupplier.getSelectionModel().getSelectedItem();
        Product product = new Product(amount, price, model, productType, supplier);
        if (fieldValidationService.isEmpty(tfProductModel) || fieldValidationService.isEmpty(tfProductAmount)
        || fieldValidationService.isEmpty(tfProductPrice) || fieldValidationService.isEmpty(cbProductType)
        || fieldValidationService.isEmpty(cbProductSupplier)){
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        } else if(!numberValidationService.isValidDouble(tfProductPrice.getText())
        || !numberValidationService.isValidInteger(tfProductAmount.getText())){
            alertService.showAlert(AlertService.AlertType.WRONG_VALUE);
        }
        else{
            Boolean result = restClient.postForObject(SERVER_URL + "product", product, Boolean.class);
            if (!result) alertService.showAlert(AlertService.AlertType.PRODUCT_WITH_THIS_NAME_THERE_IS_IN_WAREHOUSE);
            else refreshProductsTable();
        }
    }

    public void addOrder(ActionEvent actionEvent) {
        if (!numberValidationService.isValidInteger(tfProductToOrderAmount.getText())
                || tfProductToOrderAmount.getText().equals("")
                || cbProductToOrder.getSelectionModel().getSelectedItem() == null
                || cbClientToOrder.getSelectionModel().getSelectedItem() == null){
            alertService.showAlert(AlertService.AlertType.WRONG_VALUE);
        } else if (cbProductToOrder.getSelectionModel().getSelectedItem().getAmount() < Integer.parseInt(tfProductToOrderAmount.getText())){
            alertService.showAlert(AlertService.AlertType.SALE_PRODUCT_COUNT_MORE_THAN_PRODUCTS_ON_WAREHOUSE);
        } else {
            Order order = new Order();
            order.setAmount(Integer.parseInt(tfProductToOrderAmount.getText()));
            order.setClient(cbClientToOrder.getSelectionModel().getSelectedItem());
            order.setProduct(cbProductToOrder.getSelectionModel().getSelectedItem());
            order.setPrice(Integer.parseInt(tfProductToOrderAmount.getText()) * cbProductToOrder.getSelectionModel().getSelectedItem().getUnitCost());
            order.setUser(getMyUser());
            restClient.postForObject(SERVER_URL + "order", order, Boolean.class);
            Product product = cbProductToOrder.getSelectionModel().getSelectedItem();
            product.setAmount(product.getAmount() - Integer.parseInt(tfProductToOrderAmount.getText()));
            restClient.put(SERVER_URL + "product", product);
            refreshOrdersTable();
            refreshProductsTable();
        }
    }

    public void addUser(ActionEvent actionEvent) {
        if (fieldValidationService.isEmpty(tfUserLogin) ||
        fieldValidationService.isEmpty(tfUserPassword) || cbUserRole.getSelectionModel().isEmpty()){
            alertService.showAlert(AlertService.AlertType.WRONG_VALUE);
        } else {
            User user = new User();
            user.setUsername(tfUserLogin.getText());
            user.setPassword(tfUserPassword.getText());
            user.setRole(cbUserRole.getSelectionModel().getSelectedItem());
            Boolean result = restClient.postForObject(SERVER_URL + "register", user, Boolean.class);
            if (!result) alertService.showAlert(AlertService.AlertType.USER_ALREADY_EXISTS);
            else refreshUsersTable();
        }
    }


    public void deleteUser(ActionEvent actionEvent) {
        if (tableUsers.getSelectionModel().getSelectedItem() == null){
            alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(tableUsers.getSelectionModel().getSelectedItem().getId()));
            restClient.delete(SERVER_URL + "user/{id}", params);
            refreshUsersTable();
        }
    }

    public void editUser(ActionEvent actionEvent) {
        if (fieldValidationService.isEmpty(tfUserLogin) ||
                fieldValidationService.isEmpty(tfUserPassword)){
            alertService.showAlert(AlertService.AlertType.WRONG_VALUE);
        } else {
            User user = new User();
            user.setId(selectedUser.getId());
            user.setUsername(tfUserLogin.getText());
            user.setPassword(tfUserPassword.getText());
            user.setRole(cbUserRole.getSelectionModel().getSelectedItem());
            ResponseEntity<Boolean> result = restClient.exchange(SERVER_URL + "user", HttpMethod.PUT,
                    new HttpEntity<>(user),
                    Boolean.class);
            if (!result.getBody()) alertService.showAlert(AlertService.AlertType.USER_ALREADY_EXISTS);
            else refreshUsersTable();
        }
    }
}
