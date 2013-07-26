package com.wxk.jokeandroidapp.dao;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.wxk.jokeandroidapp.AppContext;
import com.wxk.jokeandroidapp.client.ReplyClient;
import com.wxk.jokeandroidapp.client.ResponseData;
import com.wxk.jokeandroidapp.db.JokeDb;
import com.wxk.jokeandroidapp.db.ReplyDb;
import com.wxk.jokeandroidapp.bean.ReplyBean;
import com.wxk.jokeandroidapp.bean.ReplyBeanXMLContentHandler;
import com.wxk.util.LogUtil;
import com.wxk.util.UniqueList;

public class ReplyDao extends BaseDao {
	ReplyClient client = new ReplyClient();
	ReplyDb db = new ReplyDb();

	public List<ReplyBean> getReplys(int jokeId, int page, int size) {
		return getReplys(jokeId, page, size, true);
	}

	public List<ReplyBean> getReplys(int jokeId, int page, int size,
			boolean isDbCache) {
		ResponseData responseData;
		List<ReplyBean> list = null;
		String key = "getReplys" + page + "_" + size;
		if (!AppContext.isNetworkConnected()// no network
				|| (isDbCache && !this.isCacheDataFailure(key))) {
			list = db.getList(jokeId, page, size);
			return list;
		}
		try {
			responseData = client.getReplys(jokeId, page, size);
			if (responseData.getStatus()) {
				list = new UniqueList<ReplyBean>();
				SAXParserFactory factory = SAXParserFactory.newInstance();
				XMLReader reader = factory.newSAXParser().getXMLReader();
				reader.setContentHandler(new ReplyBeanXMLContentHandler(jokeId,
						list));
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

	/**
	 * add joke reply
	 * 
	 * @param jokeid
	 * @param content
	 * @return
	 */
	public boolean doReply(int jokeid, String content) {
		boolean retVal = false;
		ResponseData responseData = null;
		try {
			responseData = client.doReply(jokeid, content);
			retVal = responseData.getStatus();
			if (retVal) {
				JokeDb jokeDb = new JokeDb();
				jokeDb.updateReplyPlusPlus(jokeid);
			}
		} catch (Exception ex) {
			LogUtil.e(TAG, ex.getMessage());
		} finally {
			if (responseData != null)
				responseData.close();
		}
		return retVal;
	}
}
