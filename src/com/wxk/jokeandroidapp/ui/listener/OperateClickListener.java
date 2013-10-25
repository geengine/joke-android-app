package com.wxk.jokeandroidapp.ui.listener;

import com.wxk.jokeandroidapp.App;
import com.wxk.jokeandroidapp.Constants;
import com.wxk.jokeandroidapp.R;
import com.wxk.jokeandroidapp.bean.JokeBean;
import com.wxk.jokeandroidapp.client.JokeClient;
import com.wxk.jokeandroidapp.ui.activity.app.DetailActivity;
import com.wxk.jokeandroidapp.ui.adapter.JokeAdapter.ViewHolder;

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
		this(viewHolder, jokeBean, App.context);
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
			if (!App.isNetworkConnected()) {
				showToast(R.string.toast_error_network);
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
			if (!App.isNetworkConnected()) {
				showToast(R.string.toast_error_network);
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
		case R.id.btn_comment:// 评论
			Intent intentDetail = new Intent();
			intentDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intentDetail.putExtra(DetailActivity.EXTRA_JOKE_ID, bean.getId());

			intentDetail.setClass(App.context, DetailActivity.class);
			App.context.startActivity(intentDetail);
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
					JokeClient dao = new JokeClient();
					Boolean retVal = null;
					try {
						retVal = dao.postSupport(bean.getId(),
								flag.equals("add") ? 1 : 0).getStatus();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (retVal) {
						Message msg = new Message();
						msg.what = Constants.SERVER_SUCCESSFUL;
						msg.arg1 = "add".equals(flag) ? 1 : -1;
						upSuportHandler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = Constants.SERVER_ERROR;
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
					JokeClient dao = new JokeClient();
					Boolean retVal = null;
					try {
						retVal = dao.postSupport(bean.getId(),
								flag.equals("add") ? 2 : 0).getStatus();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (retVal) {
						Message msg = new Message();
						msg.what = Constants.SERVER_SUCCESSFUL;
						msg.arg1 = "add".equals(flag) ? 1 : -1;
						downSuportHandler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = Constants.SERVER_ERROR;
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
			case Constants.SERVER_SUCCESSFUL:
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
			case Constants.SERVER_ERROR:
				showToast(R.string.toast_error_network);
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
			case Constants.SERVER_SUCCESSFUL:
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
			case Constants.SERVER_ERROR:
				showToast(R.string.toast_error_network);
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
