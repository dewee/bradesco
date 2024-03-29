package br.com.bradesco.lelis;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/*
 * From https://gist.github.com/zvineyard/2012742
 */
public class Json {
	private static final String TAG = "bradesco";
	
	public static JSONObject getJson(String url) {
		InputStream is = null;
		String result = "";
		JSONObject jsonObject = null;
		// HTTP
		try {
			HttpClient httpclient = new DefaultHttpClient(); // for port 80 requests!
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return null;
		}
		// Read response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return null;
		}

		// Convert string to object
		try {
			jsonObject = new JSONObject(result);
		} catch (JSONException e) {
			return null;
		}
		return jsonObject;

	}
}
