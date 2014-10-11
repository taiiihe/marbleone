/*  Marble One is Peg solitaire for android

    Copyright (C) 2014  Vishnu V vishnu@easwareapps.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.easwareapps.marbleone_ad_free;

import com.easwareapps.marbleone_ad_free.R;

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
