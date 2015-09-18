package itpsoft.englishvocabulary.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import itpsoft.englishvocabulary.PopubVocaActivity;
import itpsoft.englishvocabulary.receiver.ScreenOnReceiver;
import itpsoft.englishvocabulary.ultils.SPUtil;

/**
 * Created by Do on 11/09/2015.
 */
public class ScreenOnService extends Service {
    BroadcastReceiver mReceiver=null;
    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // Register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenOnReceiver();
        registerReceiver(mReceiver, filter);
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("NgaDV", "Service  distroy");
        if(mReceiver!=null)
            unregisterReceiver(mReceiver);
    }


    /**
     * @param intent
     * @param startId
     * @deprecated Implement {@link #onStartCommand(Intent, int, int)} instead.
     */
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        boolean screenOn = true;

        try{
            // Get ON/OFF values sent from receiver ( AEScreenOnOffReceiver.java )
            screenOn = intent.getBooleanExtra("screen_state", false);

        }catch(Exception e){}

        //  Toast.makeText(getBaseContext(), "Service on start :"+screenOn,
        //Toast.LENGTH_SHORT).show();

        if (screenOn) {
//            int num_screen_on = SPUtil.instance(ScreenOnService.this).get(SPUtil.KEY_NUM_SCREEN_ON, 1);
            int first_run_popub_activity = SPUtil.instance(ScreenOnService.this).get(SPUtil.KEY_FIRST_RUN_POPUB_ACTIVITY, 1);
            // Some time required to start any service
            if (first_run_popub_activity == 1){
                Log.d("NgaDV", "KEY_FIRST_RUN_POPUB_ACTIVITY: in if " + SPUtil.instance(ScreenOnService.this).get(SPUtil.KEY_FIRST_RUN_POPUB_ACTIVITY, 1) + "");
                Intent intent1 = new Intent(ScreenOnService.this, PopubVocaActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                Toast.makeText(getBaseContext(), "Screen on, ", Toast.LENGTH_LONG).show();
            }
        } else {
//           getBaseContext(), "Screen off,", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
