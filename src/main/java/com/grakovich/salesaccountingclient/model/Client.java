package com.grakovich.salesaccountingclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("name")
    private String name;
    @JsonProperty("surname")
    private String surname;

    public Client(String email, String name, String surname) {
        this.email = email;
        this.name = name;
        this.surname = surname;
    }
}
