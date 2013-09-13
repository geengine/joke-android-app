package com.wxk.jokeandroidapp.ui.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

public abstract class AsyncLoader<D> extends AsyncTaskLoader<D> {
	private D data;

	public AsyncLoader(Context context) {
		super(context);
	}

	@Override
	public void deliverResult(D data) {
		if (isReset()) {
			return;
		}
		this.data = data;
		super.deliverResult(data);
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
		data = null;
	}

	@Override
	protected void onStartLoading() {
		if (data != null) {
			deliverResult(data);
		}
		if (takeContentChanged() || data == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}
}
