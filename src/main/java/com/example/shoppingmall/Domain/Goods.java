package com.example.shoppingmall.Domain;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.util.Date;

@Document(indexName = "goods")
public class Goods {
    private String name;

    @Field(type = FieldType.Date)
    private Date timestamp;

    private String detail;

    private String image;

    @Field(type = FieldType.Date)
    private Date insertionTime;

    private Long isDeleted;

    @Field(type = FieldType.Date)
    private Date modificationTime;

    private Long price;

    private String sellId;

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

	@Override
	public String toString() {
		return "Goods [name=" + name + ", timestamp=" + timestamp + ", detail=" + detail + ", image=" + image
				+ ", insertionTime=" + insertionTime + ", isDeleted=" + isDeleted + ", modificationTime="
				+ modificationTime + ", price=" + price + ", sellId=" + sellId + "]";
	}
}

