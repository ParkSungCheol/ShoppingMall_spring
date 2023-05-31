package com.example.shoppingmall.Domain;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@Document(indexName = "goods")
public class Goods {
    private String name;

    @Field(type = FieldType.Date, name = "@timestamp")
    @JsonProperty("@timestamp")
    private Date timestamp;

    private String detail;

    private String image;

    @Field(type = FieldType.Date, name = "insertion_time")
    @JsonProperty("insertion_time")
    private Date insertionTime;

    @Field(name = "is_deleted")
    @JsonProperty("is_deleted")
    private Long isDeleted;

    @Field(type = FieldType.Date, name = "modification_time")
    @JsonProperty("modification_time")
    private Date modificationTime;

    private Long price;

    @Field(name = "sellid")
    @JsonProperty("sellid")
    private String sellId;

    private Integer amount;

    @Field(name = "discountamount")
    @JsonProperty("discountamount")
    private Integer discountAmount;

    @Field(name = "discountrate")
    @JsonProperty("discountrate")
    private Double discountRate;

    @Field(name = "deliveryfee")
    @JsonProperty("deliveryfee")
    private Integer deliveryFee;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getInsertionTime() {
		return insertionTime;
	}

	public void setInsertionTime(Date insertionTime) {
		this.insertionTime = insertionTime;
	}

	public Long getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Long isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getSellId() {
		return sellId;
	}

	public void setSellId(String sellId) {
		this.sellId = sellId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Integer discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}

	public Integer getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(Integer deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	@Override
	public String toString() {
		return "Goods [name=" + name + ", timestamp=" + timestamp + ", detail=" + detail + ", image=" + image
				+ ", insertionTime=" + insertionTime + ", isDeleted=" + isDeleted + ", modificationTime="
				+ modificationTime + ", price=" + price + ", sellId=" + sellId + ", amount=" + amount
				+ ", discountAmount=" + discountAmount + ", discountRate=" + discountRate + ", deliveryFee="
				+ deliveryFee + "]";
	}

}
