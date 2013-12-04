package com.wxk.jokeandroidapp.bean;

import java.util.ArrayList;
import java.util.List;

public class TopicBean {

	/*
	 * 
	 * <ul id="allCategory"> <li><a href="/jokelist_7.html"
	 * title="成人笑话">成人笑话</a></li> <li><a href="/jokelist_1.html"
	 * title="夫妻笑话">夫妻笑话</a></li> <li><a href="/jokelist_9.html"
	 * title="冷笑话">冷笑话</a></li> <li><a href="/jokelist_3.html"
	 * title="校园笑话">校园笑话</a></li> <li><a href="/jokelist_4.html"
	 * title="家庭笑话">家庭笑话</a></li> <li><a href="/jokelist_5.html"
	 * title="儿童笑话">儿童笑话</a></li> <li><a href="/jokelist_6.html"
	 * title="动物笑话">动物笑话</a></li> <li><a href="/jokelist_2.html"
	 * title="爱情笑话">爱情笑话</a></li> <li><a href="/jokelist_8.html"
	 * title="恶心笑话">恶心笑话</a></li> <li><a href="/jokelist_10.html"
	 * title="经典笑话">经典笑话</a></li> <li><a href="/jokelist_18.html"
	 * title="搞笑图片">搞笑图片</a></li> <li><a href="/applyfriendlink.html"
	 * title="友链申请">友链申请</a></li> </ul> </p>
	 */

	public TopicBean(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String name;
	public int id;

	public static List<TopicBean> getTopics() {
		List<TopicBean> mTopics = new ArrayList<TopicBean>();
		mTopics.add(new TopicBean("全部", 0));
		mTopics.add(new TopicBean("囧冷", 9));
		mTopics.add(new TopicBean("成人", 7));
		mTopics.add(new TopicBean("夫妻", 1));
		mTopics.add(new TopicBean("搞笑", 18));
		mTopics.add(new TopicBean("爱情", 2));
		mTopics.add(new TopicBean("校园", 3));
		mTopics.add(new TopicBean("家庭", 4));
		mTopics.add(new TopicBean("儿童", 5));
		mTopics.add(new TopicBean("动物", 6));
		mTopics.add(new TopicBean("恶心", 8));
		mTopics.add(new TopicBean("经典", 10));
		return mTopics;
	}
}
