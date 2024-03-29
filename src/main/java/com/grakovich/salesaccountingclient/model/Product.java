package com.grakovich.salesaccountingclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("unit_cost")
    private Double unitCost;
    @JsonProperty("model")
    private String model;

    @JsonProperty("productType")
    private ProductType productType;

    @JsonProperty("supplier")
    private Supplier supplier;

    public Product(Integer amount, Double unitCost, String model, ProductType productType, Supplier supplier) {
        this.amount = amount;
        this.unitCost = unitCost;
        this.model = model;
        this.productType = productType;
        this.supplier = supplier;
    }
}