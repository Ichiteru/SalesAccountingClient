package com.grakovich.salesaccountingclient.repository;

import com.grakovich.salesaccountingclient.controller.Controller;
import com.grakovich.salesaccountingclient.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductRepo extends Controller {

    private ObservableList<Product> productDataProd = FXCollections.observableArrayList();
    private ResponseEntity<List<Product>> productData;
    private static Long selectedItemId;

    public Long getSelectedItemId() {
        return selectedItemId;
    }

    public void setSelectedItemId(Long selectedItemId) {
        this.selectedItemId = selectedItemId;
    }

    public ObservableList<Product> getProductData() {
        return productDataProd;
    }

    public void init(){
        productDataProd.clear();
        productData = restClient.exchange(SERVER_URL + "products",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>(){});
        productData.getBody().forEach(p -> {
            productDataProd.add(p);
        });
    }
}
