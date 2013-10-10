package com.wxk.jokeandroidapp.bean;

public class JokeBean {

	private long id;
	private String title;
	private String imgUrl;
	private String content;
	private String activeDate;
	private int clickCount;
	private int replyCount;
	private int gooodCount;
	private int badCount;
	private int topic;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public int getTopic() {
		return topic;
	}

	public void setTopic(int topic) {
		this.topic = topic;
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
				&& this.getReplyCount() == b.getReplyCount()
				// && this.getClickCount() == b.getClickCount()
				&& this.getGooodCount() == b.getGooodCount()
				&& this.getBadCount() == b.getBadCount();
	}
}
