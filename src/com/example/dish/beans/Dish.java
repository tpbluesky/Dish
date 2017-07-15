package com.example.dish.beans;

import java.io.Serializable;
import java.sql.Date;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

@Table(name = "Dish")
public class Dish implements Serializable {

	@Column(name = "_id", isId = true, autoGen = true)
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "category")
	private String category;
	@Column(name = "material")
	private String material;
	@Column(name = "method")
	private String method;
	@Column(name = "audiopath")
	private String audiopath;
	@Column(name = "videopath")
	private String videopath;
	@Column(name = "iamgePath")
	private String imagePath;
	@Column(name = "publishDate")
	private Date publishDate;
	@Column(name = "owner")
	private int owner;

	public User getParent(DbManager db) {
		try {
			User u = db.findById(User.class, owner);
			return u;
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Dish(String name, String category, String material, String method, String audiopath, String videopath,
			String imagePath, Date publishDate, int owner) {
		super();
		this.name = name;
		this.category = category;
		this.material = material;
		this.method = method;
		this.audiopath = audiopath;
		this.videopath = videopath;
		this.imagePath = imagePath;
		this.publishDate = publishDate;
		this.owner = owner;
	}

	public Dish(int id, String name, String category, String material, String method, String audiopath,
			String videopath, String imagePath, Date publishDate, int owner) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.material = material;
		this.method = method;
		this.audiopath = audiopath;
		this.videopath = videopath;
		this.imagePath = imagePath;
		this.publishDate = publishDate;
		this.owner = owner;
	}

	public Dish() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Dish [id=" + id + ", name=" + name + ", category=" + category + ", material=" + material + ", method="
				+ method + ", audiopath=" + audiopath + ", videopath=" + videopath + ", imagePath=" + imagePath
				+ ", publishDate=" + publishDate + ", owner=" + owner + "]";
	}

	public int getId() {
		return id;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getAudiopath() {
		return audiopath;
	}

	public void setAudiopath(String audiopath) {
		this.audiopath = audiopath;
	}

	public String getVideopath() {
		return videopath;
	}

	public void setVideopath(String videopath) {
		this.videopath = videopath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

}
