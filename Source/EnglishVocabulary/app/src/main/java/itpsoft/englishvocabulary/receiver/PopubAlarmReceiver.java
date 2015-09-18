package itpsoft.englishvocabulary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import itpsoft.englishvocabulary.AboutActivity;

/**
 * Created by Do on 11/09/2015.
 */
public class PopubAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AboutActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
