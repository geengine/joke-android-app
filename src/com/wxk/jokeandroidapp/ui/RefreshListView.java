package com.wxk.jokeandroidapp.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.R.id;
import com.wxk.jokeandroidapp.R.layout;
import com.wxk.jokeandroidapp.R.string;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RefreshListView extends ListView implements OnScrollListener {

	private float mDownY;
	private float mMoveY;

	private int mHeaderHeight;

	private int mCurrentScrollState;

	private final static int NONE_PULL_REFRESH = 0; // ����״̬
	private final static int ENTER_PULL_REFRESH = 1; // ��������ˢ��״̬
	private final static int OVER_PULL_REFRESH = 2; // �������ˢ��״̬
	private final static int EXIT_PULL_REFRESH = 3; // ���ֺ󷴵�����״̬
	private int mPullRefreshState = 0; // ��¼ˢ��״̬

	private final static int REFRESH_BACKING = 0; // ������
	private final static int REFRESH_BACKED = 1; // �ﵽˢ�½��ޣ�����������
	private final static int REFRESH_RETURN = 2; // û�дﵽˢ�½��ޣ�����
	private final static int REFRESH_DONE = 3; // ���ݼ������

	private LinearLayout mHeaderLinearLayout = null;
	private LinearLayout mFooterLinearLayout = null;
	private TextView mHeaderTextView = null;
	private TextView mHeaderUpdateText = null;
	private ImageView mHeaderPullDownImageView = null;
	private ImageView mHeaderPullUpImageView = null;
	private ProgressBar mHeaderProgressBar = null;
	private TextView mFooterText = null;
	private ProgressBar mFooterProgressBar = null;

	private SimpleDateFormat mSimpleDateFormat;

	private Object mRefreshObject;
	private RefreshListener mRefreshListener;

	public void setOnRefreshListener(RefreshListener refreshListener) {
		this.mRefreshListener = refreshListener;
	}

	public RefreshListView(Context context) {
		this(context, null);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	void init(Context context) {
		mHeaderLinearLayout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.refresh_list_header, null);
		addHeaderView(mHeaderLinearLayout);
		mHeaderTextView = (TextView) findViewById(R.id.txt_refresh_header_text);
		mHeaderUpdateText = (TextView) findViewById(R.id.txt_refresh_header_lastupdate);
		mHeaderPullDownImageView = (ImageView) findViewById(R.id.img_refresh_pull_down);
		mHeaderPullUpImageView = (ImageView) findViewById(R.id.img_refresh_pull_up);
		mHeaderProgressBar = (ProgressBar) findViewById(R.id.refresh_progressbar);

		mFooterLinearLayout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.refresh_list_footer, null);
		addFooterView(mFooterLinearLayout);
		mFooterProgressBar = (ProgressBar) findViewById(R.id.pb_loadmore);
		mFooterText = (TextView) findViewById(R.id.txt_footertext);

		setSelection(1);
		setOnScrollListener(this);
		measureView(mHeaderLinearLayout);
		mHeaderHeight = mHeaderLinearLayout.getMeasuredHeight();

		mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		mHeaderUpdateText.setText(context.getString(
				R.string.header_refresh_lastUpdate,
				mSimpleDateFormat.format(new Date())));
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			mMoveY = ev.getY();
			if (mPullRefreshState == OVER_PULL_REFRESH) {
				mHeaderLinearLayout.setPadding(
						mHeaderLinearLayout.getPaddingLeft(),
						(int) ((mMoveY - mDownY) / 3),
						mHeaderLinearLayout.getPaddingRight(),
						mHeaderLinearLayout.getPaddingBottom());
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mPullRefreshState == OVER_PULL_REFRESH
					|| mPullRefreshState == ENTER_PULL_REFRESH) {
				new Thread() {
					public void run() {
						Message msg;
						while (mHeaderLinearLayout.getPaddingTop() > 1) {
							msg=mHandler.obtainMessage();
							msg.what=REFRESH_BACKING;
							mHandler.sendMessage(msg);
							try{
								sleep(5);
							}catch(Exception e){
								
							}
							msg=mHandler.obtainMessage();
							if(mPullRefreshState==NONE_PULL_REFRESH){
								msg.what=REFRESH_BACKED;
							}else{
								msg.what=REFRESH_RETURN;
							}
							mHandler.sendMessage(msg);
						}
					}
				}.start();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		System.out.println("asdfasdfasdf");
		if(mCurrentScrollState==SCROLL_STATE_TOUCH_SCROLL&&firstVisibleItem==0&&(mHeaderLinearLayout.getBottom()>=0&&mHeaderLinearLayout.getBottom()<mHeaderHeight)){
			if(mPullRefreshState==NONE_PULL_REFRESH){
				mPullRefreshState=ENTER_PULL_REFRESH;
			}
		}else if(mCurrentScrollState==SCROLL_STATE_IDLE&&firstVisibleItem==0&&mHeaderLinearLayout.getBottom()>=mHeaderHeight){
			if(mPullRefreshState==NONE_PULL_REFRESH||mPullRefreshState==ENTER_PULL_REFRESH){
				mPullRefreshState=OVER_PULL_REFRESH;
				mDownY= mMoveY;
				mHeaderTextView.setText("����ˢ��");
				mHeaderPullDownImageView.setVisibility(View.GONE);
				mHeaderPullUpImageView.setVisibility(View.GONE);
			}
		}else if(mCurrentScrollState==SCROLL_STATE_TOUCH_SCROLL&&firstVisibleItem!=0){
			if(mPullRefreshState==NONE_PULL_REFRESH){
				setSelection(1);
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		mCurrentScrollState=scrollState;
	}
	
	@Override
	public void setAdapter(ListAdapter adapter){
		super.setAdapter(adapter);
		setSelection(1);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case REFRESH_BACKING:
				mHeaderLinearLayout.setPadding(
						mHeaderLinearLayout.getPaddingLeft(),
						(int) (mHeaderLinearLayout.getPaddingTop() * 0.75f),
						mHeaderLinearLayout.getPaddingRight(),
						mHeaderLinearLayout.getPaddingBottom());
				break;
			case REFRESH_BACKED:
				mHeaderTextView.setText("���ڼ��ء�����");
				mHeaderProgressBar.setVisibility(View.VISIBLE);
				mHeaderPullDownImageView.setVisibility(View.GONE);
				mHeaderPullUpImageView.setVisibility(View.GONE);
				mPullRefreshState = EXIT_PULL_REFRESH;
				new Thread() {
					public void run() {
						if (mRefreshListener != null) {
							mRefreshObject = mRefreshListener.refreshing();
						}
						Message msg = mHandler.obtainMessage();
						msg.what = REFRESH_DONE;
						mHandler.sendMessage(msg);
					}
				}.start();
				break;
			case REFRESH_RETURN:
				mHeaderTextView.setText("����ˢ��");
				mHeaderProgressBar.setVisibility(View.GONE);
				mHeaderPullDownImageView.setVisibility(View.VISIBLE);
				mHeaderPullUpImageView.setVisibility(View.GONE);
				mPullRefreshState = EXIT_PULL_REFRESH;
				mHeaderLinearLayout.setPadding(mHeaderLinearLayout.getPaddingLeft(), 
						0, 
						mHeaderLinearLayout.getPaddingRight(), 
						mHeaderLinearLayout.getPaddingBottom());
				mPullRefreshState=NONE_PULL_REFRESH;
				setSelection(1);
				break;
			case REFRESH_DONE:
				mHeaderTextView.setText("����ˢ��");
				mHeaderProgressBar.setVisibility(View.GONE);
				mHeaderPullDownImageView.setVisibility(View.VISIBLE);
				mHeaderPullUpImageView.setVisibility(View.GONE);
				mHeaderUpdateText.setText(getContext().getString(R.string.header_refresh_lastUpdate,mSimpleDateFormat.format(new Date())));
				mHeaderLinearLayout.setPadding(mHeaderLinearLayout.getPaddingLeft(), 
						0, 
						mHeaderLinearLayout.getPaddingRight(), 
						mHeaderLinearLayout.getPaddingBottom());
				mPullRefreshState=NONE_PULL_REFRESH;
				setSelection(1);
				if(mRefreshListener!=null){
					mRefreshListener.Refreshed(mRefreshObject);
				}
				break;
			}
		}

	};

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public interface RefreshListener {
		Object refreshing();

		void Refreshed(Object obj);

		void more();
	}
}
