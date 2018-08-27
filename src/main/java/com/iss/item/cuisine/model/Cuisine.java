package com.iss.item.cuisine.model;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Cuisine {

	private int id;
	private String name;
	private String type;
	private float price;
	private float preferential;
	private float discount;
	private String lable;
	private String remark;

	
	public Cuisine(ResultSet rs){
		try {
			this.id = rs.getInt("id");
			//this.name = rs.getString("name");
			this.name = new String(rs.getBytes("name"), "ISO-8859-1");
			this.type = rs.getString("type");
			this.price = rs.getInt("price");
			this.preferential = rs.getInt("preferential");
			this.discount = rs.getInt("discount");
			this.lable = rs.getString("lable");
			this.remark = rs.getString("remark");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getPreferential() {
		return preferential;
	}

	public void setPreferential(float preferential) {
		this.preferential = preferential;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	@Override
	public String toString() {
		return "Cuisine [id=" + id + ", name=" + name + ", type=" + type + ", price=" + price + ", preferential="
				+ preferential + ", discount=" + discount + ", lable=" + lable + ", remark=" + remark + "]";
	}

	
}
