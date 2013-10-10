package com.wxk.jokeandroidapp.db;

import java.util.ArrayList;
import java.util.List;

public class Topics {
	private static Topics uniqueInstance = new Topics();

	public static Topics getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Topics();
		}
		return uniqueInstance;
	}

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
	private List<Topic> mTopics = new ArrayList<Topic>();

	Topics() {
		mTopics.add(new Topic("全部", 0));
		mTopics.add(new Topic("冷笑话", 9));
		mTopics.add(new Topic("成人笑话", 7));
		mTopics.add(new Topic("夫妻笑话", 1));
		mTopics.add(new Topic("搞笑图片", 18));
		mTopics.add(new Topic("爱情笑话", 2));
		mTopics.add(new Topic("校园笑话", 3));
		// mTopics.add(new Topic("家庭笑话", 4));
		// mTopics.add(new Topic("儿童笑话", 5));
		// mTopics.add(new Topic("动物笑话", 6));
		mTopics.add(new Topic("恶心笑话", 8));
		mTopics.add(new Topic("经典笑话", 10));
	}

	public String[] getTopicKeys() {
		String[] mPlanetTitles = new String[mTopics.size()];
		for (int i = 0; i < mTopics.size(); i++) {
			Topic item = mTopics.get(i);
			mPlanetTitles[i] = item.name;
		}
		return mPlanetTitles;
	}

	public int getTopicID(int position) {
		return mTopics.get(position).id;
	}

	public class Topic {
		public Topic(String name, int id) {
			this.name = name;
			this.id = id;
		}

		public String name;
		public int id;
	}
}
