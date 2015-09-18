package itpsoft.englishvocabulary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import itpsoft.englishvocabulary.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AlarmActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}