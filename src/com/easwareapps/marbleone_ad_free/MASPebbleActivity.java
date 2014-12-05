package com.easwareapps.marbleone_ad_free;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;




import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
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
	
	int DEFAULT = 0;
	int board = 0;
	int lastSelected[] = {-1, -1};
	
	
	private int[] getGame0(){

		int board[] = { 
				-2, -2, -2, -2, -2, -2, -2, -2, -2,
				-2, -1, -1,  0,  0,  0, -1, -1, -2,
				-2, -1, -1,  0,  1,  0, -1, -1, -2,
				-2,  0,  0,  0,  1,  0,  0,  0, -2,
				-2,  0,  1,  1,  1,  1,  1,  0, -2,
				-2,  0,  0,  0,  1,  0,  0,  0, -2,
				-2, -1, -1,  0,  1,  0, -1, -1, -2,
				-2, -1, -1,  0,  0,  0, -1, -1, -2,
				-2, -2, -2, -2, -2, -2, -2, -2, -2,
		};
		return board;
	}
	
	private int[] getGame1(){

		int board[] = { 
				-2, -2, -2, -2, -2, -2, -2, -2, -2,
				-2, -1, -1,  1,  1,  1, -1, -1, -2,
				-2, -1, -1,  1,  1,  0, -1, -1, -2,
				-2,  0,  0,  1,  1,  0,  0,  0, -2,
				-2,  0,  0,  1,  0,  0,  0,  0, -2,
				-2,  0,  0,  0,  0,  0,  0,  0, -2,
				-2, -1, -1,  0,  0,  0, -1, -1, -2,
				-2, -1, -1,  0,  0,  0, -1, -1, -2,
				-2, -2, -2, -2, -2, -2, -2, -2, -2,
		};
		return board;
	}
	
	private int[] getGame2(){

		int board[] = { 
				-2, -2, -2, -2, -2, -2, -2, -2, -2,
				-2, -1, -1,  1,  1,  1, -1, -1, -2,
				-2, -1, -1,  1,  1,  1, -1, -1, -2,
				-2,  0,  0,  1,  1,  1,  0,  0, -2,
				-2,  0,  0,  1,  0,  1,  0,  0, -2,
				-2,  0,  0,  0,  0,  0,  0,  0, -2,
				-2, -1, -1,  0,  0,  0, -1, -1, -2,
				-2, -1, -1,  0,  0,  0, -1, -1, -2,
				-2, -2, -2, -2, -2, -2, -2, -2, -2,
		};
		return board;
	}
	
	
	private int[] getGame3(){ 
		int board[] = {
				-2, -2, -2,-2,-2,-2, -2, -2, -2,
				-2, -1, -1, 1, 1, 1, -1, -1, -2, 
				-2, -1, -1, 1, 1, 1, -1, -1, -2,
				-2,  1,  1, 1, 1, 1,  1,  1, -2,
				-2,  1,  1, 1, 0, 1,  1,  1, -2,
				-2,  1,  1, 1, 1, 1,  1,  1, -2,
				-2, -1, -1, 1, 1, 1, -1, -1, -2,
				-2, -1, -1, 1, 1, 1, -1, -1, -2,
				-2, -2, -2,-2,-2,-2, -2, -2, -2
				
		};
		effectiveRows = 8;
		return board;
	}
	private int[] getGame4(){ 
		int board[] = {
				-2, -2, -2,-2,-2,-2, -2, -2, -2,
				-2, -1, -1, 1, 1, 1, -1, -1, -2, 
				-2, -1,  1, 1, 1, 1,  1, -1, -2,
				-2,  0,  1, 1, 1, 1,  1,  1, -2,
				-2,  1,  1, 1, 1, 1,  1,  1, -2,
				-2,  1,  1, 1, 1, 1,  1,  1, -2,
				-2, -1,  1, 1, 1, 1,  1, -1, -2,
				-2, -1, -1, 1, 1, 1, -1, -1, -2,
				-2, -2, -2,-2,-2,-2, -2, -2, -2
		};
		effectiveRows = 8;
		return board;
	}
	
	private int[] getGame5(){

		int board[] = { 
				-2, -2, -2, -2, -2, -2, -2, -2, -2,
				-1, -1, -1,  1,  1,  1, -1, -1, -2,
				-1, -1, -1,  1,  1,  1, -1, -1, -2,
				 1,  1,  1,  1,  1,  1,  1,  1, -2,
				 1,  1,  1,  1,  0,  1,  1,  1, -2,
				 1,  1,  1,  1,  1,  1,  1,  1, -2,
				-1, -1, -1,  1,  1,  1, -1, -1, -2,
				-1, -1, -1,  1,  1,  1, -1, -1, -2,
				-1, -1, -1,  1,  1,  1, -1, -1, -2,
				
		};
		effectiveRows = 10;
		return board;
	}
	
	private int[] getGame6(){

		int board[] = { 
				-1, -1, -1,  1,  1,  1, -1, -1, -1, 
				-1, -1, -1,  1,  1,  1, -1, -1, -1, 
				-1, -1, -1,  1,  1,  1, -1, -1, -1, 
				 1,  1,  1,  1,  1,  1,  1,  1,  1, 
				 1,  1,  1,  1,  0,  1,  1,  1,  1,
				 1,  1,  1,  1,  1,  1,  1,  1,  1,
				-1, -1, -1,  1,  1,  1, -1, -1, -1,
				-1, -1, -1,  1,  1,  1, -1, -1, -1,
				-1, -1, -1,  1,  1,  1, -1, -1, -1,
		};
		effectiveRows = 10;
		return board;
	}
	
	
	private int[] getGame7(){

		int board[] = { 
				-1, -1, -1, -1,  1, -1, -1, -1, -1, 
				-1, -1, -1,  1,  1,  1, -1, -1, -1, 
				-1, -1,  1,  1,  1,  1,  1, -1, -1, 
				-1,  1,  1,  1,  1,  1,  1,  1, -1, 
				 1,  1,  1,  1,  1,  1,  1,  1,  1,
				-1,  1,  1,  1,  0,  1,  1,  1, -1,
				-1, -1,  1,  1,  1,  1,  1, -1, -1,
				-1, -1, -1,  1,  1,  1, -1, -1, -1,
				-1, -1, -1, -1,  1, -1, -1, -1, -1,
		};
		effectiveRows = 10;
		return board;
	}
	
	


	int pebble[];
	
	
	Integer moveDetails[][] = new Integer[100][3];
	int moveIndex = -1;

	int moves = 0;
	int noPebbles = 0;
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
	int cols = 9;
	int rows = 9;
	int effectiveRows = rows;

	ImageView imgPebble[][] = new ImageView[rows][cols];
	ImageView imgSlot[][] = new ImageView[rows][cols];
	TextView txtTime = null;

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
		int newboard = masPref.getInt("board", DEFAULT);
		board = newboard;
		switch(newboard){
		case 0: pebble = getGame0();break;
		case 1: pebble = getGame1();break;
		case 2: pebble = getGame2();break;
		case 3: pebble = getGame3();break;
		case 4: pebble = getGame4();break;
		case 5: pebble = getGame5();break;
		case 6: pebble = getGame6();break;
		case 7: pebble = getGame7();break;
		default: pebble = getGame0();break;
		}
		noPebbles = getPebblesInBoard();
		

		initSlots();
		setClickListeners();

		RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams)imgPebble[0][2].getLayoutParams();

		int size = imgPebble[0][2].getLayoutParams().width;
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

	private int getMinimumResolution() {
		// TODO Auto-generated method stub
		LinearLayout header = (LinearLayout)findViewById(R.id.idHeaderLayout);
		int hh = header.getLayoutParams().height;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		final int height = dm.heightPixels - hh;
		final int width = dm.widthPixels;
		if(height < width){
			return height;
		}
		return width;
	}
	public void restartGame(){

		moveIndex = 0;
		sec = 0;
		try{
			handler.removeCallbacks(updateTimeTask);
		}catch(Exception e){
			
		}
		gameStarted = false;

		//handler.


		lastSelected[0] = -1;
		lastSelected[1] = -1;
		
		
		SharedPreferences masPref = this.getSharedPreferences("com.easwareapps.maspebble", MODE_PRIVATE);
		int newboard = masPref.getInt("board", DEFAULT);
		System.out.println(newboard+"=index\n");
		switch(newboard){
		case 0: pebble = getGame0();break;
		case 1: pebble = getGame1();break;
		case 2: pebble = getGame2();break;
		case 3: pebble = getGame3();break;
		case 4: pebble = getGame4();break;
		case 5: pebble = getGame5();break;
		case 6: pebble = getGame6();break;
		case 7: pebble = getGame7();break;
		default: pebble = getGame0();break;
		}
		noPebbles = getPebblesInBoard();
		
		

		gameEnd = false;
		gameStarted = false;
		gamePaused = false;


		isMoving = false;
		imageSaved = false;

		sec = 0;
		txtTime.setText(getResources().getString(R.string.time_zero));

		setContentView(R.layout.main_layout);
		initSlots();
		setClickListeners();







	}

	private int getPebblesInBoard() {
		// TODO Auto-generated method stub
		int no = 0;
		for(int i = 0; i < pebble.length; i++){
			if(pebble[i] == 1){
				no += 1;
			}
		}
		return no;
	}

	private void saveImage() {
		// TODO Auto-generated method stub
		try{
			invalidateAll();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams)imgPebble[0][2].getLayoutParams();

		int pebble = imgPebble[0][2].getLayoutParams().width;
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
		int points[] = {0,10,20,50,100,500,1000,5000,7500,10000};
		int top[] = {0,5,10,15,20,25,30,35,40,45};
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

		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				if(pebble[getIndex(i, j)] ==- 1){
					continue;
				}
				try{
					imgPebble[i][j].invalidate();
				}catch (Exception e) {
					// TODO: handle exception
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
		slotLayout = (RelativeLayout)findViewById(R.id.idPebbleBoard);
		//		for(int i=0;i<7;i++){
		//			for(int j=0;j<7;j++){
		//				String s = "idPebble"+i+j;
		//				imgPebble[0][2] = (ImageView)findViewById(R.id.idPebble+i+j);
		//			}
		//		}


		imgPebble[0][0] = (ImageView)findViewById(R.id.idPebble00);
		imgPebble[0][1] = (ImageView)findViewById(R.id.idPebble01);
		imgPebble[0][2] = (ImageView)findViewById(R.id.idPebble02);
		imgPebble[0][3] = (ImageView)findViewById(R.id.idPebble03);
		imgPebble[0][4] = (ImageView)findViewById(R.id.idPebble04);
		imgPebble[0][5] = (ImageView)findViewById(R.id.idPebble05);
		imgPebble[0][6] = (ImageView)findViewById(R.id.idPebble06);
		imgPebble[0][7] = (ImageView)findViewById(R.id.idPebble07);
		imgPebble[0][8] = (ImageView)findViewById(R.id.idPebble08);
		imgPebble[1][0] = (ImageView)findViewById(R.id.idPebble10);
		imgPebble[1][1] = (ImageView)findViewById(R.id.idPebble11);
		imgPebble[1][2] = (ImageView)findViewById(R.id.idPebble12);
		imgPebble[1][3] = (ImageView)findViewById(R.id.idPebble13);
		imgPebble[1][4] = (ImageView)findViewById(R.id.idPebble14);
		imgPebble[1][5] = (ImageView)findViewById(R.id.idPebble15);
		imgPebble[1][6] = (ImageView)findViewById(R.id.idPebble16);
		imgPebble[1][7] = (ImageView)findViewById(R.id.idPebble17);
		imgPebble[1][8] = (ImageView)findViewById(R.id.idPebble18);
		imgPebble[2][0] = (ImageView)findViewById(R.id.idPebble20);
		imgPebble[2][1] = (ImageView)findViewById(R.id.idPebble21);
		imgPebble[2][2] = (ImageView)findViewById(R.id.idPebble22);
		imgPebble[2][3] = (ImageView)findViewById(R.id.idPebble23);
		imgPebble[2][4] = (ImageView)findViewById(R.id.idPebble24);
		imgPebble[2][5] = (ImageView)findViewById(R.id.idPebble25);
		imgPebble[2][6] = (ImageView)findViewById(R.id.idPebble26);
		imgPebble[2][7] = (ImageView)findViewById(R.id.idPebble27);
		imgPebble[2][8] = (ImageView)findViewById(R.id.idPebble28);
		imgPebble[3][0] = (ImageView)findViewById(R.id.idPebble30);
		imgPebble[3][1] = (ImageView)findViewById(R.id.idPebble31);
		imgPebble[3][2] = (ImageView)findViewById(R.id.idPebble32);
		imgPebble[3][3] = (ImageView)findViewById(R.id.idPebble33);
		imgPebble[3][4] = (ImageView)findViewById(R.id.idPebble34);
		imgPebble[3][5] = (ImageView)findViewById(R.id.idPebble35);
		imgPebble[3][6] = (ImageView)findViewById(R.id.idPebble36);
		imgPebble[3][7] = (ImageView)findViewById(R.id.idPebble37);
		imgPebble[3][8] = (ImageView)findViewById(R.id.idPebble38);
		imgPebble[4][0] = (ImageView)findViewById(R.id.idPebble40);
		imgPebble[4][1] = (ImageView)findViewById(R.id.idPebble41);
		imgPebble[4][2] = (ImageView)findViewById(R.id.idPebble42);
		imgPebble[4][3] = (ImageView)findViewById(R.id.idPebble43);
		imgPebble[4][4] = (ImageView)findViewById(R.id.idPebble44);
		imgPebble[4][5] = (ImageView)findViewById(R.id.idPebble45);
		imgPebble[4][6] = (ImageView)findViewById(R.id.idPebble46);
		imgPebble[4][7] = (ImageView)findViewById(R.id.idPebble47);
		imgPebble[4][8] = (ImageView)findViewById(R.id.idPebble48);
		imgPebble[5][0] = (ImageView)findViewById(R.id.idPebble50);
		imgPebble[5][1] = (ImageView)findViewById(R.id.idPebble51);
		imgPebble[5][2] = (ImageView)findViewById(R.id.idPebble52);
		imgPebble[5][3] = (ImageView)findViewById(R.id.idPebble53);
		imgPebble[5][4] = (ImageView)findViewById(R.id.idPebble54);
		imgPebble[5][5] = (ImageView)findViewById(R.id.idPebble55);
		imgPebble[5][6] = (ImageView)findViewById(R.id.idPebble56);
		imgPebble[5][7] = (ImageView)findViewById(R.id.idPebble57);
		imgPebble[5][8] = (ImageView)findViewById(R.id.idPebble58);
		imgPebble[6][0] = (ImageView)findViewById(R.id.idPebble60);
		imgPebble[6][1] = (ImageView)findViewById(R.id.idPebble61);
		imgPebble[6][2] = (ImageView)findViewById(R.id.idPebble62);
		imgPebble[6][3] = (ImageView)findViewById(R.id.idPebble63);
		imgPebble[6][4] = (ImageView)findViewById(R.id.idPebble64);
		imgPebble[6][5] = (ImageView)findViewById(R.id.idPebble65);
		imgPebble[6][6] = (ImageView)findViewById(R.id.idPebble66);
		imgPebble[6][7] = (ImageView)findViewById(R.id.idPebble67);
		imgPebble[6][8] = (ImageView)findViewById(R.id.idPebble68);
		imgPebble[7][0] = (ImageView)findViewById(R.id.idPebble70);
		imgPebble[7][1] = (ImageView)findViewById(R.id.idPebble71);
		imgPebble[7][2] = (ImageView)findViewById(R.id.idPebble72);
		imgPebble[7][3] = (ImageView)findViewById(R.id.idPebble73);
		imgPebble[7][4] = (ImageView)findViewById(R.id.idPebble74);
		imgPebble[7][5] = (ImageView)findViewById(R.id.idPebble75);
		imgPebble[7][6] = (ImageView)findViewById(R.id.idPebble76);
		imgPebble[7][7] = (ImageView)findViewById(R.id.idPebble77);
		imgPebble[7][8] = (ImageView)findViewById(R.id.idPebble78);
		imgPebble[8][0] = (ImageView)findViewById(R.id.idPebble80);
		imgPebble[8][1] = (ImageView)findViewById(R.id.idPebble81);
		imgPebble[8][2] = (ImageView)findViewById(R.id.idPebble82);
		imgPebble[8][3] = (ImageView)findViewById(R.id.idPebble83);
		imgPebble[8][4] = (ImageView)findViewById(R.id.idPebble84);
		imgPebble[8][5] = (ImageView)findViewById(R.id.idPebble85);
		imgPebble[8][6] = (ImageView)findViewById(R.id.idPebble86);
		imgPebble[8][7] = (ImageView)findViewById(R.id.idPebble87);
		imgPebble[8][8] = (ImageView)findViewById(R.id.idPebble88);
		
		imgSlot[0][0] = (ImageView)findViewById(R.id.idSlot00);
		imgSlot[0][1] = (ImageView)findViewById(R.id.idSlot01);
		imgSlot[0][2] = (ImageView)findViewById(R.id.idSlot02);
		imgSlot[0][3] = (ImageView)findViewById(R.id.idSlot03);
		imgSlot[0][4] = (ImageView)findViewById(R.id.idSlot04);
		imgSlot[0][5] = (ImageView)findViewById(R.id.idSlot05);
		imgSlot[0][6] = (ImageView)findViewById(R.id.idSlot06);
		imgSlot[0][7] = (ImageView)findViewById(R.id.idSlot07);
		imgSlot[0][8] = (ImageView)findViewById(R.id.idSlot08);
		imgSlot[1][0] = (ImageView)findViewById(R.id.idSlot10);
		imgSlot[1][1] = (ImageView)findViewById(R.id.idSlot11);
		imgSlot[1][2] = (ImageView)findViewById(R.id.idSlot12);
		imgSlot[1][3] = (ImageView)findViewById(R.id.idSlot13);
		imgSlot[1][4] = (ImageView)findViewById(R.id.idSlot14);
		imgSlot[1][5] = (ImageView)findViewById(R.id.idSlot15);
		imgSlot[1][6] = (ImageView)findViewById(R.id.idSlot16);
		imgSlot[1][7] = (ImageView)findViewById(R.id.idSlot17);
		imgSlot[1][8] = (ImageView)findViewById(R.id.idSlot18);
		imgSlot[2][0] = (ImageView)findViewById(R.id.idSlot20);
		imgSlot[2][1] = (ImageView)findViewById(R.id.idSlot21);
		imgSlot[2][2] = (ImageView)findViewById(R.id.idSlot22);
		imgSlot[2][3] = (ImageView)findViewById(R.id.idSlot23);
		imgSlot[2][4] = (ImageView)findViewById(R.id.idSlot24);
		imgSlot[2][5] = (ImageView)findViewById(R.id.idSlot25);
		imgSlot[2][6] = (ImageView)findViewById(R.id.idSlot26);
		imgSlot[2][7] = (ImageView)findViewById(R.id.idSlot27);
		imgSlot[2][8] = (ImageView)findViewById(R.id.idSlot28);
		imgSlot[3][0] = (ImageView)findViewById(R.id.idSlot30);
		imgSlot[3][1] = (ImageView)findViewById(R.id.idSlot31);
		imgSlot[3][2] = (ImageView)findViewById(R.id.idSlot32);
		imgSlot[3][3] = (ImageView)findViewById(R.id.idSlot33);
		imgSlot[3][4] = (ImageView)findViewById(R.id.idSlot34);
		imgSlot[3][5] = (ImageView)findViewById(R.id.idSlot35);
		imgSlot[3][6] = (ImageView)findViewById(R.id.idSlot36);
		imgSlot[3][7] = (ImageView)findViewById(R.id.idSlot37);
		imgSlot[3][8] = (ImageView)findViewById(R.id.idSlot38);
		imgSlot[4][0] = (ImageView)findViewById(R.id.idSlot40);
		imgSlot[4][1] = (ImageView)findViewById(R.id.idSlot41);
		imgSlot[4][2] = (ImageView)findViewById(R.id.idSlot42);
		imgSlot[4][3] = (ImageView)findViewById(R.id.idSlot43);
		imgSlot[4][4] = (ImageView)findViewById(R.id.idSlot44);
		imgSlot[4][5] = (ImageView)findViewById(R.id.idSlot45);
		imgSlot[4][6] = (ImageView)findViewById(R.id.idSlot46);
		imgSlot[4][7] = (ImageView)findViewById(R.id.idSlot47);
		imgSlot[4][8] = (ImageView)findViewById(R.id.idSlot48);
		imgSlot[5][0] = (ImageView)findViewById(R.id.idSlot50);
		imgSlot[5][1] = (ImageView)findViewById(R.id.idSlot51);
		imgSlot[5][2] = (ImageView)findViewById(R.id.idSlot52);
		imgSlot[5][3] = (ImageView)findViewById(R.id.idSlot53);
		imgSlot[5][4] = (ImageView)findViewById(R.id.idSlot54);
		imgSlot[5][5] = (ImageView)findViewById(R.id.idSlot55);
		imgSlot[5][6] = (ImageView)findViewById(R.id.idSlot56);
		imgSlot[5][7] = (ImageView)findViewById(R.id.idSlot57);
		imgSlot[5][8] = (ImageView)findViewById(R.id.idSlot58);
		imgSlot[6][0] = (ImageView)findViewById(R.id.idSlot60);
		imgSlot[6][1] = (ImageView)findViewById(R.id.idSlot61);
		imgSlot[6][2] = (ImageView)findViewById(R.id.idSlot62);
		imgSlot[6][3] = (ImageView)findViewById(R.id.idSlot63);
		imgSlot[6][4] = (ImageView)findViewById(R.id.idSlot64);
		imgSlot[6][5] = (ImageView)findViewById(R.id.idSlot65);
		imgSlot[6][6] = (ImageView)findViewById(R.id.idSlot66);
		imgSlot[6][7] = (ImageView)findViewById(R.id.idSlot67);
		imgSlot[6][8] = (ImageView)findViewById(R.id.idSlot68);
		imgSlot[7][0] = (ImageView)findViewById(R.id.idSlot70);
		imgSlot[7][1] = (ImageView)findViewById(R.id.idSlot71);
		imgSlot[7][2] = (ImageView)findViewById(R.id.idSlot72);
		imgSlot[7][3] = (ImageView)findViewById(R.id.idSlot73);
		imgSlot[7][4] = (ImageView)findViewById(R.id.idSlot74);
		imgSlot[7][5] = (ImageView)findViewById(R.id.idSlot75);
		imgSlot[7][6] = (ImageView)findViewById(R.id.idSlot76);
		imgSlot[7][7] = (ImageView)findViewById(R.id.idSlot77);
		imgSlot[7][8] = (ImageView)findViewById(R.id.idSlot78);
		imgSlot[8][0] = (ImageView)findViewById(R.id.idSlot80);
		imgSlot[8][1] = (ImageView)findViewById(R.id.idSlot81);
		imgSlot[8][2] = (ImageView)findViewById(R.id.idSlot82);
		imgSlot[8][3] = (ImageView)findViewById(R.id.idSlot83);
		imgSlot[8][4] = (ImageView)findViewById(R.id.idSlot84);
		imgSlot[8][5] = (ImageView)findViewById(R.id.idSlot85);
		imgSlot[8][6] = (ImageView)findViewById(R.id.idSlot86);
		imgSlot[8][7] = (ImageView)findViewById(R.id.idSlot87);
		imgSlot[8][8] = (ImageView)findViewById(R.id.idSlot88);
		
		
		


		
		
		
		RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams)imgPebble[2][2].getLayoutParams();

		int space = rl.leftMargin;
		boolean modify = false;
		int newsize = (getMinimumResolution()/effectiveRows)-space;
		
		modify = true;
		

		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				if(pebble[getIndex(i, j)] != -1){
					imgPebble[i][j].setSoundEffectsEnabled(false);
				}
				if(modify){
					RelativeLayout.LayoutParams rel = (RelativeLayout.LayoutParams)imgPebble[i][j].getLayoutParams();
					rel.height = newsize;
					rel.width = newsize;
					imgPebble[i][j].setLayoutParams(rel);
					
					RelativeLayout.LayoutParams rel1 = (RelativeLayout.LayoutParams)imgSlot[i][j].getLayoutParams();
					rel1.height = newsize;
					rel1.width = newsize;
					imgSlot[i][j].setLayoutParams(rel1);
				}
			}
		}


		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				select(i, j, false);
			}
		}

		txtTime = (TextView)findViewById(R.id.idTxtTimeValue);
		btnPause = (ImageView)findViewById(R.id.idBtnPause);
		btnMute = (ImageView)findViewById(R.id.idBtnMute);
		btnMusicMute = (ImageView)findViewById(R.id.idBtnMusicMute);

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
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				if(pebble[getIndex(i, j)] != -1){
					imgPebble[i][j].setOnClickListener(this);
				}
			}
		}


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
				
			}
			break;
		case ACTION_UNDO:
			gamePaused = false;
			undoMove();
			break;
		case ACTION_RESUME:
			gamePaused = false;
			SharedPreferences masPref = this.getSharedPreferences("com.easwareapps.maspebble", MODE_PRIVATE);
			int newboard = masPref.getInt("board", DEFAULT);
			if(board != newboard){
				board = newboard;
				askForRestart();
			}
			break;
		default:
			break;
		}


	}

	private void askForRestart() {
		// TODO Auto-generated method stub
		AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(
				this,R.style.AppTheme));
		alert.setTitle("Confirmation");
		alert.setMessage("Game Board Changed, Do yo want to restart the game ?");					
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
				return;
			}
		}).setPositiveButton("Restart", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// OK.
				restartGame();
			}
		});						
		alert.show();
		
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

		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				select(i, j, false);
			}
		}


	}

	private void undoMove(){
		if(moveIndex < 0){
			return;
		}
		moves--;
		noPebbles++;


		int i = (int)moveDetails[moveIndex][1]/rows;
		int j = (int)moveDetails[moveIndex][1]%cols;
		imgPebble[i][j].setImageResource(R.drawable.ic_no_pebble);
		pebble[getIndex(i, j)] = 0;
		int k = i;
		int l = i;

		i = (int)moveDetails[moveIndex][2]/rows;
		j = (int)moveDetails[moveIndex][2]%cols;
		imgPebble[i][j].setImageResource(pebbleResource);
		imgPebble[i][j].setVisibility(View.VISIBLE);
		pebble[getIndex(i, j)] = 1;


		i = (int)moveDetails[moveIndex][0]/rows;
		j = (int)moveDetails[moveIndex][0]%cols;
		animatde[0] = i;
		animatde[1] = j;


		imgPebble[i][j].setImageResource(pebbleResource);
		imgPebble[i][j].bringToFront();
		
		switch (getMove(k, l, i, j)) {
		case UP:
			imgPebble[i][j].startAnimation(moveUp);
			break;
		case DOWN:
			imgPebble[i][j].startAnimation(moveDown);
			break;
		case LEFT:
			imgPebble[i][j].startAnimation(moveLeft);
			break;
		case RIGHT:
			imgPebble[i][j].startAnimation(moveRight);
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
					imgPebble[animatde[0]][animatde[1]].clearAnimation();
					imgPebble[animatde[0]][animatde[1]].invalidate();

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
		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++){
				if(view == imgPebble[i][j]){
					
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
							moveDetails[moveIndex][0] = Integer.valueOf(lastSelected[0]*rows+lastSelected[1]);
							moveDetails[moveIndex][1] = Integer.valueOf(i*cols+j);
							moves++;
							
							imgPebble[i][j].setImageResource(pebbleResource);
							imgPebble[i][j].setImageLevel(1);
							imgPebble[i][j].bringToFront();
							switch (move) {
							case UP: imgPebble[i][j].startAnimation(moveUp);
							moveDetails[moveIndex][2] = Integer.valueOf(lastSelected[0]*rows + lastSelected[1]-1);
							break;
							case DOWN: imgPebble[i][j].startAnimation(moveDown);
							moveDetails[moveIndex][2] = Integer.valueOf(lastSelected[0]*rows + lastSelected[1]+1);
							break;
							case LEFT: imgPebble[i][j].startAnimation(moveLeft);
							moveDetails[moveIndex][2] = Integer.valueOf(((lastSelected[0]-1)*rows) + lastSelected[1]);
							break;
							case RIGHT: imgPebble[i][j].startAnimation(moveRight);
							moveDetails[moveIndex][2] = Integer.valueOf(((lastSelected[0]+1)*rows) + lastSelected[1]);
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
										imgPebble[animatde[0]][animatde[1]].clearAnimation();
										imgPebble[animatde[0]][animatde[1]].invalidate();

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
								int currentBoard = masPref.getInt("board", 0);
								if(prefEditor == null){
									prefEditor = masPref.edit();
								}
								prefEditor.putBoolean("game"+currentBoard+"_finished", true);
								prefEditor.commit();
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
		if(pebble[getIndex(i, j)] == -2){
			imgPebble[i][j].setVisibility(View.GONE);
			imgSlot[i][j].setVisibility(View.GONE);
		}else{
			if(pebble[getIndex(i, j)] == 1){
				if(type)	imgPebble[i][j].setImageResource(selectedPebbleResource);
				else		imgPebble[i][j].setImageResource(pebbleResource);
			}else if(pebble[getIndex(i, j)] == 0){
				imgPebble[i][j].setImageResource(R.drawable.ic_no_pebble);
			}else if(pebble[getIndex(i, j)] == -1){
				imgPebble[i][j].setImageResource(R.drawable.ic_empty_pebble);
				imgSlot[i][j].setImageResource(R.drawable.ic_empty_pebble);
			}
			imgPebble[i][j].setVisibility(View.VISIBLE);
			imgSlot[i][j].setVisibility(View.VISIBLE);
		}
		

	}

	private void proceedMove(int move) {
		// TODO Auto-generated method stub
		switch (move) {
		case RIGHT:

			throwAwayPebble(lastSelected[0]+1, lastSelected[1]);
			imgPebble[lastSelected[0]][lastSelected[1]].setImageResource(R.drawable.ic_no_pebble);
			pebble[getIndex(lastSelected[0]+1, lastSelected[1])] = 0;
			pebble[getIndex(lastSelected[0], lastSelected[1])] = 0;
			break;
		case LEFT:
			throwAwayPebble(lastSelected[0]-1, lastSelected[1]);
			imgPebble[lastSelected[0]][lastSelected[1]].setImageResource(R.drawable.ic_no_pebble);
			pebble[getIndex(lastSelected[0]-1, lastSelected[1])] = 0;
			pebble[getIndex(lastSelected[0], lastSelected[1])] = 0;
			break;
		case DOWN:
			throwAwayPebble(lastSelected[0], lastSelected[1]+1);
			imgPebble[lastSelected[0]][lastSelected[1]].setImageResource(R.drawable.ic_no_pebble);
			pebble[getIndex(lastSelected[0], lastSelected[1]+1)] = 0;
			pebble[getIndex(lastSelected[0], lastSelected[1])] = 0;
			break;
		case UP:
			throwAwayPebble(lastSelected[0], lastSelected[1]-1);
			imgPebble[lastSelected[0]][lastSelected[1]].setImageResource(R.drawable.ic_no_pebble);
			pebble[getIndex(lastSelected[0], lastSelected[1]-1)] = 0;
			pebble[getIndex(lastSelected[0], lastSelected[1])] = 0;
			break;
		default:
			break;
		}



	}

	private void throwAwayPebble(final int i,final int j){
		imgPebble[i][j].setImageResource(R.drawable.ic_no_pebble);
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
		return i*rows+j;
	}

	private boolean checkDeadLock(){
		for(int i=0; i<pebble.length;i++){
			if(pebble[i] == 1){
				if(checkMoveExsistForPebble((int)(i/rows),(int)(i%cols))){
					return false;
				}
			}
		}
		gameEnd = true;
		return true;
	}

	private boolean checkMoveExsistForPebble(int i, int j) {
		// TODO Auto-generated method stub
		if(i+2 < rows)
			if(isMoveAllowed(i+2, j, i, j) >= 0)	return true;

		if(i-2 >= 0)
			if(isMoveAllowed(i-2, j, i, j) >= 0)	return true;

		if(j+2 < cols)
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
			if(gameEnd){
				try{
					handler.removeCallbacks(updateTimeTask);
				}catch(Exception e){
					
				}
				
			}


		}
		
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
