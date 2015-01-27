package cn.wislight.meetingsystem.util;

import java.io.Serializable;



public class Element implements Serializable{
	public boolean isCheck;
	public String name;
	public String org;
	public String post;
	public String id;
	public String type;
	public String sex;
	public String cardNo;
	public String phone;
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void copy(Element ele) {
		this.name = ele.name;
		this.org = ele.org;
		this.post = ele.post;
		this.id = ele.id;
		this.type = ele.type;
		this.sex = ele.sex;
		this.cardNo = ele.cardNo;
		this.phone = ele.phone;
		this.isCheck = false;
	}
	
};
