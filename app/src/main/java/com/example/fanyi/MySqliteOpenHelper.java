package com.example.fanyi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteOpenHelper extends SQLiteOpenHelper {

	public static final String TABLE_NAME ="newWord";
	public static final String WORD_NAME ="name";


	public MySqliteOpenHelper(Context context, String name,
							  CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//注意加空格
		db.execSQL("create table "+TABLE_NAME + "(id integer primary key autoincrement," + WORD_NAME + " char(10));");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
