package com.example.dish.beans;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

@Table(name = "Collect")
public class Collect {
	@Override
	public String toString() {
		return "Collect [id=" + id + ", userId=" + userId + ", dishId=" + dishId + "]";
	}

	@Column(name = "_id", isId = true, autoGen = true)
	private int id;
	@Column(name = "userId")
	private int userId;
	@Column(name = "dishId")
	private int dishId;

	public Collect() {
		
	}
	
	public Dish getDish(DbManager db) {
		try {
			return db.findById(Dish.class, dishId);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getDishId() {
		return dishId;
	}

	public void setDishId(int dishId) {
		this.dishId = dishId;
	}

	public Collect(int id, int userId, int dishId) {
		this.id = id;
		this.userId = userId;
		this.dishId = dishId;
	}

	public Collect(int userId, int dishId) {
		this.userId = userId;
		this.dishId = dishId;
	}

}
