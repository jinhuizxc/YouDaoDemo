package com.example.fanyi;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.Application;

public class MyApp extends Application{

	public static RequestQueue queue;
	@Override
	public void onCreate() {

		queue = Volley.newRequestQueue(getApplicationContext());
		super.onCreate();
	}
	public static RequestQueue getRequestQueue(){
		return queue;
	}
}
