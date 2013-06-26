package com.wxk.jokeandroidapp.dao;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.wxk.jokeandroidapp.AppContext;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.bean.JokeBeanXMLContentHandler;
import com.wxk.jokeandroidapp.client.JokeClient;
import com.wxk.jokeandroidapp.client.ResponseData;
import com.wxk.jokeandroidapp.db.JokeDb;
import com.wxk.util.LogUtil;
import com.wxk.util.UniqueList;

public class JokeDao extends BaseDao {

	JokeClient client = new JokeClient();
	JokeDb db = new JokeDb();

	public List<JokeBean> getJokes(int page, int size) {
		return getJokes(page, size, true);
	}

	public List<JokeBean> getJokes(int page, int pageSize, boolean isDbCache) {
		ResponseData responseData;
		List<JokeBean> list = null;
		String key = "getJokes_" + page + "_" + pageSize;
		if (!AppContext.isNetworkConnected()// no network
				|| (isDbCache && !this.isCacheDataFailure(key))) {
			list = db.getList(page, pageSize);
			return list;
		}
		try {
			responseData = client.getJokes(page, pageSize);
			if (responseData.getStatus()) {
				list = new UniqueList<JokeBean>();
				SAXParserFactory factory = SAXParserFactory.newInstance();
				XMLReader reader = factory.newSAXParser().getXMLReader();
				reader.setContentHandler(new JokeBeanXMLContentHandler(list));
				reader.parse(new InputSource(new StringReader(responseData
						.toString())));
				db.addAll(list);
				this.setMemCacheFinger(key);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LogUtil.w(TAG, "" + ex.getMessage());
		} finally {

		}
		return list;
	}
}
