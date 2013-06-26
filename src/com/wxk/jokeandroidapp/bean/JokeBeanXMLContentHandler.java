package com.wxk.jokeandroidapp.bean;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.annotation.SuppressLint;

public class JokeBeanXMLContentHandler extends DefaultHandler {

	private String tagName;
	private String jokeID;
	private String jokeTitle;
	private String jokePublishDate;
	private String jokeImg;
	private StringBuffer jokeContent = new StringBuffer();
	private String click;
	private String replyCount;
	private String haoxiao;
	private String haoleng;
	private List<JokeBean> jokeList = null;

	public JokeBeanXMLContentHandler(List<JokeBean> jokes) {
		jokeList = jokes;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		if (tagName == "id") {
			jokeID = new String(ch, start, length);
		}
		if (tagName == "title") {
			jokeTitle = new String(ch, start, length);
		}
		if (tagName == "content") {
			jokeContent.append(new String(ch, start, length));
		}
		if (tagName == "imgurl") {
			jokeImg = new String(ch, start, length);
		}
		if (tagName == "publishdate") {
			jokePublishDate = new String(ch, start, length);
		}
		if (tagName == "replycount") {
			replyCount = new String(ch, start, length);
		}
		if (tagName == "click") {
			click = new String(ch, start, length);
		}
		if (tagName == "haoxiao") {
			haoxiao = new String(ch, start, length);
		}
		if (tagName == "haoleng") {
			haoleng = new String(ch, start, length);
		}
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if (localName == "Joke") {

			JokeBean bean = new JokeBean();
			bean.setId(Integer.parseInt(jokeID));
			bean.setTitle(jokeTitle);
			bean.setContent(jokeContent.toString());
			bean.setImgUrl(jokeImg);
			bean.setClickCount(Integer.parseInt(click));
			bean.setReplyCount(Integer.parseInt(replyCount));
			bean.setActiveDate(jokePublishDate);
			bean.setBadCount(Integer.parseInt(haoleng));
			bean.setGooodCount(Integer.parseInt(haoxiao));

			jokeList.add(bean);
			ResetJokeParam();
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		tagName = localName;
	}

	private void ResetJokeParam() {
		jokeID = "";
		jokeTitle = "";
		jokePublishDate = "";
		jokeImg = "";
		jokeContent = new StringBuffer();
		click = "";
		replyCount = "";
		haoxiao = "";
	}

}
