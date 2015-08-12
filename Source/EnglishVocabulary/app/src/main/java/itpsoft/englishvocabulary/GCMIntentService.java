package itpsoft.englishvocabulary;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.format.Time;
import android.util.DisplayMetrics;

import com.google.android.gcm.GCMBaseIntentService;

import java.io.InputStream;
import java.util.StringTokenizer;

import itpsoft.englishvocabulary.google.database.NotificationDBController;
import itpsoft.englishvocabulary.google.gcm.NotificationListActivity;
import itpsoft.englishvocabulary.google.gcm.ServerUtilities;
import itpsoft.englishvocabulary.google.utils.CommonUtilities;
import itpsoft.englishvocabulary.google.utils.Utils;
import itpsoft.englishvocabulary.ultils.Log;


public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCM_PHP";
	static PendingIntent pendInt;
	static Bitmap bitmap;
	NotificationDBController db;
	String ID_KEY = "NOTIFICATION_ID";

	public GCMIntentService() {
		super(CommonUtilities.SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		ServerUtilities.register(context, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		ServerUtilities.unregister(context, registrationId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");

		Log.d(TAG, "intent : " + intent);
		String title = intent.getExtras().getString("title");
		String message = intent.getExtras().getString("message");
		String url_image = intent.getExtras().getString("url_image");
		String notification_type = intent.getExtras().getString(
				"notification_type");
		String url = intent.getExtras().getString("url");
		Log.d(TAG, "title : " + title + " , message : " + message
				+ " , url_image : " + url_image + " , notification_type :"
				+ notification_type + " , url : " + url);
		
		if (title != null && message != null) {
			generateNotification(context, title, message, url_image, url,
					notification_type);
		}

	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
	}

	@Override
	public void onError(Context context, String errorId) {
		
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		return super.onRecoverableError(context, errorId);
	}

	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	void generateNotification(Context context, String title, String content,
			String url_image, String url, String notification_type) {
		// get ID, then save it to SharePref
		int id = Utils.getInt(context, ID_KEY);
		id++;
		Utils.saveInt(context, ID_KEY, id);
		// add data to db
		addNotificationToDB(id, title, content, url_image, url);
		
		// ====================================================================================== \\
		// show Notification
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		Intent intent = null;

		checkNotificationType(context, notification_type, url, intent);

		int build_version = android.os.Build.VERSION.SDK_INT;
		if (build_version >= 16) {
			builder.setPriority(Notification.PRIORITY_HIGH);
		}

		NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
		style.bigText(content);
		style.setSummaryText("ITP-Soft");
		style.build();

		// Advance
		long[] pattern = { (long) 500, (long) 500, (long) 500, (long) 500,
				(long) 500 };
		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		builder.setContentIntent(pendInt).setSmallIcon(R.drawable.ic_logo1)
				.setContentTitle(title).setContentText(content)
				.setOngoing(false).setAutoCancel(true).setStyle(style)
				.setLights(0xFFFFFFFF, 500, 500).setVibrate(pattern)
				.setSound(alarmSound);

		Notification notification = builder.getNotification();
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		// notification.setLatestEventInfo(context, title, content, pendInt);

		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		manager.notify(0, notification);

	}

	void checkNotificationType(Context context, String notification_type,
			String url, Intent intent) {
		if (notification_type.contains("application")) {
			intent = new Intent(context, NotificationListActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		if (notification_type.contains("googleplay")) {
			try {
				StringTokenizer st = new StringTokenizer(url, "?");
				while (st.hasMoreTokens()) {
					System.out.println(st.nextToken());
				}
				intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://details?id=" + st));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			} catch (ActivityNotFoundException ex) {

			}
		}
		if (notification_type.contains("youtobe")) {
			try {
				StringTokenizer st = new StringTokenizer(url, "?v=");
				while (st.hasMoreTokens()) {
					System.out.println(st.nextToken());
				}
				intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("vnd.youtube://" + st));
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			} catch (ActivityNotFoundException ex) {

			}
		}
		if (notification_type.contains("website")) {
			try {
				// if (!url.startsWith("http://") &&
				// !url.startsWith("https://")){
				// url = "http://" + url;
				// }
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			} catch (ActivityNotFoundException ex) {

			}
		}
		createBackStackActivity(context, intent);
	}
	
	void createBackStackActivity(Context context, Intent intent){
		android.support.v4.app.TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(NotificationListActivity.class);
		stackBuilder.addNextIntent(intent);
		pendInt = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		
	}
	
	Bitmap getUrlImage(String... urls) {
		String url = urls[0];
		Bitmap icon = null;
		try {
			InputStream in = new java.net.URL(url).openStream();
			icon = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		bitmap = icon;
		return icon;
	}

	float getImageFactor(Resources r) {
		DisplayMetrics metrics = r.getDisplayMetrics();
		float multiplier = metrics.density / 3f;
		return multiplier;
	}

	class LoadUrlImage extends AsyncTask<String, Void, Bitmap> {
		NotificationCompat.Builder builder;

		public LoadUrlImage(NotificationCompat.Builder builder) {
			this.builder = builder;
		}

		protected Bitmap doInBackground(String... urls) {
			String url = urls[0];
			Bitmap icon = null;
			try {
				InputStream in = new java.net.URL(url).openStream();
				icon = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return icon;
		}

		protected void onPostExecute(Bitmap result) {
			builder.setLargeIcon(result);
		}
	}

	// =========================== DATABASE =========================== \\

	void addNotificationToDB(int id, String title, String content,
			String url_image, String url) {
		db = NotificationDBController.getInstance(getApplicationContext());
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();

		String date = "" + today.monthDay + " " + today.month + " "
				+ today.year;
		String time = today.format("%k:%M:%S");
		
		ContentValues values = new ContentValues();
		values.put(NotificationDBController.ID_COL, id);
		values.put(NotificationDBController.TITLE_COL, title);
		values.put(NotificationDBController.CONTENT_COL, content);
		values.put(NotificationDBController.URL_IMAGE_COL, url_image);
		values.put(NotificationDBController.URL_COL, url);
		values.put(NotificationDBController.DATE_COL, date);
		values.put(NotificationDBController.TIME_COL, time);
		
		long rowInserted = db.insert(NotificationDBController.TABLE_NAME, null,
				values);
		Log.d(TAG, "rowInserted is : " + rowInserted);
	}

}
