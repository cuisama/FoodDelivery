package com.iss.item.cuisine.model;

public class Cuisine {

	private int id;
	private String name;
	private String type;
	private float price;
	private float preferential;
	private float discount;
	private String lable;
	private String remark;
	
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
