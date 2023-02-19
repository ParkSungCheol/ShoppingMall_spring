package com.example.shoppingmall.Domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Character getGrade() {
		return grade;
	}
	public void setGrade(Character grade) {
		this.grade = grade;
	}
	public Character getCategory() {
		return category;
	}
	public void setCategory(Character category) {
		this.category = category;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	
}