package itpsoft.englishvocabulary.google.adapter;

import itpsoft.englishvocabulary.google.database.NotificationDBController;
import itpsoft.englishvocabulary.google.model.Notification;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import itpsoft.englishvocabulary.R;

public class NotificationListAdapter extends BaseAdapter {
	private Context context;

	private Cursor cursor;
	private NotificationDBController db;
	ArrayList<Notification> data;

	public NotificationListAdapter(Context context, ArrayList<Notification> data) {
		this.context = context;
		this.data = data;
	}

	public void setData(ArrayList<Notification> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Notification getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return data.get(arg0).getId();
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		final ViewHolder holder;
		final Notification notification = getItem(position);
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.item_notificationlist, viewGroup, false);
			holder = new ViewHolder();
			holder.title = (TextView) view
					.findViewById(R.id.item_notificationlist_title);
			holder.content = (TextView) view
					.findViewById(R.id.item_notificationlist_content);
			holder.date = (TextView) view
					.findViewById(R.id.item_notificationlist_date);
			holder.time = (TextView) view
					.findViewById(R.id.item_notificationlist_time);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

//		long id = notification.getId();
//		String state = querryFromDB(context, id);
//		if (state
//				.equalsIgnoreCase(NotificationDBController.NOTIFICATION_STATE_NEW)) {
//			view.setBackgroundColor(Color.DKGRAY);
//		} else {
//			view.setBackgroundColor(Color.WHITE);
//		}

		holder.title.setText(notification.getTitle());
		holder.content.setText(notification.getContent());
		holder.date.setText(notification.getDate());
		holder.time.setText(notification.getTime());
		return view;
	}

	private class ViewHolder {
		TextView title, content, date, time;
	}

	String querryFromDB(Context context, long position) {
		String state = "";
		db = NotificationDBController.getInstance(context);
		cursor = db.query(NotificationDBController.TABLE_NAME, null, null,
				null, null, null, null);
		String sql = "Select * from " + NotificationDBController.TABLE_NAME
				+ " where " + NotificationDBController.ID_COL + " = "
				+ position;
		Log.d("GCM_PHP", "task adapter sql : " + sql);
		cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				state = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.STATE_COL));
			} while (cursor.moveToNext());
		}
		return state;
	}

}