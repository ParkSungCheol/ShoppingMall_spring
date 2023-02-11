package com.example.shoppingmall.Domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Goods {
	@JsonProperty
    private int rownum;
	@JsonProperty
    private String name;
	@JsonProperty
    private int price;
	@JsonProperty
    private int amount;
	@JsonProperty
    private int discountamount;
	@JsonProperty
    private float discountrate;
	@JsonProperty
    private int deliveryfee;
	@JsonProperty
    private String sellid;
	@JsonProperty
	private Timestamp date;
	@JsonProperty
    private String image;
	@JsonProperty
    private String detail;
}