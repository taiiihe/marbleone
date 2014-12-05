package com.easwareapps.marbleone_ad_free;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SelectBoard extends Activity implements OnClickListener {

	int NO_BOARDS = 8;
	int BOARD = 0;
	ImageView imgBoards[] = new ImageView[NO_BOARDS];
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		

		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_board);

		
		imgBoards[0] = (ImageView)findViewById(R.id.idImgBoard0);
		imgBoards[1] = (ImageView)findViewById(R.id.idImgBoard1);
		imgBoards[2] = (ImageView)findViewById(R.id.idImgBoard2);
		imgBoards[3] = (ImageView)findViewById(R.id.idImgBoard3);
		imgBoards[4] = (ImageView)findViewById(R.id.idImgBoard4);
		imgBoards[5] = (ImageView)findViewById(R.id.idImgBoard5);
		imgBoards[6] = (ImageView)findViewById(R.id.idImgBoard6);
		imgBoards[7] = (ImageView)findViewById(R.id.idImgBoard7);
		
		for(int i=0;i<NO_BOARDS;i++){
			imgBoards[i].setOnClickListener(this);
		}
		

		SharedPreferences masPref = this.getSharedPreferences("com.easwareapps.maspebble", MODE_PRIVATE);
		int index = masPref.getInt("board", BOARD);
		
		for(int i=0;i<NO_BOARDS;i++){
			if(masPref.getBoolean("game"+i+"_finished", false)){
				imgBoards[i].setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));
			}
		}
		
		if(masPref.getBoolean("game"+index+"_finished", false)){
			imgBoards[index].setImageResource(R.drawable.ic_select_frame_finished);
		}else{
			imgBoards[index].setImageResource(R.drawable.ic_select_frame);
		}
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences masPref = this.getSharedPreferences("com.easwareapps.maspebble", MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = masPref.edit();
		for(int i=0;i<NO_BOARDS;i++){
			if(v == imgBoards[i]){
				changeBoard(i);
			}
		}
		
		prefEditor.commit();
		
	}
	
	private void changeBoard(int index) {
		// TODO Auto-generated method stub
		SharedPreferences masPref = this.getSharedPreferences("com.easwareapps.maspebble", MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = masPref.edit();
		
		int currentIndex = masPref.getInt("board", BOARD);
		if(masPref.getBoolean("game"+currentIndex+"_finished", false)){
			imgBoards[currentIndex].setImageResource(R.drawable.ic_tick);
		}else{
			imgBoards[currentIndex].setImageResource(R.drawable.ic_empty_pebble);
		}
		prefEditor.putInt("board", index);
		if(masPref.getBoolean("game"+index+"_finished", false)){
			imgBoards[index].setImageResource(R.drawable.ic_select_frame_finished);
		}else{
			imgBoards[index].setImageResource(R.drawable.ic_select_frame);
		}
		imgBoards[index].invalidate();
		prefEditor.commit();
		
	}

	public void finish(View view){
		finish();
	}
	
	

}
