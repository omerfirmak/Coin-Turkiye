package com.omerfirmak.btcturktracker2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.support.v7.app.ActionBar;


public class MainActivity extends Activity {
	static TextView lastPrice;
	static TextView lastUpdate;
	static TextView lowestPrice;
	static TextView highestPrice;
	static TextView volume;
	static ToggleButton alarmToggle;
	static EditText dusukLimit;
	static EditText yuksekLimit; 
	static ActionBar _actionBar;
	static String array_spinner[];
	static Spinner spinner;
	public static String targetURL;
	static String URLarray[];
	public static int dusukLimitVal,yuksekLimitVal;
	static double lastPriceVal;
	public static Context mContext;
	Alarm _alarm;
	Refresher _refresher;
	static Intent intent,serviceIntent;
	
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		InitializeUI();
		refresh();
		
		//internetWarning();
	
}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onPause(){
		super.onPause();
		if (serviceIntent == null){
		serviceIntent = new Intent("android.intent.AlarmService");
		startService(serviceIntent);
		}
	}	
	
	public void onResume(){
		super.onResume();
	}
	
	
	
	public void refresh(){
		_refresher = new Refresher();
		_refresher.execute("");
	}
	
	void InitializeUI(){
		targetURL =	"https://www.btcturk.com/api/ticker";
		URLarray = new String[3];
		URLarray[0] = "https://www.btcturk.com/api/ticker";
		URLarray[1] = "https://koinim.com/ticker/";
		URLarray[2] = "https://koinim.com/ticker/ltc";
		array_spinner=new String[3];
		array_spinner[0]="BTCTürk";
        array_spinner[1]="Koinim(BTC)";
        array_spinner[2]="Koinim(LTC)";		              
        spinner = (Spinner) findViewById(R.id.spinner1);
        @SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, array_spinner);
                spinner.setAdapter(adapter);
                
		Button refresh = (Button) findViewById(R.id.refresh);
		lastPrice = (TextView) findViewById(R.id.lastPrice);
		lastUpdate = (TextView) findViewById(R.id.lastUpdate);
		lowestPrice= (TextView) findViewById(R.id.lowestPrice);
		highestPrice = (TextView) findViewById(R.id.highestPrice);
		volume = (TextView) findViewById(R.id.volume);
		alarmToggle = (ToggleButton) findViewById(R.id.toggleButton1);
		dusukLimit = (EditText) findViewById(R.id.dusukLimitVal);
		yuksekLimit = (EditText) findViewById(R.id.yuksekLimitVal);
		_alarm = new Alarm();
		TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();
		
		TabHost.TabSpec tabSpec = tabHost.newTabSpec("Main");
		tabSpec.setContent(R.id.mainTab);
		tabSpec.setIndicator("Ana Sayfa");
		tabHost.addTab(tabSpec);
		
		tabSpec = tabHost.newTabSpec("Alarm");
		tabSpec.setContent(R.id.alarmTab);
		tabSpec.setIndicator("Alarm Ayarlari");
		tabHost.addTab(tabSpec);		
		
		
		
		dusukLimit.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try{
					dusukLimitVal = Integer.valueOf(dusukLimit.getText().toString());
				}catch(Exception e){
					dusukLimitVal = 0;
					e.printStackTrace();
				}				
			}
			public void afterTextChanged(Editable s) {}
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}			
		});
		
		yuksekLimit.addTextChangedListener(new TextWatcher(){		
			public void afterTextChanged(Editable s) {}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			public void onTextChanged(CharSequence s, int start, int before,int count) {
			try{
				yuksekLimitVal = Integer.valueOf(yuksekLimit.getText().toString());
			}catch(Exception e ){
				yuksekLimitVal = 0;
				e.printStackTrace();
			}
			}			
		});
		
		refresh.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("NewApi")
			public void onClick(View v){
				refresh();				
			}
		});
	
		spinner.setOnItemSelectedListener(
	            new AdapterView.OnItemSelectedListener() {
	                @Override
	                public void onItemSelected(AdapterView<?> arg0, View arg1,
	                        int arg2, long arg3) {
	                	targetURL = URLarray[spinner.getSelectedItemPosition()];	  
	                	refresh();
	                }

	                @Override
	                public void onNothingSelected(AdapterView<?> arg0) {

	                }
	               
	            }
	        );
	}
	
}
