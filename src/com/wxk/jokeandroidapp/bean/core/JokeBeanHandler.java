package com.wxk.jokeandroidapp.bean.core;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.wxk.jokeandroidapp.bean.JokeBean;

public class JokeBeanHandler extends DefaultHandler {

	private static final String TAG = "52lxh:JokeBeanHandler";

	private JokeBean currentItem;
	private List<JokeBean> items;
	private StringBuilder text;

	@Override
	public void startDocument() throws SAXException {
		items = new ArrayList<JokeBean>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		text = new StringBuilder();
		if (localName.equals("Joke")) {
			currentItem = new JokeBean();
			items.add(currentItem);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (currentItem == null) {
			return;
		}

		if (localName.equals("id")) {
			currentItem.setId(Integer.parseInt(text.toString()));
		} else if (localName.equals("title")) {
			currentItem.setTitle(text.toString());
		} else if (localName.equals("content")) {
			currentItem.setContent(text.toString());
		} else if (localName.equals("imgurl")) {
			currentItem.setImgUrl(text.toString());
		} else if (localName.equals("imgurl")) {
			currentItem.setImgUrl(text.toString());
		} else if (localName.equals("publishdate")) {
			currentItem.setActiveDate(text.toString());
		} else if (localName.equals("replycount")) {
			currentItem.setReplyCount(Integer.parseInt(text.toString()));
		} else if (localName.equals("click")) {
			currentItem.setClickCount(Integer.parseInt(text.toString()));
		} else if (localName.equals("haoxiao")) {
			currentItem.setGooodCount(Integer.parseInt(text.toString()));
		} else if (localName.equals("haoleng")) {
			currentItem.setBadCount(Integer.parseInt(text.toString()));
		}

	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		text.append(ch, start, length);
	}

	public List<JokeBean> getJokeItems() {
		Log.i(TAG, String.format("=>::getJokeItems() count=%s", items.size()));
		return items;
	}
}
