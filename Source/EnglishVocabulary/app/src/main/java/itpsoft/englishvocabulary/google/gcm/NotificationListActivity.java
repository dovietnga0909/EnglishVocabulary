package itpsoft.englishvocabulary.google.gcm;


import itpsoft.englishvocabulary.google.adapter.NotificationListAdapter;
import itpsoft.englishvocabulary.google.database.NotificationDBController;
import itpsoft.englishvocabulary.google.model.Notification;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import itpsoft.englishvocabulary.R;

public class NotificationListActivity extends Activity {

	ListView listView;
	NotificationListAdapter adapter;
	NotificationDBController db;
	ArrayList<Notification> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notificationlist);
		db = NotificationDBController.getInstance(getApplicationContext());
		getNotificationData();

		listView = (ListView) findViewById(R.id.notificationlist_listView);
		adapter = new NotificationListAdapter(getApplicationContext(), data);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// db.checkedNotification(data.get(arg2),
				// data.get(arg2).getId());
			}

		});
	}

	void getNotificationData() {
		data = new ArrayList<Notification>();
		Cursor cursor = db.query(NotificationDBController.TABLE_NAME, null,
				null, null, null, null, null);
		String sql = "Select * from " + NotificationDBController.TABLE_NAME
		// + " where " + NotificationDBController.STATE_COL + " = \"new\""
		;
		Log.d("GCM_PHP", "sql : " + sql);
		cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				int id = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(NotificationDBController.ID_COL));
				String title = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.TITLE_COL));
				String content = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.CONTENT_COL));
				String url_image = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.URL_IMAGE_COL));
				String url = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.URL_COL));
				String date = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.DATE_COL));
				String time = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.TIME_COL));
				// add to arraylist
				
				Notification model = new Notification(id, title, content,
						url_image, url, date, time,
						NotificationDBController.NOTIFICATION_STATE_NEW);
				data.add(model);
				Log.d("GCM_PHP",
						"data size after init Data is : " + data.size());
			} while (cursor.moveToNext());
		}
	}
}
