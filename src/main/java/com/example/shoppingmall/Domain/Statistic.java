package com.example.shoppingmall.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Statistic {
	@JsonProperty
    private String keyAsString;
	@JsonProperty
    private long docCount;
	@JsonProperty
    private double averagePrice;
	public String getKeyAsString() {
		return keyAsString;
	}
	public void setKeyAsString(String keyAsString) {
		this.keyAsString = keyAsString;
	}
	public long getDocCount() {
		return docCount;
	}
	public void setDocCount(long docCount) {
		this.docCount = docCount;
	}
	public double getAveragePrice() {
		return averagePrice;
	}
	public void setAveragePrice(double averagePrice) {
		this.averagePrice = averagePrice;
	}
}
