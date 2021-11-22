package com.grakovich.salesaccountingclient.repository;

import com.grakovich.salesaccountingclient.controller.Controller;
import com.grakovich.salesaccountingclient.model.Order;
import com.grakovich.salesaccountingclient.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderRepo extends Controller {
    
    private ObservableList<Order> orderDataProd = FXCollections.observableArrayList();
    private ResponseEntity<List<Order>> orderData;

    public ObservableList<Order> getOrderDataProd() {
        return orderDataProd;
    }

    public void init(){
        orderDataProd.clear();
        orderData = restClient.exchange(SERVER_URL + "orders",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Order>>(){});
        orderData.getBody().forEach(p -> {
            orderDataProd.add(p);
        });
    }
}
