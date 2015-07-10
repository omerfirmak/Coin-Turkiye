package com.omerfirmak.btcturktracker2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmService extends IntentService {
	  double checkVal;
		private Notification noti;
	    private NotificationManager nm;
	    PendingIntent contentIntent;
	    int yuksekLimitVal,dusukLimitVal,currentapiVersion = android.os.Build.VERSION.SDK_INT;
	    String targetURL;
	    Context mContext;
	public AlarmService() {
		super("AlarmService");
		targetURL = MainActivity.targetURL;
		dusukLimitVal = MainActivity.dusukLimitVal;
		yuksekLimitVal = MainActivity.yuksekLimitVal;
		mContext = MainActivity.mContext;
	}

	protected void onHandleIntent(Intent intent) {
		while(true){
			if(MainActivity.alarmToggle.isChecked()){
				try{	
					StringBuilder builder = new StringBuilder();
					HttpClient client = new DefaultHttpClient();
					HttpGet requestedUrl = new HttpGet(targetURL);
					HttpResponse response;
					response = client.execute(requestedUrl);		
					HttpEntity entity = response.getEntity();
					if (entity != null) {
			            InputStream inputStream = entity.getContent();
			            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			            for (String line = null; (line = bufferedReader.readLine()) != null;) {
			                builder.append(line).append("\n");
			            }
			        }
					Map<String, Double> map = new Gson().fromJson(builder.toString(), new TypeToken<HashMap<String, Double>>() {}.getType());
					
					try{
						checkVal = map.get("last");
					}catch(Exception e){}
					
					try{
						checkVal = map.get("last_order");
					}catch(Exception e){}
					
					if(checkVal <= dusukLimitVal && dusukLimitVal != 0){			
						Intent notificationIntent = new Intent(mContext.getApplicationContext(),MainActivity.class);
						notificationIntent.setAction(Intent.ACTION_MAIN);
						notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
						contentIntent = PendingIntent.getActivity(mContext.getApplicationContext(),
						        0, notificationIntent,
						        0);
						noti = new NotificationCompat.Builder(mContext)
						.setContentIntent(contentIntent)
						.setContentTitle("Coin Türkiye Düşük Fiyat Uyarısı")
				        .setContentText("Seçtiğiniz coinin fiyatı belirlediğiniz fiyatın altına düştü.")
				        .setSmallIcon(R.drawable.ic_launcher)
				        .setTicker("Coin Türkiye Düşük Fiyat Uyarısı")
				        .build();		
						noti.defaults = Notification.DEFAULT_ALL;
						noti.flags = Notification.FLAG_AUTO_CANCEL;
						
				
					        nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
					        nm.notify(1, noti);			
					}	
					if(checkVal >= yuksekLimitVal && yuksekLimitVal != 0){			
						Intent notificationIntent = new Intent(mContext.getApplicationContext(),MainActivity.class);
						notificationIntent.setAction(Intent.ACTION_MAIN);
						notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
						contentIntent = PendingIntent.getActivity(mContext.getApplicationContext(),
						        0, notificationIntent,
						        0);
						noti = new NotificationCompat.Builder(mContext)
						.setContentIntent(contentIntent)
						.setContentTitle("Coin Türkiye Yüksek Fiyat Uyarısı")
				        .setContentText("Seçtiğiniz coinin fiyatı belirlediğiniz fiyatın üstüne çıktı.")
				        .setSmallIcon(R.drawable.ic_launcher)
				        .setTicker("Coin Türkiye Yüksek Fiyat Uyarısı")
				        .build();
						noti.defaults = Notification.DEFAULT_ALL;
						noti.flags = Notification.FLAG_AUTO_CANCEL;
									
					        nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
					        nm.notify(2, noti);
					}			
				}catch(Exception e){
					e.printStackTrace();
				}
					
				try {
					Thread.sleep(600000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				}
			}

	}

}
