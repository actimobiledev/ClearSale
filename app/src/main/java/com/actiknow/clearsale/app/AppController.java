package com.actiknow.clearsale.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.actiknow.clearsale.utils.LruBitmapCache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();
	private static AppController mInstance;
	private static Context context;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	public static synchronized AppController getInstance () {
		return mInstance;
	}

	public static Context getAppContext () {
		return AppController.context;
	}
    
    @Override
    protected void attachBaseContext (Context context) {
        super.attachBaseContext (context);
        MultiDex.install (this);
    }
    
    @Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		AppController.context = getApplicationContext ();
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty (tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}


//	@Override
//	protected void attachBaseContext (Context base) {
//		super.attachBaseContext (base);
//		MultiDex.install (this);
//	}

}