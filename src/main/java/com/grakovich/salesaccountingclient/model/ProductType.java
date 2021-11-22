package com.grakovich.salesaccountingclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductType {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;

    public ProductType(String name) {
        this.name = name;
    }
}
