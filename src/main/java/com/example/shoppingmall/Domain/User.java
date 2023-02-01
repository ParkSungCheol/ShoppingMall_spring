package com.example.shoppingmall.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class User {
	@JsonProperty
    private String NAME;
	@JsonProperty
    private int AGE;
	@JsonProperty
    private String GENDER;
}