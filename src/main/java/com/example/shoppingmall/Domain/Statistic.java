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
    private int averagePrice;
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
	public int getAveragePrice() {
		return averagePrice;
	}
	public void setAveragePrice(int averagePrice) {
		this.averagePrice = averagePrice;
	}
}
