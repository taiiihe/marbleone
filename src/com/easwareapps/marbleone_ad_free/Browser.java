package com.easwareapps.marbleone_ad_free;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Browser extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browser_layout);
		WebView webView = (WebView)findViewById(R.id.idWebView);
		Bundle b = getIntent().getExtras();
		String url =  "file:///android_asset/help.html";
		
		if(b != null && b.getString("page").equals("credits")){
			url =  "file:///android_asset/credits.html";
		}
		
		webView.loadUrl(url);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		try{
			webView.getSettings().setDisplayZoomControls(false);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

}
