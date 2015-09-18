package itpsoft.englishvocabulary.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.gcm.GcmListenerService;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import itpsoft.englishvocabulary.R;

/**
 * Created by LuanDT on 17/09/2015.
 */
public class GcmListenerServices extends GcmListenerService {
    public GcmListenerServices() {
        super();
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String title = data.getString("title" );
        String content = data.getString("content");
        String url_image = data.getString("url_image");
        String link = data.getString("link");
//        try {
//            title = URLDecoder.decode(title, "UTF-8");
//            content = URLDecoder.decode(content, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        Log.d("LuanDT", "title: " + title);
        generateNotification(title, content, url_image, link);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String msgId) {
        super.onMessageSent(msgId);
    }

    @Override
    public void onSendError(String msgId, String error) {
        super.onSendError(msgId, error);
    }

    private void generateNotification(String title, String content, String url_image, String link) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this);

        int build_version = android.os.Build.VERSION.SDK_INT;
        if (build_version >= 16) {
            notiBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText(content);
        style.setSummaryText(getString(R.string.app_name));
        style.build();

        // Advance
        long[] pattern = {(long) 500, (long) 500, (long) 500, (long) 500,
                (long) 500};

        notiBuilder
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setStyle(style)
                .setContentText(content)
                .setContentInfo(getString(R.string.click))
                .setSmallIcon(R.drawable.ic_logo1)
                .setPriority(2)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setOngoing(false)
                .setAutoCancel(true)
                .setLights(0xFFFFFFFF, 500, 500)
                .setVibrate(pattern)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        Bitmap bitmap = getUrlImage(url_image);
        if(bitmap != null){
            notiBuilder.setLargeIcon(bitmap);
        }

        Notification notification = notiBuilder.getNotification();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification);

    }

    public Bitmap getUrlImage(String url) {
        Bitmap img = null;
        try {
            InputStream in = new URL(url).openStream();
            img = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

}
