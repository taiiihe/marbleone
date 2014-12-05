package com.easwareapps.marbleone_ad_free;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SelectMarble extends Activity implements OnClickListener {

	int NO_MARBLES = 5;
	int BLUE = 0;
	int RED = 3;
	ImageView imgMarbles[] = new ImageView[NO_MARBLES];
	ImageView imgSelectedMarbles[] = new ImageView[NO_MARBLES];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_marble_color);
		
		imgMarbles[0] = (ImageView)findViewById(R.id.idImgMarbleBlue);
		imgMarbles[1] = (ImageView)findViewById(R.id.idImgMarbleGreen);
		imgMarbles[2] = (ImageView)findViewById(R.id.idImgMarbleMagenta);
		imgMarbles[3] = (ImageView)findViewById(R.id.idImgMarbleRed);
		imgMarbles[4] = (ImageView)findViewById(R.id.idImgMarbleTurquoise);
		
		imgSelectedMarbles[0] = (ImageView)findViewById(R.id.idImgSelectedMarbleBlue);
		imgSelectedMarbles[1] = (ImageView)findViewById(R.id.idImgSelectedMarbleGreen);
		imgSelectedMarbles[2] = (ImageView)findViewById(R.id.idImgSelectedMarbleMagenta);
		imgSelectedMarbles[3] = (ImageView)findViewById(R.id.idImgSelectedMarbleRed);
		imgSelectedMarbles[4] = (ImageView)findViewById(R.id.idImgSelectedMarbleTurquoise);
		
		imgMarbles[0].setOnClickListener(this);
		imgMarbles[1].setOnClickListener(this);
		imgMarbles[2].setOnClickListener(this);
		imgMarbles[3].setOnClickListener(this);
		imgMarbles[4].setOnClickListener(this);

		imgSelectedMarbles[0].setOnClickListener(this);
		imgSelectedMarbles[1].setOnClickListener(this);
		imgSelectedMarbles[2].setOnClickListener(this);
		imgSelectedMarbles[3].setOnClickListener(this);
		imgSelectedMarbles[4].setOnClickListener(this);
		SharedPreferences masPref = this.getSharedPreferences("com.easwareapps.maspebble", MODE_PRIVATE);
		int index = masPref.getInt("pebble", BLUE);
		Log.d("INDEX", index+"");
		imgMarbles[index].setImageResource(R.drawable.ic_marble_select_frame);
		index = masPref.getInt("selected_pebble", RED);
		imgSelectedMarbles[index].setImageResource(R.drawable.ic_marble_select_frame);
		
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences masPref = this.getSharedPreferences("com.easwareapps.maspebble", MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = masPref.edit();
		for(int i=0;i<NO_MARBLES;i++){
			if(v == imgMarbles[i]){
				changeMarble(i);
				if(masPref.getInt("selected_pebble", RED) == i){
					changeSelectedMarble((i+2)%5);
				}
				return;
			}
		}
		for(int i=0;i<NO_MARBLES;i++){
			if(v == imgSelectedMarbles[i]){
				changeSelectedMarble(i);
				if(masPref.getInt("pebble", BLUE) == i){
					changeMarble((i+5+2)%5);
				}
				return;
			}
		}
		prefEditor.commit();
		
	}
	
	private void changeMarble(int index) {
		// TODO Auto-generated method stub
		Log.d("index", index+"");
		SharedPreferences masPref = this.getSharedPreferences("com.easwareapps.maspebble", MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = masPref.edit();
		
		int currentIndex = masPref.getInt("pebble", BLUE);
		imgMarbles[currentIndex].setImageResource(R.drawable.ic_empty_pebble);
		prefEditor.putInt("pebble", index);
		imgMarbles[index].setImageResource(R.drawable.ic_marble_select_frame);
		imgMarbles[index].invalidate();
		prefEditor.commit();
		
	}
	
	private void changeSelectedMarble(int i) {
		// TODO Auto-generated method stub
		SharedPreferences masPref = this.getSharedPreferences("com.easwareapps.maspebble", MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = masPref.edit();
		
		int currentIndex = masPref.getInt("selected_pebble", RED);
		Log.d("currentIndex", currentIndex+"");
		imgSelectedMarbles[currentIndex].setImageResource(R.drawable.ic_empty_pebble);
		
		prefEditor.putInt("selected_pebble", i);
		imgSelectedMarbles[i].setImageResource(R.drawable.ic_marble_select_frame);
		imgSelectedMarbles[i].invalidate();
		prefEditor.commit();
		
	}


	public void finish(View view){
		finish();
	}

}
