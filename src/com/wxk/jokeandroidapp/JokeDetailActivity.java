package com.wxk.jokeandroidapp;

import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wxk.tools.AsyncImageLoader;
import com.wxk.tools.AsyncImageLoader.ImageCallback;
import com.wxk.tools.Common;
import com.wxk.tools.DownLoadHelper;
import com.wxk.tools.ReplyListXMLContentHandler;

public class JokeDetailActivity extends Activity {

	private ImageButton btn_return = null;
	private ImageButton btn_submit = null;
	private TextView txt_noReply = null;
	private Button btn_good = null;
	private Button btn_bad = null;
	private EditText et_replyContent = null;
	private String jokeID = "";
	private String repContent = "";
	private List<Map<String, String>> replyList = null;
	private ProgressBar titleProgressBar = null;
	private boolean isReplying = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.joke_detail);

		titleProgressBar = (ProgressBar) findViewById(R.id.titlebar_progress);

		Intent intent = getIntent();
		String jokeContent = intent.getStringExtra("jokecontent");
		String jokeTitle = intent.getStringExtra("jokeTitle");
		String jokeImg = intent.getStringExtra("jokeImg");
		jokeID = intent.getStringExtra("jokeID");
		TextView txt_jokeTitle = (TextView) findViewById(R.id.titlebar_text);
		TextView txt_jokecontent = (TextView) findViewById(R.id.txt_jokeContent);
		ImageView imgView = (ImageView) findViewById(R.id.img_jokeImage);
		txt_noReply = (TextView) findViewById(R.id.txt_noReply);
		txt_jokeTitle.setText(jokeTitle);
		if (jokeContent.length() > 0)
			txt_jokecontent.setText(jokeContent);
		else {
			txt_jokecontent.setVisibility(View.GONE);
		}

		// 设置顶踩的数据和按钮事件
		btn_good = (Button) findViewById(R.id.btn_good);
		btn_good.setOnClickListener(new ButtonGoodOnCLickListener());
		btn_bad = (Button) findViewById(R.id.btn_bad);
		btn_bad.setOnClickListener(new ButtonBadOnClickListener());
		btn_good.setText(intent.getStringExtra("haoxiao"));
		btn_bad.setText(intent.getStringExtra("haoleng"));

		btn_return = (ImageButton) findViewById(R.id.titlebar_app_icon);
		// btn_return.setText("<<返回");
		// 给返回按钮添加监听事件
		btn_return.setOnClickListener(new ButtonReturnOnClickListener());
		// 提交按钮
		btn_submit = (ImageButton) findViewById(R.id.btn_submit);
		// 提交按钮侦听事件
		btn_submit.setOnClickListener(new ButtonSubmitOnClickListener());
		// 评论框
		et_replyContent = (EditText) findViewById(R.id.etxt_reply);
		// 加载图片
		if (jokeImg != "") {
			String imgUrl = jokeImg;
			LoadImg(imgUrl, imgView);
		} else {
			imgView.setVisibility(View.GONE);
		}
		// if (!isLoadingImg) {
		loadReplyList();
		// }
		titleProgressBar.setVisibility(View.GONE);
	}

	// 回复按钮事件
	class ButtonSubmitOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			repContent = et_replyContent.getText().toString();
			if (repContent.equals("")) {
				Common.ShowDialog("评论内容不能为空！");
			} else {
				doReply();
			}
		}

	}

	class ButtonReturnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			JokeDetailActivity.this.finish();
		}

	}

	class ButtonGoodOnCLickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			haoxiaoclick();
		}

	}

	class ButtonBadOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			haolengclick();
		}

	}

	private AsyncImageLoader loader = new AsyncImageLoader();

	private void LoadImg(final String imgUrl, ImageView imgView) {
		ImageCallback callBack = new ImageLoadImpI(imgView);
		loader.LoadDrawable(imgUrl, callBack);
	}

	private void doReply() {
		if (!isReplying) {
			isReplying = true;
			new Thread() {
				public void run() {
					try {
						String url = "http://www.52lxh.com/appinterface/doreply.aspx?jokeid="
								+ jokeID
								+ "&con="
								+ URLEncoder.encode(repContent);
						DownLoadHelper downloadHelper = new DownLoadHelper();
						String res = downloadHelper.DownLoad(url);
						if (res.equals("0")) {
							DoReplyHandler.sendEmptyMessage(0);
						} else if (res.equals("1")) {
							DoReplyHandler.sendEmptyMessage(1);
						} else {
							DoReplyHandler.sendEmptyMessage(2);
						}
					} catch (Exception e) {
						DoReplyHandler.sendEmptyMessage(2);
					}
				}
			}.start();
		}
	}

	private void loadReplyList() {
		new Thread() {
			public void run() {
				try {
					String url = "http://www.52lxh.com/appinterface/getnewreplylist.aspx?jokeid="
							+ jokeID + "&count=20";
					DownLoadHelper downloadHelper = new DownLoadHelper();
					String res = downloadHelper.DownLoad(url);
					if (res.equals("")) {
						GetReplyListHandler.sendEmptyMessage(0);
					} else {
						try {
							SAXParserFactory factory = SAXParserFactory
									.newInstance();
							XMLReader reader = factory.newSAXParser()
									.getXMLReader();
							replyList = new ArrayList<Map<String, String>>();
							reader.setContentHandler(new ReplyListXMLContentHandler(
									replyList));
							reader.parse(new InputSource(new StringReader(res)));
							GetReplyListHandler.sendEmptyMessage(1);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							GetReplyListHandler.sendEmptyMessage(2);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void haoxiaoclick() {
		new Thread() {
			public void run() {
				try {
					String url = "http://www.52lxh.com/appinterface/dosupport.aspx?id="
							+ jokeID + "&type=1";
					DownLoadHelper downloadHelper = new DownLoadHelper();
					String res = downloadHelper.DownLoad(url);
					if (!res.equals("")) {
						HaoxiaoHandler.sendEmptyMessage(Integer.parseInt(res));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void haolengclick() {
		new Thread() {
			public void run() {
				try {
					String url = "http://www.52lxh.com/appinterface/dosupport.aspx?id="
							+ jokeID + "&type=2";
					DownLoadHelper downloadHelper = new DownLoadHelper();
					String res = downloadHelper.DownLoad(url);
					if (!res.equals("")) {
						HaolengHandler.sendEmptyMessage(Integer.parseInt(res));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private Handler HaoxiaoHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what > 0) {
				if (btn_good != null) {
					btn_good.setText(msg.what + "");
				}
			}
		}

	};

	private Handler HaolengHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what > 0) {
				if (btn_bad != null) {
					btn_bad.setText(msg.what + "");
				}
			}
		}

	};

	private Handler GetReplyListHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int res = msg.what;
			switch (res) {
			case 0:
				txt_noReply.setVisibility(View.VISIBLE);
				break;
			case 1:
				txt_noReply.setVisibility(View.GONE);
				JokeReplyAdapter replyAdapter = new JokeReplyAdapter(replyList,
						JokeDetailActivity.this);
				ListView listReply = (ListView) findViewById(R.id.lv_replyList);
				listReply.setAdapter(replyAdapter);
				setListViewHeightBasedOnChildren(listReply);
				break;
			case 2:
				Common.ShowDialog("由于网络问题未能加载出来评论内容！");
				break;
			}
		}
	};

	/**
	 * 重新设置listview的高度
	 * 
	 * @param listView
	 *            目标listview
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))
				+ listView.getPaddingTop() + listView.getPaddingBottom();
		listView.setLayoutParams(params);
	}

	private Handler DoReplyHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int res = msg.what;
			switch (res) {
			case 0:
				Common.ShowDialog("评论成功！感谢您的支持!\n请继续关注52冷笑话！");
				et_replyContent.setText("");
				loadReplyList();
				break;
			case 1:
				Common.ShowDialog("评论成功！\n由于评论内容中含有敏感词，需要管理员审核之后才能显示！");
				et_replyContent.setText("");
				loadReplyList();
				break;
			case 2:
				Common.ShowDialog("系统异常，请重试！");
				break;
			}
			isReplying = false;
		}

	};
}
