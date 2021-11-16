package com.grakovich.salesaccountingclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Supplier {

    @JsonProperty("id")
    private Long id;
    private String name;

    public Supplier(String name) {
        this.name = name;
    }
}
