package com.grakovich.salesaccountingclient.repository;

import com.grakovich.salesaccountingclient.controller.Controller;
import com.grakovich.salesaccountingclient.model.Client;
import com.grakovich.salesaccountingclient.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientRepo extends Controller {

    private ObservableList<Client> clientDataProd = FXCollections.observableArrayList();
    private ResponseEntity<List<Client>> clientData;
    private static Long selectedItemId;

    public Long getSelectedItemId() {
        return selectedItemId;
    }

    public void setSelectedItemId(Long selectedItemId) {
        this.selectedItemId = selectedItemId;
    }

    public ObservableList<Client> getClientData() {
        return clientDataProd;
    }

    public void init(){
        clientDataProd.clear();
        clientData = restClient.exchange(SERVER_URL + "clients",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Client>>(){});
        clientData.getBody().forEach(p -> {
            clientDataProd.add(p);
        });
    }
}
