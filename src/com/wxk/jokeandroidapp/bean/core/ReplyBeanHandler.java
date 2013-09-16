package com.wxk.jokeandroidapp.bean.core;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.wxk.jokeandroidapp.bean.ReplyBean;

public class ReplyBeanHandler extends DefaultHandler {

	private List<ReplyBean> mItems;

	private static final String TAG = "52lxh:ReplyBeanHandler";

	private ReplyBean currentItem;
	private StringBuilder text;

	@Override
	public void startDocument() throws SAXException {
		mItems = new ArrayList<ReplyBean>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		text = new StringBuilder();
		if (localName.equals("Reply")) {
			currentItem = new ReplyBean();
			mItems.add(currentItem);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (currentItem == null) {
			return;
		}

		if (localName.equals("replyid")) {
			currentItem.setId(Integer.parseInt(text.toString()));
		} else if (localName.equals("replyTime")) {
			currentItem.setActiveDate(text.toString());
		} else if (localName.equals("content")) {
			currentItem.setContent(text.toString());
		}

	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		text.append(ch, start, length);
	}

	public List<ReplyBean> getReplyItems() {
		Log.i(TAG, String.format("Items: %s", mItems.size()));
		return mItems;
	}
}
