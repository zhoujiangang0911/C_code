package cn.wislight.publicservice.util;

import java.io.Serializable;


public class ServiceTypeElement implements Serializable {
	public boolean isCheck;
	public String Typename;
	public String Typeid;
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public String getTypename() {
		return Typename;
	}
	public void setTypename(String typename) {
		Typename = typename;
	}
	public String getTypeid() {
		return Typeid;
	}
	public void setTypeid(String typeid) {
		Typeid = typeid;
	}
	
	public void copy(ServiceTypeElement ele) {
		this.Typename = ele.Typename;
		this.Typeid = ele.Typeid;
		this.isCheck = false;
	}
	
}
