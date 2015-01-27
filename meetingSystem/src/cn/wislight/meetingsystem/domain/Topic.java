package cn.wislight.meetingsystem.domain;

import java.io.Serializable;

public class Topic implements Serializable {
	private String id;
	private boolean isCheck;
	private String title;
	private String content;
	private String keywords;
	private String time;
	private String startTime;
	private String endTime;
	private boolean hasEdited;
	
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void copy(Topic item) {
		// TODO Auto-generated method stub
		this.id = item.getId();
		this.title = item.getTitle();
		this.content = item.getContent();
		this.keywords = item.getKeywords();
		this.time = item.getTime();
		this.startTime = item.getStartTime();
		this.endTime = item.getEndTime();
		this.isCheck = false;
		this.hasEdited = false;
	}

	public boolean isHasEdited() {
		return hasEdited;
	}

	public void setHasEdited(boolean hasEdited) {
		this.hasEdited = hasEdited;
	}

};
