package com.easwareapps.marbleone_ad_free;

import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class BitmapManager extends AsyncTask<Integer, Void, Bitmap> {

	private final WeakReference<ImageView> imageViewReference;
	private int res = 0;
	Resources resource = null;
	LruCache<String, Bitmap> cache = null;

	public BitmapManager(ImageView imageView, Resources r, LruCache<String , Bitmap> cache) {
		// Use a WeakReference to ensure the ImageView can be garbage collected
		this.cache = cache;  
		imageViewReference = new WeakReference<ImageView>(imageView);
		resource = r;
	}

	@Override
	protected Bitmap doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		res = params[0];
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = 4;
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeResource(resource, res, options);
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		// TODO Auto-generated method stub
		if(isCancelled()){
			bitmap = null;
		}
		if(imageViewReference != null && bitmap != null){
			ImageView imageView = imageViewReference.get();
			imageView.setImageBitmap(bitmap);
			cache.put(String.valueOf(res), bitmap);

		}
		super.onPostExecute(bitmap);
	}

	
}