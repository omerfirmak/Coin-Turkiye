package com.omerfirmak.btcturktracker2;

import com.omerfirmak.btcturktracker2.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class splashScreen extends Activity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Thread timer = new Thread(){
			public void run(){
				try {
					sleep(1000);	
					startActivity(new Intent("android.intent.action.PAGE"));
				} catch (InterruptedException e) {}
				finally{
					finish();
				}
				
			}
		};
		
		if(isOnline()){
			timer.start();
		}else{
			internetWarning();
		}
	}
	
	public void internetWarning(){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Uygulamayı kullanabilmek için aktif bir internet bağlantısına ihtiyacınız vardır.")
			       .setTitle("Uyarı");
			
			builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   finish();
		           }
		       });					
			AlertDialog dialog = builder.create();
			dialog.show();
		
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
}
