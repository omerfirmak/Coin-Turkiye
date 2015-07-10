package com.omerfirmak.btcturktracker2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.AsyncTask;


public class Refresher extends AsyncTask<String, Void, String> {

	Map<String, Double> map;
	
	protected String doInBackground(String... params) {
		try{
			StringBuilder builder = new StringBuilder();
			HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

			DefaultHttpClient client = new DefaultHttpClient();

			SchemeRegistry registry = new SchemeRegistry();
			SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
			socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
			registry.register(new Scheme("https", socketFactory, 443));
			SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
			DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
			
			HttpGet requestedUrl = new HttpGet(MainActivity.targetURL);
			HttpResponse response;
			response = httpClient.execute(requestedUrl);		
			HttpEntity entity = response.getEntity();
			if (entity != null) {
	            InputStream inputStream = entity.getContent();
	            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            for (String line = null; (line = bufferedReader.readLine()) != null;) {
	                builder.append(line).append("\n");
	            }
	        }
			map = new Gson().fromJson(builder.toString(), new TypeToken<HashMap<String, Double>>() {}.getType());
			}catch(Exception e){
				e.printStackTrace();
			}
		return null;
	}

	protected void onPostExecute(String result) {
		try{
			MainActivity.lastPrice.setText(map.get("last").toString()+ " TL");
			MainActivity.lastPriceVal = map.get("last");
		}catch(Exception e){}
		
		try {
			MainActivity.lastPrice.setText(map.get("last_order").toString()+ " TL");
			MainActivity.lastPriceVal = map.get("last_order");
		} catch (Exception e) {}
				
		try {
			MainActivity.lowestPrice.setText(map.get("low").toString()+ " TL");
			MainActivity.highestPrice.setText(map.get("high").toString()+ " TL");
			MainActivity.volume.setText(map.get("volume").toString()+ " BTC/LTC");

			String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
			MainActivity.lastUpdate.setText("Son guncelleme:  "+date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		super.onPostExecute(result);
		cancel(true);
	}

}
