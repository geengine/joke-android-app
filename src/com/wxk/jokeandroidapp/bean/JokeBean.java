package com.wxk.jokeandroidapp.bean;

public class JokeBean {

	private int id;
	private String title;
	private String imgUrl;
	private String content;
	private String activeDate;
	private int clickCount;
	private int replyCount;
	private int gooodCount;
	private int badCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String context) {
		this.content = context;
	}

	public String getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(String activeDate) {
		this.activeDate = activeDate;
	}

	public int getClickCount() {
		return clickCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public int getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public int getGooodCount() {
		return gooodCount;
	}

	public void setGooodCount(int gooodCount) {
		this.gooodCount = gooodCount;
	}

	public int getBadCount() {
		return badCount;
	}

	public void setBadCount(int badCount) {
		this.badCount = badCount;
	}

	@Override
	public boolean equals(Object o) {
		JokeBean b = (JokeBean) o;
		if (b == null)
			return true;
		return this.getId() == b.getId();
	}

	public boolean equalsAll(JokeBean b) {
		if (b == null)
			return true;

		return this.getId() == b.getId()
				&& this.getTitle().equals(b.getTitle())
				&& this.getContent().equals(b.getContent())
				&& this.getReplyCount() == b.getReplyCount()
				&& this.getClickCount() == b.getClickCount()
				&& this.getGooodCount() == b.getGooodCount()
				&& this.getBadCount() == b.getBadCount();
	}
}
