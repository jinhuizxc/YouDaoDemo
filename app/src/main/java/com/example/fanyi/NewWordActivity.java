package com.example.fanyi;



import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewWordActivity extends Activity {

	SQLiteDatabase db;
	TextView tv;
	LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newd);

		layout =(LinearLayout) findViewById(R.id.re);



		// tv = (TextView) findViewById(R.id.textView2);
		MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(this,
				"ming.db", null, 3);
		db = mySqliteOpenHelper.getWritableDatabase();

		Cursor cursor = db.query(MySqliteOpenHelper.TABLE_NAME, null, null,
				null, null, null, null);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			int id = cursor.getInt(cursor.getColumnIndex("id"));

			String name = cursor.getString(cursor
					.getColumnIndex(MySqliteOpenHelper.WORD_NAME));
//			tv.append(id + "   " + name + "\n");
			TextView tv = new TextView(this);
			tv.setText(name);
			layout.addView(tv);

			registerForContextMenu(tv);
		}

	}

	TextView tv1;
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.addSubMenu(0,1,0,"删除");
		tv1 = (TextView) v;
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Log.d("Tag", "111");
		if(item.getItemId()==1){
			db.delete(MySqliteOpenHelper.TABLE_NAME, "name=?", new String[]{tv1.getText().toString()});
			layout.removeView(tv1);
		}

		return super.onContextItemSelected(item);
	}

}
