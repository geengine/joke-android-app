package com.wxk.jokeandroidapp.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReplyBeanXMLContentHandler extends DefaultHandler {

	private String tagName="";
	private StringBuffer replyContent=new StringBuffer();
	private String replyTime="";
	private List<Map<String,String>> replyList=null;
	
	public ReplyBeanXMLContentHandler(List<Map<String,String>> arrReplyList){
		replyList=arrReplyList;
	}
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		if(tagName=="content"){
			replyContent.append(new String(ch,start,length));
		}
		if(tagName=="replytime"){
			replyTime=new String(ch,start,length);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if(localName=="Reply"){
			Map<String,String> map=new HashMap<String, String>();
			map.put("replytime", replyTime);
			map.put("content", replyContent.toString());
			replyList.add(map);
			ResetJokeParam();
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		tagName=localName;
	}
	
	private void ResetJokeParam(){
		replyContent=new StringBuffer();
		replyTime="";
	}

}
