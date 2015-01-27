package cn.wislight.meetingsystem.domain;

import java.io.Serializable;

import cn.wislight.meetingsystem.util.Element;

public class AttenderVoteElement implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 185142947265355253L;
	
	public boolean isCheck;
	public String id;
	public String name;
	public int iJoinstate;
	public int iVotestate;
	private String phone;
	
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getiJoinstate() {
		return iJoinstate;
	}
	public void setiJoinstate(int iJoinstate) {
		this.iJoinstate = iJoinstate;
	}
	public int getiVotestate() {
		return iVotestate;
	}
	public void setiVotestate(int iVotestate) {
		this.iVotestate = iVotestate;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
