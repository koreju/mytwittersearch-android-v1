package com.example.mytwittersearch.operation;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.mytwittersearch.utils.ConstantValues;

public class DownloadImageTask {
	private Map<String, Drawable> mImageCache = null;
	private ImageView mImageView = null;

	public DownloadImageTask() {
		mImageCache = new HashMap<String, Drawable>();
	}

	public void loadImage(ImageView view) {
		mImageView = view;
		String url = (String) view.getTag();
		if (mImageCache.containsKey(url)) {
			mImageView.setImageDrawable(mImageCache.get(url));
		} else {
			synchronized (this) {
				new ImageTask().execute(url);
			}
		}
	}

	private class ImageTask extends AsyncTask<String, Void, Drawable> {
		private String mUrl;

		@Override
		protected Drawable doInBackground(String... params) {
			mUrl = params[0];
			InputStream inputStream = null;
			try {
				URL url = new URL(mUrl);
				inputStream = url.openStream();
			} catch (MalformedURLException e) {
				Log.e(ConstantValues.LOG_TAG, "Malformed: " + e.getMessage());
				throw new RuntimeException(e);
			} catch (IOException e) {
				Log.e(ConstantValues.LOG_TAG, "I/O: " + e.getMessage());
				throw new RuntimeException(e);				
			}
			return Drawable.createFromStream(inputStream, "src");
		}

		@Override
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			synchronized (this) {
				if (mImageView != null) {
					mImageView.setImageDrawable(result);
				}
				mImageCache.put(mUrl, result);
			}
		}
	}
}
