package com.example.shoppingmall.Domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class User {
	@JsonProperty
    private String id;
	@JsonProperty
    private String pwd;
	@JsonProperty
    private String name;
	@JsonProperty
    private String address;
	@JsonProperty
    private String phone;
	@JsonProperty
    private Character grade;
	@JsonProperty
    private Character category;
	@JsonProperty
    private int age;
	@JsonProperty
    private String email;
	@JsonProperty
	private Timestamp date;
}