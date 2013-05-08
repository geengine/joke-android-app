package com.wxk.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class JokeListXMLContentHandler extends DefaultHandler {
	
	private String tagName;
	private String jokeID;
	private String jokeTitle;
	private String jokePublishDate;
	private String jokeImg;
	private StringBuffer jokeContent=new StringBuffer();
	private String click;
	private String replyCount;
	private String haoxiao;
	private String haoleng;
	private List<Map<String,String>> jokeList=null;
	
	public JokeListXMLContentHandler(List<Map<String,String>> arrJokeList){
		jokeList=arrJokeList;
	}
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		if(tagName=="id"){
			jokeID=new String(ch,start,length);
		}
		if(tagName=="title"){
			jokeTitle=new String(ch,start,length);
		}
		if(tagName=="content"){
			jokeContent.append(new String(ch,start,length));
		}
		if(tagName=="imgurl"){
			jokeImg=new String(ch,start,length);
		}
		if(tagName=="publishdate"){
			jokePublishDate=new String(ch,start,length);
		}
		if(tagName=="replycount"){
			replyCount=new String(ch,start,length);
		}
		if(tagName=="click"){
			click=new String(ch,start,length);
		}
		if(tagName=="haoxiao"){
			haoxiao=new String(ch,start,length);
		}
		if(tagName=="haoleng"){
			haoleng=new String(ch,start,length);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if(localName=="Joke"){
			Map<String,String> map=new HashMap<String, String>();
			map.put("id", jokeID);
			map.put("title", jokeTitle);
			map.put("img", jokeImg);
			map.put("publishdate", jokePublishDate);
			map.put("content", jokeContent.toString());
			map.put("click", click);
			map.put("replyCount", replyCount);
			map.put("haoxiao", haoxiao);
			map.put("haoleng", haoleng);
			jokeList.add(map);
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
		jokeID="";
		jokeTitle="";
		jokePublishDate="";
		jokeImg="";
		jokeContent=new StringBuffer();
		click="";
		replyCount="";
		haoxiao="";
	}

}
