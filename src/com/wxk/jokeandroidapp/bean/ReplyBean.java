package com.wxk.jokeandroidapp.bean;

public class ReplyBean {

	private long id;
	private long jokeID;
	private String content;
	private String activeDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getJokeID() {
		return jokeID;
	}

	public void setJokeID(long jokeID) {
		this.jokeID = jokeID;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(String activeDate) {
		this.activeDate = activeDate;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return true;
		ReplyBean b = (ReplyBean) o;
		return this.getId() == b.getId();
	}

	public boolean equalsAll(ReplyBean b) {
		if (b == null)
			return true;
		return this.equals(b) && this.getContent().equals(b.getContent());
	}
}
