package com.easwareapps.marbleone_ad_free;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

//, TapjoyNotifier, TJEventCallback
public class PauseGame extends Activity implements OnClickListener{

	SharedPreferences masPref = null;
	SharedPreferences.Editor prefEditor = null;

	Button btnExit = null;
	Button btnResume = null;
	Button btnUndo = null;
	Button btnChangeColor = null;
	Button btnChangeBoard = null;
	Button btnRestart = null;
	Intent databackIntent = null;
	LinearLayout adLayout = null;


	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		masPref = this.getSharedPreferences("com.easwareapps.maspebble", MODE_PRIVATE);

		setContentView(R.layout.pause_layout);

		adLayout = (LinearLayout)findViewById(R.id.idAdLayout);
		btnResume = (Button)findViewById(R.id.btnResume);
		btnUndo = (Button)findViewById(R.id.btnUndo);
		btnChangeColor = (Button)findViewById(R.id.btnSelectMarble);
		btnChangeBoard = (Button)findViewById(R.id.btnSelectBoard);
		btnRestart = (Button)findViewById(R.id.btnRestart);
		btnExit = (Button)findViewById(R.id.btnExit);

		btnExit.setOnClickListener(this);
		btnResume.setOnClickListener(this);
		btnUndo.setOnClickListener(this);
		btnChangeColor.setOnClickListener(this);
		btnChangeBoard.setOnClickListener(this);
		btnRestart.setOnClickListener(this);







	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		try{
			databackIntent = new Intent();
			databackIntent.putExtra("action", MASPebbleActivity.ACTION_RESUME);
			setResult(Activity.RESULT_OK, databackIntent);
			finish();
		}catch (Exception e) {
			// close the activity if ad is not available.

		}

	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Log.d("onCLICK", "........");
		databackIntent = new Intent();
		if(view == btnExit){
			databackIntent.putExtra("action", MASPebbleActivity.ACTION_EXIT);
		}else if(view == btnResume){
			databackIntent.putExtra("action", MASPebbleActivity.ACTION_RESUME);
		}else if(view == btnRestart){
			databackIntent.putExtra("action", MASPebbleActivity.ACTION_RESTART);
		}else if(view == btnUndo){
			databackIntent.putExtra("action", MASPebbleActivity.ACTION_UNDO);
		}else if(view == btnChangeBoard){
			Intent intent = new Intent(this, SelectBoard.class);
			startActivity(intent);
			return;
		}else if(view == btnChangeColor){
			Intent intent = new Intent(this, SelectMarble.class);
			startActivityForResult(intent, 9);
			return;
		}
		try{
			setResult(Activity.RESULT_OK, databackIntent);
			finish();
		}catch(Exception e){
			Log.d("EXE", e.toString());
		}
	}	
	
	public void showHelp(View view){
		Intent browserIntent = new Intent(this, Browser.class);
		browserIntent.putExtra("page", "help");
		startActivity(browserIntent);
		
	}
	
	public void showCredits(View view){
		Intent browserIntent = new Intent(this, Browser.class);
		browserIntent.putExtra("page", "credits");
		startActivity(browserIntent);
	}
	


	







}
