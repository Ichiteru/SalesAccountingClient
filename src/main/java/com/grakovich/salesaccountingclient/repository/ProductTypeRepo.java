package com.grakovich.salesaccountingclient.repository;

import com.grakovich.salesaccountingclient.controller.Controller;
import com.grakovich.salesaccountingclient.model.ProductType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductTypeRepo extends Controller {

    private ObservableList<ProductType> ProductTypeDataProd = FXCollections.observableArrayList();
    private List<ProductType> ProductTypeListProd = new ArrayList<>();
    private ResponseEntity<List<ProductType>> ProductTypeData;

    public ObservableList<ProductType> getProductTypeDataProd() {
        return ProductTypeDataProd;
    }

    public List<ProductType> getProductTypeListProd() {
        return ProductTypeListProd;
    }

    public void init(){
        ProductTypeDataProd.clear();
        ProductTypeData = restClient.exchange(SERVER_URL + "productTypes",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductType>>(){});
        ProductTypeData.getBody().forEach(p -> {
            ProductTypeDataProd.add(p);
            ProductTypeListProd.add(p);
        });
    }
}
