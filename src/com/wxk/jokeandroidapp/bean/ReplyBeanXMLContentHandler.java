package com.wxk.jokeandroidapp.bean;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReplyBeanXMLContentHandler extends DefaultHandler {

	private String tagName = "";
	private int jokeID;
	private String replyID;
	private StringBuffer replyContent = new StringBuffer();
	private String replyTime = "";
	private List<ReplyBean> replyList = null;

	public ReplyBeanXMLContentHandler(int jokeID, List<ReplyBean> arrReplyList) {
		replyList = arrReplyList;
		this.jokeID = jokeID;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (tagName == "replyid") {
			replyID = new String(ch, start, length);
		}
		if (tagName == "content") {
			replyContent.append(new String(ch, start, length));
		}
		if (tagName == "replytime") {
			replyTime = new String(ch, start, length);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if (localName == "Reply") {
			ReplyBean bean = new ReplyBean();
			bean.setId(Integer.parseInt(replyID));
			bean.setJokeID(jokeID);
			bean.setContent(replyContent.toString());
			bean.setActiveDate(replyTime);

			replyList.add(bean);
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
		replyContent = new StringBuffer();
		replyTime = "";
		replyID = "";
	}

}
