package com.easwareapps.marbleone_ad_free;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class GameOver extends Activity {

	TextView txtGameScore;
	TextView txtTimeBonus;
	TextView txtTotalScore;
	TextView txtGameMessage;
	int NO_BOARDS = 8;



	int score = 0;
	int bonus = 0;
	int total = 0;
	int loop = 0;

	Handler handler;

	int SCORE = 0;
	int BONUS = 0;
	int TOTAL = 0;
	int pebble = 31;
	String message = "";
	String send = "";

	int incrementerScore = 0;
	int incrementerBonus = 0;

	

	ProgressDialog progressSpinner = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


		try{
			Intent intent = getIntent();
			SCORE = intent.getIntExtra("score", 0);
			BONUS = intent.getIntExtra("bonus", 0);
			message = intent.getStringExtra("message");
			send = intent.getStringExtra("send");
			pebble = intent.getIntExtra("pebble", 31);

			incrementerScore = SCORE / 10;
			incrementerScore = BONUS / 10;

			TOTAL = SCORE + BONUS;


		}catch (Exception e) {
			// TODO: handle exception
		}
		setContentView(R.layout.game_over_layout);
		SharedPreferences masPref = this.getSharedPreferences("com.easwareapps.maspebble", MODE_PRIVATE);
		int i=0;
		for(i=0; i<NO_BOARDS; i++){
			if(!masPref.getBoolean("game"+i+"_finished", false)){
				break;
			}
		}
		if(pebble != 1){
			ImageView btnNext = (ImageView)findViewById(R.id.nextGame); 
			btnNext.setVisibility(View.GONE);
		}
		
		txtGameMessage = (TextView)findViewById(R.id.idCongrats);
		txtGameMessage.setText(message);

		handler = new Handler();
		handler.postDelayed(updateTimeTask, 0);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	



	


	private Runnable updateTimeTask = new Runnable() {

		public void run() {
			updateScore();
			handler.postDelayed(this, 0);
			if(loop == 10){

				txtGameScore = (TextView)findViewById(R.id.idGameScore);
				txtTimeBonus = (TextView)findViewById(R.id.idBonusScore);
				txtTotalScore = (TextView)findViewById(R.id.idTotalScore);
				txtGameScore.setText(String.format("%04d", SCORE));
				txtTimeBonus.setText(String.format("%04d", BONUS));
				txtTotalScore.setText(String.format("%04d", TOTAL));

				handler.removeCallbacks(updateTimeTask);
			}
		}
	};


	private void updateScore() {

		score += incrementerScore;
		bonus += incrementerBonus;
		total = score + bonus;

		txtGameScore = (TextView)findViewById(R.id.idGameScore);
		txtTimeBonus = (TextView)findViewById(R.id.idBonusScore);
		txtTotalScore = (TextView)findViewById(R.id.idTotalScore);
		txtGameScore.setText(String.format("%04d", score));
		txtTimeBonus.setText(String.format("%04d", bonus));
		txtTotalScore.setText(String.format("%04d", total));

		loop++;



	}

	public void shareResult(View view){

		String imagePath = Environment.getExternalStorageDirectory() +
				File.separator + ".com.easwareapps.maspebble" + File.separator
				+ "result.png";
		File resultImage = new File(imagePath); 

		if(resultImage.exists()){
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/png");
			Uri uri = Uri.fromFile(resultImage);
			share.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
			share.putExtra(Intent.EXTRA_TEXT,   send);
			share.putExtra(Intent.EXTRA_STREAM, uri);
			startActivity(Intent.createChooser(share, getResources().getString(R.string.title_share_image)));
		}

	}

	public void restartGame(View view){
		restartGame();
	}
	
	private void restartGame() {
		// TODO Auto-generated method stub
		Intent databackIntent = new Intent();
		databackIntent.putExtra("action", MASPebbleActivity.ACTION_RESTART);
		setResult(Activity.RESULT_OK, databackIntent);
		finish();
	}

	public void nextGame(View view){
		SharedPreferences masPref = this.getSharedPreferences("com.easwareapps.maspebble", MODE_PRIVATE);
		int i=0;
		for(i=0; i<NO_BOARDS; i++){
			if(!masPref.getBoolean("game"+i+"_finished", false)){
				break;
			}
		}
		if(i==NO_BOARDS){
			i = 0;
		}
		SharedPreferences.Editor prefEditor = masPref.edit();
		prefEditor.putInt("board", i);
		prefEditor.commit();
		restartGame();
	}





}