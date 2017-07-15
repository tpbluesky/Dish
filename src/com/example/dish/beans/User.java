package com.example.dish.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "User")
public class User {

	@Column(name = "_id", isId = true, autoGen = true)
	private int id;
	@Column(name = "phoneNumber")
	private String phoneNumber;
	@Column(name = "name")
	private String name;
	@Column(name = "pasword")
	private String pasword;
	@Column(name = "userIcon")
	private String userIcon;

	public int getId() {
		return id;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasword() {
		return pasword;
	}

	public void setPasword(String pasword) {
		this.pasword = pasword;
	}

	public User() {
	}

	public User(int id, String phoneNumber, String name, String pasword) {
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.pasword = pasword;
	}

	public User(String phoneNumber, String name, String pasword) {
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.pasword = pasword;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", phoneNumber=" + phoneNumber + ", name=" + name + ", pasword=" + pasword
				+ ", userIcon=" + userIcon + "]";
	}

}
