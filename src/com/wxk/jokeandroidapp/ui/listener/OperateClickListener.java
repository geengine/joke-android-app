package com.wxk.jokeandroidapp.ui.listener;

import com.wxk.jokeandroidapp.AppContext;
import com.wxk.jokeandroidapp.Constant;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.dao.JokeDao;
import com.wxk.jokeandroidapp.ui.DetailActivity;
import com.wxk.jokeandroidapp.ui.adapter.JokesAdapter.ViewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class OperateClickListener implements OnClickListener {
	private Context context;
	private JokeBean bean;
	private ViewHolder viewHolder;
	private Boolean isUping = false;
	private Boolean isDowning = false;
	private Boolean isDowned = false;
	private Boolean isUped = false;

	public OperateClickListener(ViewHolder viewHolder, JokeBean jokeBean,
			Context context) {
		this.context = context;
		this.viewHolder = viewHolder;
		this.bean = jokeBean;
	}

	public OperateClickListener(ViewHolder viewHolder, JokeBean jokeBean) {
		this(viewHolder, jokeBean, AppContext.context);
	}

	public Animation getIconAnim() {
		final Animation icon_anim = new ScaleAnimation(0.5f, 1.4f, 0.5f, 1.4f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		icon_anim.setDuration(350);// 设置动画持续时间
		return icon_anim;
	}

	protected void showToast(String showText) {
		Toast toast = Toast.makeText(context, showText, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	protected void showToast(int resId) {
		showToast(context.getString(resId));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_good:
			if (!AppContext.isNetworkConnected()) {
				showToast(R.string.error_no_network);
				return;
			}
			if (!isUping && !isUped) {
				Drawable drawable = OperateClickListener.this.context
						.getResources().getDrawable(R.drawable.point_up_red);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				viewHolder.btnGood.setCompoundDrawables(drawable, null, null,
						null);
				viewHolder.btnGood
						.setTextColor(OperateClickListener.this.context
								.getResources().getColor(
										R.color.txt_count_num_red));
				Animation anim = getIconAnim();
				v.setAnimation(anim);
				anim.startNow();
				v.invalidate();
				doLikeUp("add"); // 顶
			}
			break;

		case R.id.btn_bad: // 踩
			if (!AppContext.isNetworkConnected()) {
				showToast(R.string.error_no_network);
				return;
			}
			if (!isDowning && !isDowned) {
				Drawable drawable = OperateClickListener.this.context
						.getResources().getDrawable(R.drawable.point_down_red);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				viewHolder.btnBad.setCompoundDrawables(drawable, null, null,
						null);
				viewHolder.btnBad
						.setTextColor(OperateClickListener.this.context
								.getResources().getColor(
										R.color.txt_count_num_red));
				Animation anim2 = getIconAnim();
				v.setAnimation(anim2);
				anim2.startNow();
				doLikeDown("add");
			}
			break;
		case R.id.btn_share:// 分享
			Intent intent = new Intent(Intent.ACTION_SEND);

			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
			intent.putExtra(Intent.EXTRA_TEXT, bean.getContent());
			intent = Intent.createChooser(intent,
					context.getString(R.string.app_name));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			break;
		case R.id.btn_comment://评论
			Intent intentDetail = new Intent();
			intentDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intentDetail.putExtra("id", bean.getId());
			intentDetail.putExtra("title", bean.getTitle());
			intentDetail.putExtra("content", bean.getContent());
			intentDetail.putExtra("replys", bean.getReplyCount());
			intentDetail.putExtra("clicks", bean.getClickCount());
			intentDetail.putExtra("goods", bean.getGooodCount());
			intentDetail.putExtra("bads", bean.getBadCount());
			intentDetail.putExtra("date", bean.getActiveDate());
			intentDetail.putExtra("imgurl", bean.getImgUrl());
			intentDetail.setClass(AppContext.context, DetailActivity.class);
			AppContext.context.startActivity(intentDetail);
			break;
		}
	}

	// 顶
	void doLikeUp(final String flag) {
		if (!isUping && !isUped) {
			isUping = true;
			new Thread() {

				@Override
				public void run() {
					super.run();
					JokeDao dao = new JokeDao();
					Boolean retVal = dao.doUp(bean.getId(), flag);
					if (retVal) {
						Message msg = new Message();
						msg.what = Constant.SERVER_SUCCESSFUL;
						msg.arg1 = "add".equals(flag) ? 1 : -1;
						upSuportHandler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = Constant.SERVER_ERROR;
						upSuportHandler.sendMessage(msg);
					}
				}

			}.start();
		}
	}

	// 踩
	void doLikeDown(final String flag) {
		if (!isDowning && !isDowned) {
			isDowning = true;
			new Thread() {

				@Override
				public void run() {
					super.run();
					JokeDao dao = new JokeDao();
					Boolean retVal = dao.doDown(bean.getId(), flag);
					if (retVal) {
						Message msg = new Message();
						msg.what = Constant.SERVER_SUCCESSFUL;
						msg.arg1 = "add".equals(flag) ? 1 : -1;
						downSuportHandler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = Constant.SERVER_ERROR;
						downSuportHandler.sendMessage(msg);
					}
				}

			}.start();
		}
	}

	// 顶
	Handler upSuportHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.SERVER_SUCCESSFUL:
				isUping = false;

				bean.setGooodCount(bean.getGooodCount() + msg.arg1);
				viewHolder.btnGood.setText("" + (bean.getGooodCount()));

				isUped = false;

				// 如果踩过
				// if (isDowned && isUped) {
				// // 踩 -1
				// isDowned = false;
				// doLikeDown("min");
				// LogUtil.d(TAG, " 踩过  -1");
				//
				// }
				break;
			case Constant.SERVER_ERROR:
				showToast(R.string.error_network);
				break;
			default:
				break;
			}

			Drawable drawable = OperateClickListener.this.context
					.getResources().getDrawable(R.drawable.point_up);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			viewHolder.btnGood.setCompoundDrawables(drawable, null, null, null);
			viewHolder.btnGood.setTextColor(OperateClickListener.this.context
					.getResources().getColor(R.color.txt_count_num));
		}

	};
	// 踩
	Handler downSuportHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.SERVER_SUCCESSFUL:
				isDowning = false;

				bean.setBadCount(bean.getBadCount() + msg.arg1);
				viewHolder.btnBad.setText("" + bean.getBadCount());

				isDowned = false;

				// 如果顶过
				// if (isUped && isDowned) {
				// // 顶 -1
				// isUped = false;
				// doLikeUp("min");
				// LogUtil.d(TAG, " 顶过  -1");
				// }

				break;
			case Constant.SERVER_ERROR:
				showToast(R.string.error_network);
				break;
			default:
				break;
			}
			Drawable drawable = OperateClickListener.this.context
					.getResources().getDrawable(R.drawable.point_down);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			viewHolder.btnBad.setCompoundDrawables(drawable, null, null, null);
			viewHolder.btnBad.setTextColor(OperateClickListener.this.context
					.getResources().getColor(R.color.txt_count_num));
		}

	};
}
