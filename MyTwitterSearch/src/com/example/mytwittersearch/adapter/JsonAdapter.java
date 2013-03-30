package com.example.mytwittersearch.adapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.Uri;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytwittersearch.R;
import com.example.mytwittersearch.model.Tweet;
import com.example.mytwittersearch.model.TweetList;
import com.example.mytwittersearch.operation.DownloadImageTask;

public class JsonAdapter extends BaseAdapter {

	// Pattern for matching from_user
	private final Pattern mFromUserPattern = Pattern.compile("^[A-Za-z0-9_]+");

	// Pattern for matching @mention
	private final Pattern mMentionPattern = Pattern.compile("@[A-Za-z0-9_]+");

	// Pattern for matching #hash
	private final Pattern mHashPattern = Pattern.compile("#[A-Za-z0-9_]+");

	// Pattern for matching URL, Linkify.WEB_URLS seem to overdo the
	// substitution
	private final Pattern mUrlPattern = Pattern.compile("http://[^ ]+");
	
	private final String mTwitterUserURL = "http://twitter.com/";
	private final String mTwitterSearchURL = "http://search.twitter.com/search?q=";


	private TweetList mTweetList = null;
	private LayoutInflater mInflater = null;
	
	private class ViewHolder {
		TextView text, time;
		ImageView photo;
	}

	private Linkify.TransformFilter noAtSign = new Linkify.TransformFilter() {
		// A filter to remove the @ character before the user name
		@Override
		public String transformUrl(Matcher match, String user) {
			return user.substring(1);
		}
	};

	private Linkify.TransformFilter keywordEncoder = new Linkify.TransformFilter() {
		// encode the search keyword
		@Override
		public String transformUrl(Matcher match, String keyword) {
			return Uri.encode(keyword);
		}
	};

	public JsonAdapter(LayoutInflater inflater, TweetList tweetList) {
		mInflater = inflater;
		mTweetList = tweetList;
	}
	
	public void setListData(TweetList tweetList) {
		mTweetList = tweetList;
	}

	@Override
	public int getCount() {
		return mTweetList.getSize();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		ImageView photo;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item, parent, false);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.time = (TextView) convertView.findViewById(R.id.created_at);
			holder.photo = (ImageView) convertView
					.findViewById(R.id.user_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Tweet tweet = mTweetList.getTweet(position);
		if (tweet != null) {
			TextView textView = holder.text;
			textView.setText(tweet.getFromUser() + ": "
					+ Html.fromHtml(tweet.getText()));
//			Linkify.addLinks(textView, mFromUserPattern, mTwitterUserURL);
//			Linkify.addLinks(textView, mMentionPattern, mTwitterUserURL, null,
//					noAtSign);
//			Linkify.addLinks(textView, mHashPattern, mTwitterSearchURL, null,
//					keywordEncoder);
//			Linkify.addLinks(textView, mUrlPattern, "");
			photo = holder.photo;
			photo.setTag(tweet.getImageUrl());
			new DownloadImageTask().loadImage(photo);
			
			holder.time.setText(tweet.getCreatedAt());
		}
		return convertView;
	}
}
