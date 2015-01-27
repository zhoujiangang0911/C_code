package cn.wislight.meetingsystem.domain;

import java.io.Serializable;



public class GroupElement implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9071450729654073911L;

	public boolean isCheck;
	public String groupName;
	public String remark;  
	public String createDate;
	public String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public void copy(GroupElement ge) {
		// TODO Auto-generated method stub
		this.isCheck = false;
		this.groupName = ge.groupName;
		this.remark = ge.remark;  
		this.createDate = ge.createDate;
		this.id = ge.id;
	}
};
