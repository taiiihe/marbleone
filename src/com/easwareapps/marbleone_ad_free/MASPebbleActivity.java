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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easwareapps.marbleone_ad_free.R;

@SuppressLint("RtlHardcoded")
public class MASPebbleActivity extends Activity implements OnClickListener, Runnable{

	SharedPreferences masPref = null;
	SharedPreferences.Editor prefEditor = null;

	private static final int RIGHT = 0;
	private static final int LEFT = 1;
	private static final int DOWN = 2;
	private static final int UP = 3;

	int lastSelected[] = {-1, -1};
	int pebble[] = {-1, -1, 1, 1, 1, -1, -1, -1, -1, 1, 1, 1, -1, -1, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, 1, 1,
			1, -1, -1, -1, -1, 1, 1, 1, -1, -1};
	Integer moveDetails[][] = new Integer[32][3];
	int moveIndex = -1;

	int moves = 0;
	int noPebbles = 32;
	boolean gameEnd = false;
	boolean gameStarted = false;
	boolean gamePaused = false;
	Thread timerThread = null;
	int animatde[] = {-1,-1};
	int duration = 250;

	int sec = 0;
	int min = 0;
	int hr = 0;
	Handler handler;

	ImageView imgSlot[][] = new ImageView[7][7];
	TextView txtTime = null;

	LinearLayout adLayout;
	RelativeLayout mainLayout = null; 
	RelativeLayout slotLayout = null;

	Animation moveUp = null;
	Animation moveDown = null;
	Animation moveLeft = null;
	Animation moveRight = null;
	Animation throwAway = null;

	ImageView btnPause = null;
	ImageView btnMute = null;
	ImageView btnMusicMute = null;

	public static final int ACTION_EXIT = 0;
	public static final int ACTION_RESTART = 1;
	public static final int ACTION_UNDO = 2;
	public static final int ACTION_RESUME = 3;

	boolean isMoving = false;
	boolean imageSaved = false;

	MediaPlayer player = null;
	MediaPlayer musicPlayer = null;

	int pebbleResource;
	int selectedPebbleResource;
	
	int BLUE = 0;
	int RED = 3;
	
	int marbleColor = BLUE;
	int selectedMarbleColor = RED;



	String sounds[] = {"select.ogg", "move.ogg", "noh.ogg"}; 

	private static int SELECT = 0;
	private static int MOVE_SUCESS = 1;
	private static int MOVE_INVALID = 2;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		masPref = this.getSharedPreferences("com.easwareapps.maspebble", MODE_PRIVATE);

		initSlots();
		setClickListeners();

		RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams)imgSlot[0][2].getLayoutParams();

		int size = imgSlot[0][2].getLayoutParams().width;
		int space = rl.leftMargin;
		int distance = (size + space)*2;


		moveRight = new TranslateAnimation(-distance, 0, 0, 0);
		moveLeft = new TranslateAnimation(distance, 0, 0, 0);
		moveDown = new TranslateAnimation(0, 0, -distance, 0);
		moveUp = new TranslateAnimation(0, 0, distance, 0);
		throwAway  = new TranslateAnimation(0, 0, 1000, 1000); 

		moveUp.setDuration(duration);
		moveUp.setFillAfter(true);



		moveDown.setDuration(duration);
		moveDown.setFillAfter(true);


		moveLeft.setDuration(duration);
		moveLeft.setFillAfter(true);


		moveRight.setDuration(duration);
		moveRight.setFillAfter(true);

		throwAway.setDuration(duration);
		throwAway.setFillAfter(true);

		
		AssetFileDescriptor afd;
		try {
			afd = getAssets().openFd("bg-1.ogg");
			musicPlayer = new MediaPlayer();
			musicPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength()); 
			musicPlayer.prepare();
			musicPlayer.setLooping(true);
			if(masPref.getBoolean("music", true)){
				musicPlayer.start();
			}
			//musicPlayer.setVolume(0, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void restartGame(){

		moveIndex = 0;
		sec = 0;
		//		try {
		//			updateTimeTask.interrupt();
		//		} catch (Exception e) {
		//			// TODO: handle exception
		//		}
		handler.removeCallbacks(updateTimeTask);
		gameStarted = false;

		//handler.


		lastSelected[0] = -1;
		lastSelected[1] = -1;



		noPebbles = 32;

		gameEnd = false;
		gameStarted = false;
		gamePaused = false;

		int index = 0;
		pebble[index] = -1;
		index++;
		pebble[index] = -1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = -1;
		index++;
		pebble[index] = -1;
		index++;
		pebble[index] = -1;
		index++;
		pebble[index] = -1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = -1;
		index++;
		pebble[index] = -1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 0;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = -1;
		index++;
		pebble[index] = -1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = -1;
		index++;
		pebble[index] = -1;
		index++;
		pebble[index] = -1;
		index++;
		pebble[index] = -1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = 1;
		index++;
		pebble[index] = -1;
		index++;
		pebble[index] = -1;
		index++;

		isMoving = false;
		imageSaved = false;

		sec = 0;
		txtTime.setText(getResources().getString(R.string.time_zero));

		setContentView(R.layout.main_layout);
		initSlots();
		setClickListeners();







	}

	private void saveImage() {
		// TODO Auto-generated method stub
		try{
			invalidateAll();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams)imgSlot[0][2].getLayoutParams();

		int pebble = imgSlot[0][2].getLayoutParams().width;
		int space = rl.leftMargin;
		int size = (pebble + space)*8;


		Bitmap bitmap = Bitmap.createBitmap(size, size,
				Bitmap.Config.ARGB_8888);

		Canvas c = new Canvas(bitmap);
		c.drawColor(0xFFFFFFFF);
		mainLayout.draw(c);


		String dirPath = Environment.getExternalStorageDirectory() +
				File.separator + ".com.easwareapps.maspebble";
		File dir = new File(dirPath);
		if(!dir.exists()){
			dir.mkdir();
		}

		if(dir.exists()){
			String imagePath = dirPath + File.separator + "result.png";   


			OutputStream fout = null;
			File imageFile = new File(imagePath);

			try {
				fout = new FileOutputStream(imageFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
				fout.flush();
				fout.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		Intent intent = new Intent(getApplicationContext(), GameOver.class);
		int score = calculateScore(moveIndex + 1);
		intent.putExtra("score", score);
		intent.putExtra("pebble", noPebbles);
		int timebonus = (300-sec);
		if(timebonus < 0){
			timebonus = 0;
		}
		if(noPebbles == 1){
			intent.putExtra("message", getResources().getString(R.string.title_hatsoff));
			intent.putExtra("send", String.format(getResources().getString(R.string.title_finished), score + timebonus)
					+ getResources().getString(R.string.title_to_download));
		}else if(noPebbles < 4){
			intent.putExtra("message", getResources().getString(R.string.title_great));
			intent.putExtra("send", String.format(getResources().getString(R.string.title_game_end), score + timebonus)
					+ getResources().getString(R.string.title_to_download));
		}else{
			intent.putExtra("message", getResources().getString(R.string.title_gameover));
			intent.putExtra("send", String.format(getResources().getString(R.string.title_game_end), score + timebonus)
					+ getResources().getString(R.string.title_to_download));
		}
		intent.putExtra("bonus", timebonus);
		startActivityForResult(intent, 0);

	}

	private int calculateScore(int moves) {
		int score  = 0;
		int points[] = {0,10,20,50,100,500,1000,5000};
		int top[] = {0,5,10,15,20,25,30,31};
		int index = 1;
		while(index < top.length){
			if(moves > top[index]){
				score += (top[index] - top[index-1])*points[index];
			}else{
				score += (moves - top[index-1])*points[index];
				break;
			}
			index++;
		}
		return score;
	}

	private void invalidateAll(){

		for(int i=0;i<7;i++){
			for(int j=0;j<7;j++){
				if(pebble[getIndex(i, j)] ==- 1){
					continue;
				}
				try{
					imgSlot[i][j].invalidate();
				}catch (Exception e) {
					// TODO: handle exception
					Log.e("INVALIDATE", ""+i+"|"+j);
					e.printStackTrace();
				}
			}

		}
	}

	private void initSlots(){


		setContentView(R.layout.main_layout);
		marbleColor = masPref.getInt("pebble", BLUE);
		switch(marbleColor){
		case 0: pebbleResource = R.drawable.ic_pebble_blue;break;
		case 1: pebbleResource = R.drawable.ic_pebble_green;break;
		case 2: pebbleResource = R.drawable.ic_pebble_magenta;break;
		case 3: pebbleResource = R.drawable.ic_pebble_red;break;
		case 4: pebbleResource = R.drawable.ic_pebble_turquoise;break;
		default: pebbleResource = R.drawable.ic_pebble_blue;break;

		}
		selectedMarbleColor = masPref.getInt("selected_pebble", RED);
		if(marbleColor == selectedMarbleColor){
			selectedMarbleColor = (selectedMarbleColor+2)%5;
		}
		switch(selectedMarbleColor){
		case 0: selectedPebbleResource = R.drawable.ic_pebble_blue;break;
		case 1: selectedPebbleResource = R.drawable.ic_pebble_green;break;
		case 2: selectedPebbleResource = R.drawable.ic_pebble_magenta;break;
		case 3: selectedPebbleResource = R.drawable.ic_pebble_red;break;
		case 4: selectedPebbleResource = R.drawable.ic_pebble_turquoise;break;
		default: selectedPebbleResource = R.drawable.ic_pebble_red;break;

		}

		mainLayout = (RelativeLayout)findViewById(R.id.idGameBoard);
		slotLayout = (RelativeLayout)findViewById(R.id.idSlotBoard);
		imgSlot[0][2] = (ImageView)findViewById(R.id.idSlot02);
		imgSlot[0][3] = (ImageView)findViewById(R.id.idSlot03);
		imgSlot[0][4] = (ImageView)findViewById(R.id.idSlot04);
		imgSlot[1][2] = (ImageView)findViewById(R.id.idSlot12);
		imgSlot[1][3] = (ImageView)findViewById(R.id.idSlot13);
		imgSlot[1][4] = (ImageView)findViewById(R.id.idSlot14);
		imgSlot[2][0] = (ImageView)findViewById(R.id.idSlot20);
		imgSlot[2][1] = (ImageView)findViewById(R.id.idSlot21);
		imgSlot[2][2] = (ImageView)findViewById(R.id.idSlot22);
		imgSlot[2][3] = (ImageView)findViewById(R.id.idSlot23);
		imgSlot[2][4] = (ImageView)findViewById(R.id.idSlot24);
		imgSlot[2][5] = (ImageView)findViewById(R.id.idSlot25);
		imgSlot[2][6] = (ImageView)findViewById(R.id.idSlot26);
		imgSlot[3][0] = (ImageView)findViewById(R.id.idSlot30);
		imgSlot[3][1] = (ImageView)findViewById(R.id.idSlot31);
		imgSlot[3][2] = (ImageView)findViewById(R.id.idSlot32);
		imgSlot[3][3] = (ImageView)findViewById(R.id.idSlot33);
		imgSlot[3][4] = (ImageView)findViewById(R.id.idSlot34);
		imgSlot[3][5] = (ImageView)findViewById(R.id.idSlot35);
		imgSlot[3][6] = (ImageView)findViewById(R.id.idSlot36);
		imgSlot[4][0] = (ImageView)findViewById(R.id.idSlot40);
		imgSlot[4][1] = (ImageView)findViewById(R.id.idSlot41);
		imgSlot[4][2] = (ImageView)findViewById(R.id.idSlot42);
		imgSlot[4][3] = (ImageView)findViewById(R.id.idSlot43);
		imgSlot[4][4] = (ImageView)findViewById(R.id.idSlot44);
		imgSlot[4][5] = (ImageView)findViewById(R.id.idSlot45);
		imgSlot[4][6] = (ImageView)findViewById(R.id.idSlot46);
		imgSlot[5][2] = (ImageView)findViewById(R.id.idSlot52);
		imgSlot[5][3] = (ImageView)findViewById(R.id.idSlot53);
		imgSlot[5][4] = (ImageView)findViewById(R.id.idSlot54);
		imgSlot[6][2] = (ImageView)findViewById(R.id.idSlot62);
		imgSlot[6][3] = (ImageView)findViewById(R.id.idSlot63);
		imgSlot[6][4] = (ImageView)findViewById(R.id.idSlot64);

		imgSlot[0][2].setSoundEffectsEnabled(false);
		imgSlot[0][3].setSoundEffectsEnabled(false);
		imgSlot[0][4].setSoundEffectsEnabled(false);
		imgSlot[1][2].setSoundEffectsEnabled(false);
		imgSlot[1][3].setSoundEffectsEnabled(false);
		imgSlot[1][4].setSoundEffectsEnabled(false);
		imgSlot[2][0].setSoundEffectsEnabled(false);
		imgSlot[2][1].setSoundEffectsEnabled(false);
		imgSlot[2][2].setSoundEffectsEnabled(false);
		imgSlot[2][3].setSoundEffectsEnabled(false);
		imgSlot[2][4].setSoundEffectsEnabled(false);
		imgSlot[2][5].setSoundEffectsEnabled(false);
		imgSlot[2][6].setSoundEffectsEnabled(false);
		imgSlot[3][0].setSoundEffectsEnabled(false);
		imgSlot[3][1].setSoundEffectsEnabled(false);
		imgSlot[3][2].setSoundEffectsEnabled(false);
		imgSlot[3][3].setSoundEffectsEnabled(false);
		imgSlot[3][4].setSoundEffectsEnabled(false);
		imgSlot[3][5].setSoundEffectsEnabled(false);
		imgSlot[3][6].setSoundEffectsEnabled(false);
		imgSlot[4][0].setSoundEffectsEnabled(false);
		imgSlot[4][1].setSoundEffectsEnabled(false);
		imgSlot[4][2].setSoundEffectsEnabled(false);
		imgSlot[4][3].setSoundEffectsEnabled(false);
		imgSlot[4][4].setSoundEffectsEnabled(false);
		imgSlot[4][5].setSoundEffectsEnabled(false);
		imgSlot[4][6].setSoundEffectsEnabled(false);
		imgSlot[5][2].setSoundEffectsEnabled(false);
		imgSlot[5][3].setSoundEffectsEnabled(false);
		imgSlot[5][4].setSoundEffectsEnabled(false);
		imgSlot[6][2].setSoundEffectsEnabled(false);
		imgSlot[6][3].setSoundEffectsEnabled(false);
		imgSlot[6][4].setSoundEffectsEnabled(false);

		for(int i=0;i<7;i++){
			for(int j=0;j<7;j++){
				select(i, j, false);
			}
		}

		txtTime = (TextView)findViewById(R.id.idTxtTimeValue);
		btnPause = (ImageView)findViewById(R.id.idBtnPause);
		btnMute = (ImageView)findViewById(R.id.idBtnMute);
		btnMusicMute = (ImageView)findViewById(R.id.idBtnMusicMute);
		adLayout = (LinearLayout)findViewById(R.id.idAdLayout);

		if(masPref.getBoolean("sound", true)){
			btnMute.setImageResource(R.drawable.ic_sound);
		}else{
			btnMute.setImageResource(R.drawable.ic_mute);
		}
		if(masPref.getBoolean("music", true)){
			btnMusicMute.setImageResource(R.drawable.ic_music);
		}else{
			btnMusicMute.setImageResource(R.drawable.ic_music_mute);
		}

		btnPause.setSoundEffectsEnabled(false);
		btnMute.setSoundEffectsEnabled(false);
		btnMusicMute.setSoundEffectsEnabled(false);


	}

	private void setClickListeners(){
		imgSlot[0][2].setOnClickListener(this);
		imgSlot[0][3].setOnClickListener(this);
		imgSlot[0][4].setOnClickListener(this);
		imgSlot[1][2].setOnClickListener(this);
		imgSlot[1][3].setOnClickListener(this);
		imgSlot[1][4].setOnClickListener(this);
		imgSlot[2][0].setOnClickListener(this);
		imgSlot[2][1].setOnClickListener(this);
		imgSlot[2][2].setOnClickListener(this);
		imgSlot[2][3].setOnClickListener(this);
		imgSlot[2][4].setOnClickListener(this);
		imgSlot[2][5].setOnClickListener(this);
		imgSlot[2][6].setOnClickListener(this);
		imgSlot[3][0].setOnClickListener(this);
		imgSlot[3][1].setOnClickListener(this);
		imgSlot[3][2].setOnClickListener(this);
		imgSlot[3][3].setOnClickListener(this);
		imgSlot[3][4].setOnClickListener(this);
		imgSlot[3][5].setOnClickListener(this);
		imgSlot[3][6].setOnClickListener(this);
		imgSlot[4][0].setOnClickListener(this);
		imgSlot[4][1].setOnClickListener(this);
		imgSlot[4][2].setOnClickListener(this);
		imgSlot[4][3].setOnClickListener(this);
		imgSlot[4][4].setOnClickListener(this);
		imgSlot[4][5].setOnClickListener(this);
		imgSlot[4][6].setOnClickListener(this);
		imgSlot[5][2].setOnClickListener(this);
		imgSlot[5][3].setOnClickListener(this);
		imgSlot[5][4].setOnClickListener(this);
		imgSlot[6][2].setOnClickListener(this);
		imgSlot[6][3].setOnClickListener(this);
		imgSlot[6][4].setOnClickListener(this);

		btnPause.setOnClickListener(this);

	}


	private void playMoveSound(int index){
		if(masPref.getBoolean("sound", true)){
			if(player != null){
				try{
					player.stop();
				}catch (Exception e) {
					// TODO: handle exception
				}
				player.release();
			}
			AssetFileDescriptor afd;
			try {
				afd = getAssets().openFd(sounds[index]);
				player = new MediaPlayer();
				player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength()); 
				player.prepare();
				player.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		changeColorIfneeded();
		if(data == null){
			return;
		}
		Log.d("Activity", "Finished");
		int action = data.getIntExtra("action", 0);
		switch (action) {
		case ACTION_EXIT:
			finish();
			break;
		case ACTION_RESTART:
			try{
				restartGame();
			}catch (Exception e) {
				// TODO: handle exception
				Log.d("Exe", e.toString());
			}
			break;
		case ACTION_UNDO:
			gamePaused = false;
			undoMove();
			break;
		case ACTION_RESUME:
			gamePaused = false;
			break;
		default:
			break;
		}


	}

	private void changeColorIfneeded() {
		// TODO Auto-generated method stub
		if(masPref.getInt("pebble", BLUE) == marbleColor
			&& masPref.getInt("selected_pebble", RED) == selectedMarbleColor){
			return;
		}
		
		marbleColor = masPref.getInt("pebble", BLUE);
		switch(marbleColor){
		case 0: pebbleResource = R.drawable.ic_pebble_blue;break;
		case 1: pebbleResource = R.drawable.ic_pebble_green;break;
		case 2: pebbleResource = R.drawable.ic_pebble_magenta;break;
		case 3: pebbleResource = R.drawable.ic_pebble_red;break;
		case 4: pebbleResource = R.drawable.ic_pebble_turquoise;break;
		default: pebbleResource = R.drawable.ic_pebble_blue;break;

		}
		
		selectedMarbleColor = masPref.getInt("selected_pebble", RED);
		if(marbleColor == selectedMarbleColor){
			selectedMarbleColor = (selectedMarbleColor+2)%5;
		}
		switch(selectedMarbleColor){
		case 0: selectedPebbleResource = R.drawable.ic_pebble_blue;break;
		case 1: selectedPebbleResource = R.drawable.ic_pebble_green;break;
		case 2: selectedPebbleResource = R.drawable.ic_pebble_magenta;break;
		case 3: selectedPebbleResource = R.drawable.ic_pebble_red;break;
		case 4: selectedPebbleResource = R.drawable.ic_pebble_turquoise;break;
		default: selectedPebbleResource = R.drawable.ic_pebble_red;break;

		}
		
		for(int i=0;i<7;i++){
			for(int j=0;j<7;j++){
				select(i, j, false);
			}
		}

		
	}

	private void undoMove(){
		if(moveIndex < 0){
			return;
		}
		moves--;


		int i = (int)moveDetails[moveIndex][1]/7;
		int j = (int)moveDetails[moveIndex][1]%7;
		imgSlot[i][j].setImageResource(R.drawable.ic_no_pebble);
		pebble[getIndex(i, j)] = 0;
		int k = i;
		int l = i;

		i = (int)moveDetails[moveIndex][2]/7;
		j = (int)moveDetails[moveIndex][2]%7;
		imgSlot[i][j].setImageResource(pebbleResource);
		imgSlot[i][j].setVisibility(View.VISIBLE);
		pebble[getIndex(i, j)] = 1;


		i = (int)moveDetails[moveIndex][0]/7;
		j = (int)moveDetails[moveIndex][0]%7;
		animatde[0] = i;
		animatde[1] = j;


		imgSlot[i][j].setImageResource(pebbleResource);
		imgSlot[i][j].bringToFront();
		Log.d("UNDO", getMove(k, l, i, j)+"");
		switch (getMove(k, l, i, j)) {
		case UP:
			imgSlot[i][j].startAnimation(moveUp);
			break;
		case DOWN:
			imgSlot[i][j].startAnimation(moveDown);
			break;
		case LEFT:
			imgSlot[i][j].startAnimation(moveLeft);
			break;
		case RIGHT:
			imgSlot[i][j].startAnimation(moveRight);
			break;

		default:
			break;
		}


		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					Thread.sleep(duration);

				}catch(InterruptedException e){

				}
				try{
					imgSlot[animatde[0]][animatde[1]].clearAnimation();
					imgSlot[animatde[0]][animatde[1]].invalidate();

				}catch(Exception e){

				}
				try{
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							invalidateAll();
						}
					});
					
					mainLayout.invalidate();
				}catch(Exception e){

				}
			}
		}).start();
		pebble[getIndex(i, j)] = 1;

		moveIndex--;

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view == btnPause){
			gamePaused = true;
			if(gamePaused){
				Intent i = new Intent(this, PauseGame.class);
				MASPebbleActivity.this.startActivityForResult(i, 0);
				return;
			}
		}

		if(gameEnd){
			return;
		}else if(!gameStarted){
			gameStarted = true;
			handler = new Handler();
			handler.removeCallbacks(updateTimeTask);
			handler.postDelayed(updateTimeTask, 0);

		}
		for(int i=0; i<7; i++){
			for(int j=0; j<7; j++){
				if(view == imgSlot[i][j]){

					if(lastSelected[0] == -1 && lastSelected[1] == -1){
						if(pebble[getIndex(i, j)] == 0 ){
							playMoveSound(MOVE_INVALID);
							lastSelected[0] = -1;
							lastSelected[1] = -1;
							return;
						}
						playMoveSound(SELECT);
						lastSelected[0] = i;
						lastSelected[1] = j;
						select(i, j, true);


					}else if(lastSelected[0] == i && lastSelected[1] == j){
						lastSelected[0] = -1;
						lastSelected[1] = -1;
						select(i, j, false);
					}else{
						int move = isMoveAllowed(i,j, lastSelected[0], lastSelected[1]);
						if( move >=0 ){
							playMoveSound(MOVE_SUCESS);
							isMoving = true;
							proceedMove(move);
							moveIndex++;
							moveDetails[moveIndex][0] = Integer.valueOf(lastSelected[0]*7+lastSelected[1]);
							moveDetails[moveIndex][1] = Integer.valueOf(i*7+j);
							moves++;
							Log.d("pebble_resource", pebbleResource+"");
							imgSlot[i][j].setImageResource(pebbleResource);
							imgSlot[i][j].setImageLevel(1);
							imgSlot[i][j].bringToFront();
							switch (move) {
							case UP: imgSlot[i][j].startAnimation(moveUp);
							moveDetails[moveIndex][2] = Integer.valueOf(lastSelected[0]*7 + lastSelected[1]-1);
							break;
							case DOWN: imgSlot[i][j].startAnimation(moveDown);
							moveDetails[moveIndex][2] = Integer.valueOf(lastSelected[0]*7 + lastSelected[1]+1);
							break;
							case LEFT: imgSlot[i][j].startAnimation(moveLeft);
							moveDetails[moveIndex][2] = Integer.valueOf(((lastSelected[0]-1)*7) + lastSelected[1]);
							break;
							case RIGHT: imgSlot[i][j].startAnimation(moveRight);
							moveDetails[moveIndex][2] = Integer.valueOf(((lastSelected[0]+1)*7) + lastSelected[1]);
							break;
							default:break;
							}
							animatde[0] = i;
							animatde[1] = j;

							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									try{
										Thread.sleep(duration);

									}catch(InterruptedException e){

									}
									try{
										imgSlot[animatde[0]][animatde[1]].clearAnimation();
										imgSlot[animatde[0]][animatde[1]].invalidate();

									}catch(Exception e){

									}try{
										try{
											runOnUiThread(new Runnable() {
												
												@Override
												public void run() {
													// TODO Auto-generated method stub
													invalidateAll();
													mainLayout.invalidate();
													slotLayout.invalidate();
												}
											});
											
										}catch(Exception e){

										}

									}catch(Exception e){

									}
									isMoving = false;
								}
							}).start();
							pebble[getIndex(i, j)] = 1;
							lastSelected[0] = -1;
							lastSelected[1] = -1;
							select(i, j, false);
							noPebbles--;
							if(noPebbles == 1){
								gameEnd = true;
							}else if(checkDeadLock()){
								Toast.makeText(getApplicationContext(),
										"Oops!. No further move possible",
										Toast.LENGTH_SHORT).show();
								gameEnd = true;
							}

							return;
						}
						if(pebble[getIndex(i, j)] == 1){
							playMoveSound(SELECT);
							select(lastSelected[0], lastSelected[1], false);
							lastSelected[0] = i;
							lastSelected[1] = j;
							select(i, j, true);
						}else{
							playMoveSound(MOVE_INVALID);
						}

					}
				}
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		gamePaused = true;
		Intent i = new Intent(this, PauseGame.class);
		MASPebbleActivity.this.startActivityForResult(i, 0);
		return;


	}

	private void select(int i, int j, boolean type) {
		// TODO Auto-generated method stub

		if(pebble[getIndex(i, j)] == 1){
			if(type)	imgSlot[i][j].setImageResource(selectedPebbleResource);
			else		imgSlot[i][j].setImageResource(pebbleResource);
		}

	}

	private void proceedMove(int move) {
		// TODO Auto-generated method stub
		switch (move) {
		case RIGHT:

			throwAwayPebble(lastSelected[0]+1, lastSelected[1]);
			imgSlot[lastSelected[0]][lastSelected[1]].setImageResource(R.drawable.ic_no_pebble);
			pebble[getIndex(lastSelected[0]+1, lastSelected[1])] = 0;
			pebble[getIndex(lastSelected[0], lastSelected[1])] = 0;
			break;
		case LEFT:
			throwAwayPebble(lastSelected[0]-1, lastSelected[1]);
			imgSlot[lastSelected[0]][lastSelected[1]].setImageResource(R.drawable.ic_no_pebble);
			pebble[getIndex(lastSelected[0]-1, lastSelected[1])] = 0;
			pebble[getIndex(lastSelected[0], lastSelected[1])] = 0;
			break;
		case DOWN:
			throwAwayPebble(lastSelected[0], lastSelected[1]+1);
			imgSlot[lastSelected[0]][lastSelected[1]].setImageResource(R.drawable.ic_no_pebble);
			pebble[getIndex(lastSelected[0], lastSelected[1]+1)] = 0;
			pebble[getIndex(lastSelected[0], lastSelected[1])] = 0;
			break;
		case UP:
			throwAwayPebble(lastSelected[0], lastSelected[1]-1);
			imgSlot[lastSelected[0]][lastSelected[1]].setImageResource(R.drawable.ic_no_pebble);
			pebble[getIndex(lastSelected[0], lastSelected[1]-1)] = 0;
			pebble[getIndex(lastSelected[0], lastSelected[1])] = 0;
			break;
		default:
			break;
		}



	}

	private void throwAwayPebble(final int i,final int j){
		imgSlot[i][j].setImageResource(R.drawable.ic_no_pebble);
	}

	private int isMoveAllowed(int i, int j, int k, int l) {
		// TODO Auto-generated method stub
		if(pebble[getIndex(i, j)] != 0)	return -1;

		if(i-2 == k && j==l && pebble[getIndex(i-1, j)] ==1 ){
			return RIGHT;
		}else if(i+2 == k && j==l && pebble[getIndex(i+1, j)] ==1 ){
			return LEFT;
		}else if(i == k && j-2==l && pebble[getIndex(i, j-1)] ==1 ){
			return DOWN;
		}else if(i == k && j+2==l && pebble[getIndex(i, j+1)] ==1 ){
			return UP;
		}
		return -1;
	}

	private int getMove(int i, int j, int k, int l) {

		if(i-2 == k && j==l){
			return LEFT;
		}else if(i+2 == k && j==l){
			return RIGHT;
		}else if(i == k && j-2==l){
			return UP;
		}else if(i == k && j+2==l){
			return DOWN;
		}
		return -1;

	}

	private int getIndex(int i, int j) {
		// TODO Auto-generated method stub
		return i*7+j;
	}

	private boolean checkDeadLock(){
		for(int i=0; i<pebble.length;i++){
			if(pebble[i] == 1){
				if(checkMoveExsistForPebble((int)(i/7),(int)(i%7))){
					return false;
				}
			}
		}
		gameEnd = true;
		return true;
	}

	private boolean checkMoveExsistForPebble(int i, int j) {
		// TODO Auto-generated method stub
		if(i+2 < 7)
			if(isMoveAllowed(i+2, j, i, j) >= 0)	return true;

		if(i-2 >= 0)
			if(isMoveAllowed(i-2, j, i, j) >= 0)	return true;

		if(j+2 < 7)
			if(isMoveAllowed(i, j+2, i, j) >= 0)	return true;

		if(j-2 >= 0)
			if(isMoveAllowed(i, j-2, i, j) >= 0)	return true;
		return false;
	}

	public void run() {

		// TODO Auto-generated method stub
		while(!gameEnd){
			if(gameStarted && !gamePaused){
				try{
					Thread.sleep(1000);
				}catch(Exception e){

				}
				sec++;
				txtTime.setText(sec+"");
			}
		}	
	}

	private Runnable updateTimeTask = new Runnable() {
		public void run() {
			updateTime();
			handler.postDelayed(this, 1000);
			if(gameEnd && imageSaved){
				handler.removeCallbacks(updateTimeTask);
			}

		}
	};

	private void updateTime() {
		if(gamePaused)	return;
		String strTime="";
		if(hr < 10){
			strTime += "0";
		}
		strTime += hr + ":";
		if(min < 10){
			strTime += "0";
		}strTime += min + ":";
		if(sec < 10){
			strTime += "0";
		}
		strTime += sec;

		final TextView txtTime = (TextView) findViewById(R.id.idTxtTimeValue);
		txtTime.setText(strTime);
		sec++;
		if(sec==60){
			sec = 0;
			min++;
			if(min==60){
				min=0;
				hr++;
			}
		}
		if(gameEnd  && !isMoving ){
			saveImage();
			imageSaved = true;
			

		}
		if(gameEnd)	Log.d("Inside update time", strTime);
	}

	public void toggleSound(View view){
		prefEditor = masPref.edit();
		if(masPref.getBoolean("sound", true)){
			prefEditor.putBoolean("sound", false);
			prefEditor.commit();
			btnMute.setImageResource(R.drawable.ic_mute);
		}else{
			prefEditor.putBoolean("sound", true);
			prefEditor.commit();
			btnMute.setImageResource(R.drawable.ic_sound);
		}
	}

	public void toggleMusic(View view){
		prefEditor = masPref.edit();
		if(masPref.getBoolean("music", true)){
			prefEditor.putBoolean("music", false);
			prefEditor.commit();
			musicPlayer.pause();
			btnMusicMute.setImageResource(R.drawable.ic_music_mute);
		}else{
			prefEditor.putBoolean("music", true);
			prefEditor.commit();
			musicPlayer.start();
			btnMusicMute.setImageResource(R.drawable.ic_music);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		musicPlayer.stop();
		musicPlayer.release();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		musicPlayer.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(masPref.getBoolean("music", true)){
			musicPlayer.start();
		}
		super.onResume();
	}


}
