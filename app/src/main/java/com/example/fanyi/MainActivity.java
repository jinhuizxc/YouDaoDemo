package com.example.fanyi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lidroid.xutils.ViewUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	ProgressBar bar;
	TextView tv;
	EditText edit;
	String s1;
	StringBuffer buffer = new StringBuffer();
	SQLiteDatabase db;
	String name2;
	boolean flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(this,
				"ming.db", null, 3);
		db = mySqliteOpenHelper.getWritableDatabase();

		ViewUtils.inject(this);

		bar = (ProgressBar) findViewById(R.id.progressBar1);
		tv = (TextView) findViewById(R.id.textView1);
		edit = (EditText) findViewById(R.id.editText1);

		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.button2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String s3 = edit.getText().toString();
				ContentValues values = new ContentValues();
				values.put(MySqliteOpenHelper.WORD_NAME, s3);
				Cursor cursor = db.query(MySqliteOpenHelper.TABLE_NAME, null,
						null, null, null, null, null);
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					name2 = cursor.getString(cursor
							.getColumnIndex(MySqliteOpenHelper.WORD_NAME));
					if (!s3.equals(name2)) {
						flag = true;
					} else if (s3.equals(name2)) {
						flag = false;
						Toast.makeText(MainActivity.this, "生词本已有",
								Toast.LENGTH_SHORT).show();
					}
				}
				if (flag) {
					// 插入生词
					db.insert(MySqliteOpenHelper.TABLE_NAME, null, values);
					Log.d("Tag", "000");
					Toast.makeText(MainActivity.this, "成功添加一条记录",
							Toast.LENGTH_SHORT).show();

				}

			}
		});
		findViewById(R.id.button3).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, NewWordActivity.class));

			}
		});

	}

	@Override
	public void onClick(View v) {
		tv.setText("");
		bar.setVisibility(View.VISIBLE);
		buffer = new StringBuffer();
		String s = edit.getText().toString();
		byte b[] = s.getBytes();
		int i = b.length;
		int j = s.length();


		try {
			s1 = URLEncoder.encode(edit.getText().toString(), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String url = "http://fanyi.youdao.com/openapi.do?keyfrom=dicFarsight&key=305582204&type=data&doctype=json&version=1.1&q="
				+ s1;
		StringRequest request = new StringRequest(Method.GET, url,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						bar.setVisibility(View.INVISIBLE);

						try {
							JSONObject object01 = new JSONObject(response);
							// "translation"
							buffer.append("翻译：\n\t");
							appendString(object01, "translation");
							buffer.append("\n");
							// explains
							buffer.append("解释：\n\t");
							JSONObject jarr03 = object01.getJSONObject("basic");
							appendString(jarr03, "explains");
							buffer.append("\n");

							// web
							buffer.append("网络释义：\n");
							JSONArray jarr02 = object01.getJSONArray("web");
							for (int i = 0; i < jarr02.length(); i++) {
								JSONObject json = jarr02.getJSONObject(i);
								buffer.append("\t" + json.getString("key")
										+ "：");
								appendString(json, "value");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						tv.setText(buffer.toString());
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});

		request.setTag("youdao");
		MyApp.getRequestQueue().add(request);

	}

	public void appendString(JSONObject object01, String str) {
		try {
			JSONArray jsonArr = object01.getJSONArray(str);
			for (int i = 0; i < jsonArr.length(); i++) {
				buffer.append(jsonArr.get(i) + "\t");
			}
			Log.d("tag", buffer.toString() + jsonArr.length());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		buffer.append("\n");
	}

}
