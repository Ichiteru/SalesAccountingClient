package com.grakovich.salesaccountingclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("product")
    private Product product;
    @JsonProperty("client")
    private Client client;
    @JsonProperty("user")
    private User user;
    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("price")
    private Double price;

    public Order(Product product, Client client, User user, Integer amount, Double price) {
        this.product = product;
        this.client = client;
        this.user = user;
        this.amount = amount;
        this.price = price;
    }
}
