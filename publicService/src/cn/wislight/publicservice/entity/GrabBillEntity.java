package cn.wislight.publicservice.entity;

/**
 * @author Administrator
 * 找生意 中抢单界面
 */
public  class GrabBillEntity {
	//发布人姓名
	private String name;
	//发布内容简介
	private String intro;
	//时间
	private String time;
	//距离
	private String distance;
	//头像
	private Integer photo;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public Integer getPhoto() {
		return photo;
	}
	public void setPhoto(Integer photo) {
		this.photo = photo;
	}
	
	
}
