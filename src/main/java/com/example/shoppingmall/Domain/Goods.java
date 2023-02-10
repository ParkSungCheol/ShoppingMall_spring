package com.example.shoppingmall.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Goods {
	@JsonProperty
    private String name;
	@JsonProperty
    private int age;
	@JsonProperty
    private String gender;
}