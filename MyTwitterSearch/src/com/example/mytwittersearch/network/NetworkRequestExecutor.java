package com.example.mytwittersearch.network;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class NetworkRequestExecutor {
	private static final String TWITTER_SEARCH_URL = "http://search.twitter.com/search.json";
	private static byte[] sBuffer = new byte[1024];

	public static class DownloadException extends Exception {

		private static final long serialVersionUID = 1L;

		public DownloadException(String detailMessage) {
			super(detailMessage);
		}

		public DownloadException(String detailMessage, Throwable throwable) {
			super(detailMessage, throwable);
		}

	}

	public static class ParseException extends Exception {

		private static final long serialVersionUID = 2L;

		public ParseException(String detailMessage, Throwable throwable) {
			super(detailMessage, throwable);
		}

	}

	public static synchronized String downloadFromServer(String param)
			throws DownloadException {
		String uri = TWITTER_SEARCH_URL + param;
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(uri);

		try {
			HttpResponse response = client.execute(request);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				throw new DownloadException("Invalid response from "
						+ TWITTER_SEARCH_URL + ". Error: " + status.toString());
			}
			BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());
			InputStream inputStream = entity.getContent();
			ByteArrayOutputStream content = new ByteArrayOutputStream();
			int readCount = 0;
			while ((readCount = inputStream.read(sBuffer)) != -1) {
				content.write(sBuffer, 0, readCount);
			}
			return new String(content.toByteArray());
		} catch (Exception e) {
			throw new DownloadException("Problem connecting to the server"
					+ e.getMessage(), e);
		}
	}
}
