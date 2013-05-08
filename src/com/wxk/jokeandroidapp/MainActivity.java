package com.wxk.jokeandroidapp;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wxk.tools.Common;
import com.wxk.tools.DownLoadHelper;
import com.wxk.tools.FilesHelper;
import com.wxk.tools.JokeListXMLContentHandler;

public class MainActivity extends Activity {

	private final String TAG = "MainActivity";
	private ListView listMain = null;
	private List<Map<String, String>> jokeList = null;
	private View loadingView = null;
	private boolean isLoading = false;
	private ProgressBar progressBar = null;
	private ProgressBar pb_loading = null;
	private JokeAdapter jokeAdapter = null;
	protected int listViewScrollState = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setTheme(android.R.style.Theme_Translucent_NoTitleBar);

		LayoutInflater inflater = LayoutInflater.from(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 检查版本更新
		// CheckVesion();
		listMain = (ListView) findViewById(R.id.list);
		// 向上滑动加载更多的进度条
		progressBar = (ProgressBar) findViewById(R.id.titlebar_progress);
		pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
		loadingView = inflater.inflate(R.layout.loading, null);
		loadingView.setVisibility(View.INVISIBLE);

		listMain.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				listViewScrollState = scrollState;
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if ((listViewScrollState == 2)
						&& (view.getLastVisiblePosition() >= totalItemCount - 1)) {
					CheckLoadMore();
				}
			}
		});
		listMain.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.d(TAG, "" + arg2 + "," + arg3);
				Map<String, String> jokeMap = jokeList.get(arg2);
				Intent intent = new Intent();
				intent.putExtra("jokecontent", jokeMap.get("content"));
				intent.putExtra("jokeTitle", jokeMap.get("title"));
				intent.putExtra("jokeID", jokeMap.get("id"));
				intent.putExtra("jokeImg", jokeMap.get("img"));
				intent.putExtra("haoxiao", jokeMap.get("haoxiao"));
				intent.putExtra("haoleng", jokeMap.get("haoleng"));
				intent.setClass(MainActivity.this, JokeDetailActivity.class);
				startActivity(intent);
			}
		});

		View listFooterView = inflater.inflate(R.layout.refresh_list_footer,
				null);

		listMain.addFooterView(listFooterView);
		jokeList = new ArrayList<Map<String, String>>();
		jokeAdapter = new JokeAdapter(jokeList, this);

		CheckLoadMore();
		listMain.setAdapter(jokeAdapter);
	}

	@Override
	/**
	 * 添加右键菜单
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "退出");
		return true;
	}

	@Override
	/**
	 * 设置右键菜单事件
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 0) {// 如果点击退出按钮提示用户是否退出！
			Dialog_Eixt(MainActivity.this);
		}
		return false;
	}

	@Override
	/**
	 * 退出时弹出退出提示框
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == event.KEYCODE_BACK || keyCode == 4 || keyCode == event.KEYCODE_HOME)
				&& event.getRepeatCount() == 0) {
			Dialog_Eixt(MainActivity.this);
		}
		return false;
	}

	/**
	 * 退出提示信息
	 * 
	 * @param context
	 *            当前文档
	 */
	private static boolean showExit = false;

	private static void Dialog_Eixt(Context context) {
		if (!showExit) {
			Common.ShowDialog("再按一次退出程序");
			showExit = true;
		} else {
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	/**
	 * 获取网络数据
	 */
	private Handler GetJokeList = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int res = msg.what;
			isLoading = false;
			if (res == 0) {
				loadingView.setVisibility(View.INVISIBLE);
				jokeAdapter.notifyDataSetChanged();
			} else if (res == 1) {
				loadingView.setVisibility(View.INVISIBLE);
				Common.ShowDialog("加载数据失败，可能是由于网络太慢！\n下拉重新加载");
			}
			progressBar.setVisibility(View.GONE);
			pb_loading.setVisibility(View.GONE);
		}
	};

	private void CheckLoadMore() {

		if (isLoading) {
			return;
		} else {
			if (listMain.getFooterViewsCount() != 0) {
				loadingView.setVisibility(View.VISIBLE);
				listMain.setSelection(jokeList.size() - 1);
				LoadMoreItems();
			}
		}
	}

	private void LoadMoreItems() {
		isLoading = true;
		progressBar.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {

				try {

					DownLoadHelper downloadHelper = new DownLoadHelper();
					int totalCount = jokeList.size();
					int pageSize = 10;
					int current = totalCount / pageSize;
					current = current < 1 ? 1 : current + 1;
					String url = "http://www.52lxh.com/appinterface/getnewdatalist.aspx?current="
							+ current + "&pagesize=" + pageSize;
					String res = downloadHelper.DownLoad(url);
					System.out.println(res);
					try {
						SAXParserFactory factory = SAXParserFactory
								.newInstance();
						XMLReader reader = factory.newSAXParser()
								.getXMLReader();
						reader.setContentHandler(new JokeListXMLContentHandler(
								jokeList));
						reader.parse(new InputSource(new StringReader(res)));
						GetJokeList.sendEmptyMessage(0);
						progressBar.setVisibility(View.GONE);
					} catch (Exception e) {
						// TODO: handle exception
						GetJokeList.sendEmptyMessage(1);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private Handler UpdateNewVesion = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			new AlertDialog.Builder(MainActivity.this)
					.setTitle("版本更新提示")
					.setMessage("发现新版本，是否更新？")
					.setPositiveButton("马上更新",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									downLoadApk();
								}
							})
					.setNegativeButton("以后再说",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
								}
							}).show();
		}
	};

	private void CheckVesion() {
		new Thread() {
			public void run() {
				try {
					DownLoadHelper downloadHelper = new DownLoadHelper();
					String newVesion = downloadHelper
							.DownLoad("http://www.52lxh.com/appinterface/getnewvesion.aspx");
					if (Integer.parseInt(newVesion) > MApplication.currentVesion) {
						UpdateNewVesion.sendEmptyMessage(0);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}.start();
	}

	/*
	 * 从服务器中下载APK
	 */
	private void downLoadApk() {
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = DownLoadHelper.getFileFromServer(
							"http://www.52lxh.com/downloadandroidapk.html", pd);
					// File file = new File(FilesHelper.GetPath("/52lxh/apk"),
					// "MainActivityNew.apk");
					installApk(file);
					pd.dismiss(); // 结束掉进度条对话框
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
}
